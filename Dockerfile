# Stage 1: Build the application
FROM gradle:8.10.0-jdk22-alpine AS builder
WORKDIR /home/app

# Copy the entire project to the container
COPY . .

# Use the Gradle wrapper to build the project
RUN gradle clean build

# Stage 2: Run the application
FROM openjdk:22-slim AS my-app
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /home/app/build/libs/*.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
