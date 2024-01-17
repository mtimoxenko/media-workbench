# Stage 1: Build the application using the existing image
FROM openjdk:20-jdk-slim as build

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory in the Docker container
WORKDIR /app

# Copy the Maven pom.xml file and source code
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package

# Stage 2: Create the runtime image using a slim JRE base image
FROM eclipse-temurin:20-jre-ubi9-minimal

# Set the working directory in the Docker container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/media-api-0.0.1-SNAPSHOT.jar .

# Indicate that the container listens on port 8080 at runtime
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "media-api-0.0.1-SNAPSHOT.jar"]
