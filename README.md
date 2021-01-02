# CurrencyRatesPublisher
Standalone Springboot Java application to retrieve and publish various currency rates to Kafka Topics.

The main goal of this application module is to retrieve various currency values starting from given date to latest date
and keep publishing to configured Kafka Brokers for every 1 Second on various Kafka topics configured based on currency name.

The various currnecy values are retrieved from public api available at [Foreign exchange rates API](http://exchangeratesapi.io/)

# Prerequisites
- Linux / Ubuntu
- Java 8
- Springboot
- Spring Kafka
- Apache Maven
- Apache Kafka

# Flow
CurrencyPublisher application is designed to have a threads pool containing separate thread for retrieveing curreny rates for
each of the currency for every 1 second from [Foreign exchange rates API](http://exchangeratesapi.io/) and publish to respective
Kafka topic for further processing from Kafka Event Streaming Module. Below diagram represents pictorial representation of the flow:

![Currency-Publisher-Flow](https://github.com/Lokesh-K-Haralakatta/CurrencyRatesPublisher/blob/develop/currency-rates-publisher-flow.png)

# Build Steps
- Get Source Code either using Git Clone or Downliading ZIP followed by extract to `CurrencyRatesPublisher` directory
- Go to root directory - `cd CurrencyRatesPublisher` 
- Update configurations in src/main/resources/application.properties
  - Kafka Broker listener port
  - Start Date to retrieve and publish currency rates
- Build JAR Package - `mvn clean package`
- After successful compilation, we should get Jar file `CurrencyRatesPublisher-0.0.1-SNAPSHOT.jar` in target directory

# Execution Steps
- Make sure Kafka Broker(s) setup and listening at configured URL in application.properties
- Go to target direcory - `cd CurrencyRatesPublisher/target`
- To run with bundled configurations - `java -jar CurrencyRatesPublisher-0.0.1-SNAPSHOT.jar`
- To run with custom configurations - `java -jar CurrencyRatesPublisher-0.0.1-SNAPSHOT.jar --spring.config.location=path-to-application.properties-file`
