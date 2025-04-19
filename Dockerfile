# Build argument for Java version
ARG JAVA_VERSION=17

# Stage 1: Build the application
FROM maven:3.9.9 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Run the application
FROM openjdk:${JAVA_VERSION}-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/anuncios-service.jar app.jar
EXPOSE 8585
ENTRYPOINT ["java", "-jar", "app.jar"]