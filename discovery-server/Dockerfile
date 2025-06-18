# Usa una imagen base de OpenJDK (Java 17)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor
# Asume que el JAR se llamará "discovery-server-0.0.1-SNAPSHOT.jar"
# Reemplaza con el nombre real de tu JAR si es diferente
COPY target/discovery-server-0.0.1-SNAPSHOT.jar app.jar


RUN apt-get update && apt-get install -y curl --no-install-recommends && rm -rf /var/lib/apt/lists/*


# Expone el puerto en el que la aplicación Eureka Server se ejecutará (el puerto 8761)
EXPOSE 8761

# Comando para ejecutar la aplicación JAR cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]