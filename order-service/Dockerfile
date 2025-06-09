FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/order-service-0.0.1-SNAPSHOT.jar /app/order-service.jar
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]
