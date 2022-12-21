#include <cstdlib>
#include <deque>
#include <iostream>
#include <list>
#include <memory>
#include <set>
#include <utility>
#include <boost/asio.hpp>
#include "chat_message.hpp"
#include <boost/asio/ssl.hpp>
#include "rapidjson/document.h"
#include "rapidjson/writer.h"
#include "rapidjson/stringbuffer.h"


using boost::asio::ip::tcp;


//----------------------------------------------------------------------

typedef std::deque<chat_message> message_queue;

//----------------------------------------------------------------------

class chat_participant
{
public:
  virtual ~chat_participant() {}
  virtual void deliver(const chat_message& msg) = 0;
};

typedef std::shared_ptr<chat_participant> participant_ptr;

//----------------------------------------------------------------------

class chat_room
{
public:
  void join(participant_ptr participant)
  {
    participants_.insert(participant);
  }

  void leave(participant_ptr participant)
  {
    participants_.erase(participant);
  }

  void deliver(const chat_message& msg, participant_ptr sender)
  {
    recent_msgs_.push_back(msg);
    while (recent_msgs_.size() > max_recent_msgs)
      recent_msgs_.pop_front();

    for (auto participant: participants_){
      if(participant == sender) participant->deliver(msg);
      
    }
      
  }


private:
  std::set<participant_ptr> participants_;
  enum { max_recent_msgs = 1 };
  message_queue recent_msgs_;
};

//----------------------------------------------------------------------

class chat_session
  : public chat_participant,
    public std::enable_shared_from_this<chat_session>
{
public:
  chat_session(tcp::socket socket,
      boost::asio::ssl::context& ctx, chat_room& room)
    : socket_(std::move(socket), ctx),
      room_(room)
  {
    
  }

  void start()
  {
    socket_.handshake(boost::asio::ssl::stream_base::server);
    room_.join(shared_from_this());
    do_read_header();
  }

  void deliver(const chat_message& msg)
  {
    bool write_in_progress = !write_msgs_.empty();
    write_msgs_.push_back(msg);
    if (!write_in_progress)
    {
      do_write();
    }
  }

private:
  void do_read_header()
  {
    auto self(shared_from_this());
    boost::asio::async_read(socket_,boost::asio::buffer(read_msg_.data(), chat_message::header_length),
        [this, self](boost::system::error_code ec, std::size_t /*length*/)
        {
          
          if (!ec && read_msg_.decode_header())
          {

            do_read_body();
          }
          else
          {
            room_.leave(shared_from_this());
          }
        });
  }

  void do_read_body()
  {
    auto self(shared_from_this());
    boost::asio::async_read(socket_,
        boost::asio::buffer(read_msg_.body(), read_msg_.body_length()),
        [this, self](boost::system::error_code ec, std::size_t /*length*/)
        {
          if (!ec)
          {
            
            process_request();
            room_.deliver(read_msg_, shared_from_this());
            
            do_read_header();
          }
          else
          {
            room_.leave(shared_from_this());
          }
        });
  }

  void do_write()
  {
    auto self(shared_from_this());
    boost::asio::async_write(socket_,
        boost::asio::buffer(write_msgs_.front().data(),
          write_msgs_.front().length()),
        [this, self](boost::system::error_code ec, std::size_t /*length*/)
        {
          if (!ec)
          {
            write_msgs_.pop_front();
            if (!write_msgs_.empty())
            {
              do_write();
            }
          }
          else
          {
            room_.leave(shared_from_this());
          }
        });
  }

  rapidjson::Document extract_data(std::istream& input_stream)
  {
      std::string request_data(std::istreambuf_iterator<char>(input_stream), {});
      std::string delimiter = "\r\n\r\n";
      size_t pos = request_data.find(delimiter);
      if(pos != std::string::npos)
      {
        std::string data = request_data.substr(pos + delimiter.length());
        std::cout << data << std::endl;

        rapidjson::Document document;
        document.Parse(data.c_str());
        if(document.HasParseError()) std::cout << "Error parsing the data" << std::endl;
        else
        {
          return document;
          //std::cout << "speed = " << document["speed"].GetInt() << std::endl;
          //std::cout << "fuel = " << document["fuel"].GetInt() << std::endl;
        }
      }
      return rapidjson::Document();
  }

  void process_request(){
    boost::asio::streambuf request_buffer;
    request_buffer.sputn(read_msg_.body(), read_msg_.body_length());
    std::istream input_stream(&request_buffer);

    std::string method;
    std::getline(input_stream, method, ' ');


    if(method == "GET")
    {
      const char* response_header_OK = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n";
      rapidjson::StringBuffer sb;
      rapidjson::Writer<rapidjson::StringBuffer> writer(sb);

      writer.StartObject();
      writer.Key("speed");
      writer.Int(speed_);
      writer.Key("fuel");
      writer.Int(fuel_);
      writer.EndObject();

      const std::string json_str = sb.GetString();

      std::string response = response_header_OK + json_str;

      read_msg_.body_length(std::strlen(response.c_str()));
      std::memcpy(read_msg_.body(), response.c_str(), read_msg_.body_length());
      read_msg_.encode_header();

    }
    else if(method == "PUT")
    {
      rapidjson::Document document = extract_data(input_stream);
      if(document.IsNull()) std::cout << "Rapidjson document is empty" << std::endl;
      else
      {
        speed_ = document["speed"].GetInt();
        fuel_ = document["fuel"].GetInt();
      }
    }
    else if(method == "POST")
    {
      
    }    
  }

  boost::asio::ssl::stream<tcp::socket> socket_;
  chat_room& room_;
  chat_message read_msg_;
  message_queue write_msgs_;

  //zadnji podaci koje je auto poslao
  static int speed_;
  static int fuel_;


};

//Alociranje memorije za statičke varijable
int chat_session::speed_ = 0;
int chat_session::fuel_ = 0;
//----------------------------------------------------------------------



class chat_server
{
public:
  chat_server(boost::asio::io_context& io_context,
      const tcp::endpoint& endpoint,
      boost::asio::ssl::context& ctx)
    : acceptor_(io_context, endpoint), ctx_(boost::asio::ssl::context::sslv23)
  {
    // Load the certificate and private key files.
    ctx_.use_certificate_chain_file("server.crt");
    ctx_.use_private_key_file("server.key", boost::asio::ssl::context::pem);
    ctx.load_verify_file("ca.crt");
    ctx.set_verify_mode(boost::asio::ssl::verify_peer);
    ctx.set_verify_depth(1);

    do_accept();
  }

private:
  void do_accept()
  {
    acceptor_.async_accept(
        [this](boost::system::error_code ec, tcp::socket socket)
        {
          if (!ec)
          {
            std::make_shared<chat_session>(
              std::move(socket), ctx_, room_
            )->start();
          }

          do_accept();
        });
  }

  tcp::acceptor acceptor_;
  boost::asio::ssl::context ctx_;
  chat_room room_;

};

//----------------------------------------------------------------------

int main()
{ 
  int port = 8080;
  
  try
  {
    boost::asio::io_context io_context;

    boost::asio::ssl::context ctx(boost::asio::ssl::context::sslv23);

    std::list<chat_server> servers;
    tcp::endpoint endpoint(tcp::v4(), port);
    servers.emplace_back(io_context, endpoint, ctx);

    io_context.run();
  }
  catch (std::exception& e)
  {
    std::cerr << "Exception: " << e.what() << std::endl;
  }

  return 0;
}