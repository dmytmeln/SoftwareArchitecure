FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/config-server-0.0.1-SNAPSHOT.jar /app/config-server.jar
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
