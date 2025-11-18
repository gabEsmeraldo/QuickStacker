# Use official Maven image with Java 21
FROM maven:3.9-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY back/pom.xml ./back/

# Download dependencies (this layer will be cached if pom.xml doesn't change)
WORKDIR /app/back
RUN mvn dependency:go-offline -B

# Copy source code
COPY back/src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/back/target/back-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Railway will set PORT env var)
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

