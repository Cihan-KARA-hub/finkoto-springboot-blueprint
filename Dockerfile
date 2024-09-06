# FROM maven:3.8.2-jdk-8 # for Java 8
FROM maven:3.8.5-openjdk-17
ARG JAR_FILE=target/*.jar
WORKDIR /blueprint
COPY . .
#RUN mvn clean install
CMD mvn spring-boot:run

########################################
FROM quay.io/keycloak/keycloak:latest as builder

# saglık durumu incelemesi , ve perfprmans metriklerini incelememizi mümkün kılar
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure a database vendor
ENV KC_DB=postgres


WORKDIR /opt/keycloak
# güvenli bir keystore oluşturur TLS/SSL kullanılır (sertifiksı imzalıdır)
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
RUN /opt/keycloak/bin/kc.sh build

#yapılamdırma ve çalışmaya hazır
FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# change these values to point to a running postgres instance
ENV KC_DB=postgres
ENV KC_DB_URL=<dbc:postgresql://localhost:5432/postgres>
ENV KC_DB_USERNAME=<postgret>
ENV KC_DB_PASSWORD=<123456>
ENV KC_HOSTNAME=localhost
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]