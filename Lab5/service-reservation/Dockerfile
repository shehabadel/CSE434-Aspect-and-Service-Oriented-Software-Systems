FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy POM first to leverage Docker layer caching
COPY pom.xml .
# Download dependencies separately to improve build caching
RUN mvn dependency:go-offline -B

# Copy source code and build the application
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=5000"] 