# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Създай uploads директория в работната директория
RUN mkdir -p uploads

# Копирай JAR файла
COPY --from=build /app/target/*.jar app.jar

# Използвай root потребител за сега (за тестване)
# USER root

# Или създай потребител и дай права
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown -R spring:spring /app
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]