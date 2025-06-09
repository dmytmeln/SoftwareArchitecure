FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/discovery-server-0.0.1-SNAPSHOT.jar /app/discovery-server.jar
ENTRYPOINT ["java", "-jar", "/app/discovery-server.jar"]
