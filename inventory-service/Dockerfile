FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/inventory-service-0.0.1-SNAPSHOT.jar /app/inventory-service.jar
ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]
