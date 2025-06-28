FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/review-service-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y curl --no-install-recommends && rm -rf /var/lib/apt/lists/*

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]