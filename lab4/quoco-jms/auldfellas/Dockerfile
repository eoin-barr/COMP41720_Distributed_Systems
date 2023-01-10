FROM openjdk:8-jre-alpine

ENV SERVER_HOST localhost

COPY target/auldfellas-*-jar-with-dependencies.jar /auldfellas.jar

CMD sleep 7; java -cp /auldfellas.jar service.Receiver ${SERVER_HOST}