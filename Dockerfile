FROM openjdk:21-jdk-slim
LABEL authors="gabriel"

WORKDIR /app

COPY /target/springmail-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]