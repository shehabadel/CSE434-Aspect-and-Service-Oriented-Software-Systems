# Build Stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy jar to runtime image
COPY --from=build /app/target/*.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
