FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src src

COPY mvnw .
COPY .mvn .mvn

RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim
VOLUME /tmp

 COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT [ "java" , "-jar" , "/app.jar" ]
EXPOSE 8080
