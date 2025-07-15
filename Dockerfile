# Stage 1: Build the application (if not already built) or just copy the JAR
# Using a JDK image for this stage, even if just copying, is good practice
# as it provides a consistent environment.
FROM openjdk:17-jdk-slim AS builder

# Set the working directory inside the container
WORKDIR /app

# For debugging: List contents of the current directory to verify build context
# This helps confirm if 'build/libs/demo-0.0.1-SNAPSHOT.jar' is accessible.
RUN ls -l

# Copy the pre-built JAR file into the container
# IMPORTANT: The source path 'build/libs/demo-0.0.1-SNAPSHOT.jar' is relative
# to the Docker build context (the directory where you run 'docker build').
# Ensure this file exists at this path relative to your build command.
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Stage 2: Create the final, smaller runtime image
# Using a JRE-only image for a smaller attack surface and image size
# Changed to eclipse-temurin:17-jre-jammy as openjdk:17-jre-slim was not found.
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/app.jar .

# Expose the port that the Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Define the entry point for the application
# This command will run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

