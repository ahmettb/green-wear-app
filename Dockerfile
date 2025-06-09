FROM openjdk:17-jdk-slim

WORKDIR /app

COPY product .

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

COPY --from=0 /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]