#include <iostream>
#include <boost/asio.hpp>
#include "chat_message.hpp"
#include <boost/asio/ssl.hpp>


using namespace boost::asio;

class client
{
public:
  client(boost::asio::io_context& io_context,
      boost::asio::ssl::context& context_,
      const ip::tcp::resolver::results_type&server_endpoints)
    : io_context_(io_context), socket_(io_context, context_)
  {
    do_connect(server_endpoints);
  }

  void close()
  {
    boost::asio::post(io_context_, [this]() { socket_.lowest_layer().close(); });
  }

  void write(const chat_message& msg){
    write_msg_ = msg;
}
  


private:
  void do_connect(const ip::tcp::resolver::results_type& endpoints)
  {
    
    boost::asio::async_connect(socket_.lowest_layer(), endpoints,
        [this](boost::system::error_code ec, ip::tcp::endpoint)
        {
          if (!ec)
          {
            // Perform the SSL handshake.
            socket_.handshake(boost::asio::ssl::stream_base::client);
            do_write();
            do_read_header();
          }
        });
  }

  void do_read_header()
  {
    boost::asio::async_read(socket_,
        boost::asio::buffer(read_msg_.data(), chat_message::header_length),
        [this](boost::system::error_code ec, std::size_t /*length*/)
        {
          if (!ec && read_msg_.decode_header())
          {
            do_read_body();
          }
          else
          {
            socket_.lowest_layer().close();
          }
        });
  }

  void do_read_body()
  {
    boost::asio::async_read(socket_,
        boost::asio::buffer(read_msg_.body(), read_msg_.body_length()),
        [this](boost::system::error_code ec, std::size_t /*length*/)
        {
          if (!ec)
          { 
            std::cout.write(read_msg_.body(), read_msg_.body_length());
            std::cout << "\n";
            do_read_header();
            close();
          }
          else
          {
            socket_.lowest_layer().close();
          }
        });
  }

  void do_write()
  {
    boost::asio::async_write(socket_,
        boost::asio::buffer(write_msg_.data(),
          write_msg_.length()),
        [this](boost::system::error_code ec, std::size_t /*length*/)
        {
          if (!ec)
          {

          }
          else
          {
            socket_.lowest_layer().close();
          }
        });

    
  }



  boost::asio::ssl::stream<ip::tcp::socket> socket_;
  boost::asio::io_context& io_context_;
  chat_message read_msg_;
  chat_message write_msg_;

};


int main(){
  int port = 8080;

  try{
    boost::asio::io_context io_context;

    boost::asio::ssl::context ctx(boost::asio::ssl::context::sslv23);
    ctx.use_certificate_chain_file("client.crt");
    ctx.use_private_key_file("client.key", boost::asio::ssl::context::pem);
    ctx.load_verify_file("ca.crt");

    ip::tcp::resolver resolver(io_context);
    auto endpoints = resolver.resolve("127.0.0.1", std::to_string(port));
    
    client c(io_context, ctx, endpoints);

    std::thread t([&io_context](){ io_context.run(); });


    
    const char* request = "GET / HTTP/1.1\r\nContent-Type: application/json\r\n\r\n";
    chat_message msg;
    msg.body_length(std::strlen(request));
    std::memcpy(msg.body(), request, msg.body_length());
    msg.encode_header();
    
    c.write(msg);
    t.join();
  }
  catch (std::exception& e)
  {
    std::cerr << "Exception: " << e.what() << std::endl;
  }

  return 0;
}
