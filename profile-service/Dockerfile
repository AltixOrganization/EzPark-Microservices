FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn -pl profile-service -am clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y curl --no-install-recommends && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/profile-service/target/*.jar app.jar

EXPOSE 8082

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]