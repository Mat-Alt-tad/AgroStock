FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY settings.gradle build.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon
COPY AuthService AuthService
RUN ./gradlew :AuthService:bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/AuthService/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
