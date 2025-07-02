FROM eclipse-temurin:17-jdk-jammy

# Instalar herramientas necesarias para certificados
RUN apt-get update && apt-get install -y \
    ca-certificates \
    openssl \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Actualizar certificados del sistema
RUN update-ca-certificates

# Descargar e instalar certificados de Gmail
RUN echo "Instalando certificados de Gmail..." && \
    # Obtener certificados de Gmail SMTP
    echo -n | openssl s_client -connect smtp.gmail.com:587 -starttls smtp -servername smtp.gmail.com -showcerts 2>/dev/null | \
    sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > /tmp/gmail-certs.pem && \
    # Dividir certificados individuales
    awk 'BEGIN {c=0;} /BEGIN CERTIFICATE/{c++} { print > "/tmp/gmail-cert-" c ".pem"}' /tmp/gmail-certs.pem && \
    # Importar cada certificado al keystore de Java
    for cert in /tmp/gmail-cert-*.pem; do \
        if [ -s "$cert" ]; then \
            cert_name=$(basename "$cert" .pem); \
            echo "Importando certificado: $cert_name"; \
            keytool -import -alias "$cert_name" -file "$cert" \
                -keystore "$JAVA_HOME/lib/security/cacerts" \
                -storepass changeit -noprompt -trustcacerts || true; \
        fi; \
    done && \
    # Limpiar archivos temporales
    rm -f /tmp/gmail-cert*.pem && \
    echo "Certificados de Gmail instalados correctamente"

# Verificar que los certificados se instalaron
RUN echo "Verificando certificados instalados:" && \
    keytool -list -keystore "$JAVA_HOME/lib/security/cacerts" -storepass changeit | grep gmail || \
    echo "Advertencia: No se encontraron certificados de Gmail en el keystore"

# Configurar directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación
COPY target/notification-service-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8088

# Configurar JVM para mejor manejo de SSL
ENTRYPOINT ["java", \
    "-Djavax.net.ssl.trustStore=$JAVA_HOME/lib/security/cacerts", \
    "-Djavax.net.ssl.trustStorePassword=changeit", \
    "-Djavax.net.ssl.trustStoreType=JKS", \
    "-Dcom.sun.net.ssl.checkRevocation=false", \
    "-jar", "app.jar"]