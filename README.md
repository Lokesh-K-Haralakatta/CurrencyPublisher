# CurrencyPublisher
Standalone Springboot Java application to retrieve and publish various currency to Kafka Topics.

The main goal of this application is to retrieve various currency values starting from date 1999-01-04 to latest
and keep publishing to configured Kafka Brokers for every 1 Second on various Kafka topics configured based on currency name.

The various currnecy values are retrieved from public api available at [Foreign exchange rates API](http://exchangeratesapi.io/)

# Prerquisites
- Java 8
- Apache Maven
- Apache Kafka
