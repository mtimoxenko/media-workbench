# Use a base image with Java 20
FROM openjdk:20-jdk-slim

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory in the Docker container
WORKDIR /app

# Copy the Maven pom.xml file and source code
COPY pom.xml /app
COPY src /app/src

# Build the application using Maven
RUN mvn clean package

# Indicate that the container listens on port 8080 at runtime
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "target/media-api-0.0.1-SNAPSHOT.jar"]
