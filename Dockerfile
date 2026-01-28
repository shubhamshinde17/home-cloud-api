# ---- Build stage ----
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN ./gradlew --no-daemon dependencies
COPY src src
RUN ./gradlew --no-daemon clean bootJar

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
