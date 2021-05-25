FROM amazoncorretto:8
LABEL maintainer="sr.funk.sensei@gmail.com"
COPY target/ognio-conquer-game-0.0.1-SNAPSHOT.jar ognio-conquer-game.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/ognio-conquer-game.jar"]