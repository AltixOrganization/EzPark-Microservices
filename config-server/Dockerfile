# Usa una imagen base de OpenJDK (Java 17) para tus aplicaciones Spring Boot
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/config-server-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y curl --no-install-recommends && rm -rf /var/lib/apt/lists/*

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]