FROM openjdk:8-jre-alpine

ENV SERVER_HOST localhost

COPY target/broker-*-jar-with-dependencies.jar /broker.jar

CMD sleep 7; java -cp /broker.jar service.Broker -h ${SERVER_HOST}
