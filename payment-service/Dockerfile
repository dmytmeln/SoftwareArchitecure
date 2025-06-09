FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/payment-service-0.0.1-SNAPSHOT.jar /app/payment-service.jar
ENTRYPOINT ["java", "-jar", "/app/payment-service.jar"]
