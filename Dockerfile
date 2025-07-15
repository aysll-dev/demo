#FROM openjdk:17-jdk-slim
#COPY build/libs/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
# --- Stage 1: Build the application ---
# Use a full JDK image to compile the Java code
FROM openjdk:17-jdk as builder

# Set the working directory inside the container
WORKDIR /workspace/app

# Copy the Gradle wrapper and build files to leverage Docker layer caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy the application source code
COPY src src

# Build the application, creating the .jar file.
# The --no-daemon flag is recommended for containerized builds.
RUN ./gradlew build --no-daemon


# --- Stage 2: Create the final, lightweight image ---
# Use a slim image that only contains the Java Runtime Environment
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy only the built .jar file from the 'builder' stage
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

# Expose port 8080 (the default for many web applications like Spring Boot)
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]