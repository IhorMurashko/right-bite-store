FROM maven:3.8.6-amazoncorretto-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -Pprod -DskipTests -Dmaven.test.skip=true

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]