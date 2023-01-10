FROM openjdk:8-jre-alpine

COPY target/client-*-jar-with-dependencies.jar /client.jar

CMD java -Duser.language=en -Duser.country=IE -jar /client.jar -h ${SERVER_HOST} 
