# Use Eclipse Temurin JDK 17 slim image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (this speeds up rebuilds)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the Spring Boot jar
RUN ./mvnw clean package -DskipTests

# Expose port (Spring Boot default is 8080)
EXPOSE 8080

# Command to run the jar
CMD ["java", "-jar", "target/pastebin-lite-0.0.1-SNAPSHOT.jar"]
