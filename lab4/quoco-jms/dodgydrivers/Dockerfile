FROM openjdk:8-jre-alpine

ENV SERVER_HOST localhost

COPY target/dodgydrivers-*-jar-with-dependencies.jar /dodgydrivers.jar

CMD sleep 7; java -cp /dodgydrivers.jar service.Receiver ${SERVER_HOST}