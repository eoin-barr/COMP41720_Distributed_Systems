FROM openjdk:8-jre-alpine

ENV BROKER_URLS http://localhost:8080

COPY target/broker-1.0.jar  /broker.jar

CMD java -jar /broker.jar service.broker.Application 