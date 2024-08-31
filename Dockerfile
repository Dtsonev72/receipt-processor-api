# Use an official openjdk image as a parent image
FROM openjdk:22

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Build the application
RUN ./gradlew clean build

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "build/libs/receipt-processor-1.0-SNAPSHOT.jar"]
