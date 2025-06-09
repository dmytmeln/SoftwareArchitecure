FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]
