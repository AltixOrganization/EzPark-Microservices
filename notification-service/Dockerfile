FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn -pl notification-service -am clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    ca-certificates \
    openssl \
    curl \
    && rm -rf /var/lib/apt/lists/*

RUN update-ca-certificates

RUN echo "Instalando certificados de Gmail..." && \
    echo -n | openssl s_client -connect smtp.gmail.com:587 -starttls smtp -servername smtp.gmail.com -showcerts 2>/dev/null | \
    sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > /tmp/gmail-certs.pem && \
    awk 'BEGIN {c=0;} /BEGIN CERTIFICATE/{c++} { print > "/tmp/gmail-cert-" c ".pem"}' /tmp/gmail-certs.pem && \
    for cert in /tmp/gmail-cert-*.pem; do \
        if [ -s "$cert" ]; then \
            cert_name=$(basename "$cert" .pem); \
            echo "Importando certificado: $cert_name"; \
            keytool -import -alias "$cert_name" -file "$cert" \
                -keystore "$JAVA_HOME/lib/security/cacerts" \
                -storepass changeit -noprompt -trustcacerts || true; \
        fi; \
    done && \
    rm -f /tmp/gmail-cert*.pem && \
    echo "Certificados de Gmail instalados correctamente"


COPY --from=build /app/notification-service/target/*.jar app.jar

EXPOSE 8088


ENTRYPOINT ["java", \
    "-Djavax.net.ssl.trustStore=$JAVA_HOME/lib/security/cacerts", \
    "-Djavax.net.ssl.trustStorePassword=changeit", \
    "-Djavax.net.ssl.trustStoreType=JKS", \
    "-Dcom.sun.net.ssl.checkRevocation=false", \
    "-jar", "app.jar"]