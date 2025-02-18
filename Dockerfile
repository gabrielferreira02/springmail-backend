#FROM openjdk:21-jdk-slim
#LABEL authors="gabriel"
#
#WORKDIR /app
#
#COPY /target/springmail-0.0.1-SNAPSHOT.jar app.jar
#
#ENTRYPOINT ["java", "-jar", "app.jar"]
# Estágio de build: usa Maven para compilar o projeto e gerar o JAR
FROM maven:3.8.6-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio final: copia apenas o JAR gerado e executa a aplicação
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/springmail-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
