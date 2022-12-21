#include <iostream>
#include <boost/asio.hpp>
#include "chat_message.hpp"
#include <boost/asio/ssl.hpp>
#include <random>
#include "rapidjson/writer.h"
#include "rapidjson/stringbuffer.h"


using namespace boost::asio;

class Car {
private:
    std::mt19937 generator;
    std::uniform_int_distribution<int> speedDistribution;
    std::uniform_int_distribution<int> fuelDistribution;
    std::uniform_int_distribution<int> locationDistribution;

public:
    Car() : generator(std::random_device{}()) {}

    // Set the range for the random speed values
    void setSpeedRange(int minSpeed, int maxSpeed) {
        speedDistribution = std::uniform_int_distribution<int>(minSpeed, maxSpeed);
    }

    // Set the range for the random fuel values
    void setFuelRange(int minFuel, int maxFuel) {
        fuelDistribution = std::uniform_int_distribution<int>(minFuel, maxFuel);
    }

    // Generate and return a random speed value
    int getSpeed() {
        return speedDistribution(generator);
    }

    // Generate and return a random fuel value
    int getFuel() {
        return fuelDistribution(generator);
    }
};

class client
{
public:
  client(boost::asio::io_context& io_context,
      boost::asio::ssl::context& context_,
      const ip::tcp::resolver::results_type&server_endpoints,
      Car car)
    : io_context_(io_context), socket_(io_context, context_), car_(car)
  {
    do_connect(server_endpoints);
  }

  void close()
  {
    //boost::asio::post(io_context_, [this]() { socket_.shutdown(); });
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
            generate_request();
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
            do_read_header();
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
            generate_request();
          }
          else
          {
            socket_.lowest_layer().close();
          }
        });

    
  }

  void generate_request(){
    const char* request_header = "PUT / HTTP/1.1\r\nContent-Type: application/json\r\n\r\n";
    rapidjson::StringBuffer sb;
    rapidjson::Writer<rapidjson::StringBuffer> writer(sb);

    writer.StartObject();
    writer.Key("speed");
    writer.Int(car_.getSpeed());
    writer.Key("fuel");
    writer.Int(car_.getFuel());
    writer.EndObject();

    const std::string json_str = sb.GetString();

    std::string request = request_header + json_str;
    std::cout << request << std::endl;
    write_msg_.body_length(std::strlen(request.c_str()));
    std::memcpy(write_msg_.body(), request.c_str(), write_msg_.body_length());
    write_msg_.encode_header();

    
    do_write();
    sleep(1);
  }



  boost::asio::ssl::stream<ip::tcp::socket> socket_;
  boost::asio::io_context& io_context_;
  chat_message read_msg_;
  chat_message write_msg_;
  Car car_;

};


int main(){

  Car car;
  car.setSpeedRange(0, 200);
  car.setFuelRange(0, 100);


  //std::string_view ip = "127.0.0.1";
  int port = 8080;

  try{
    boost::asio::io_context io_context;

    boost::asio::ssl::context ctx(boost::asio::ssl::context::sslv23);
    ctx.use_certificate_chain_file("client.crt");
    ctx.use_private_key_file("client.key", boost::asio::ssl::context::pem);
    ctx.load_verify_file("ca.crt");

    ip::tcp::resolver resolver(io_context);
    auto endpoints = resolver.resolve("127.0.0.1", std::to_string(port));
    
    client c(io_context, ctx, endpoints, car);

    std::thread t([&io_context](){ io_context.run(); });



    c.close();
    t.join();
  }
  catch (std::exception& e)
  {
    std::cerr << "Exception: " << e.what() << std::endl;
  }

  return 0;
}
