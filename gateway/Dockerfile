FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar
ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]
