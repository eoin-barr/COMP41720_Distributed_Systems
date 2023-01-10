FROM openjdk:8-jre-alpine

ENV SERVER_HOST localhost

COPY target/girlpower-*-jar-with-dependencies.jar /girlpower.jar

CMD sleep 7; java -cp /girlpower.jar service.Receiver ${SERVER_HOST}