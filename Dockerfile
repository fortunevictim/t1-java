# Dockerfile
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

COPY client-ms/pom.xml client-ms/
COPY account-ms/pom.xml account-ms/
COPY credit-ms/pom.xml credit-ms/


RUN --mount=type=cache,target=/root/.m2 mvn -q dependency:go-offline \
    -pl client-ms,account-ms,credit-ms -am


COPY . .


RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests \
    -pl client-ms,account-ms,credit-ms -am clean package


# === 
FROM eclipse-temurin:21-jre AS client-ms
WORKDIR /app
COPY --from=builder /app/client-ms/target/client-ms-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre AS account-ms
WORKDIR /app
COPY --from=builder /app/account-ms/target/account-ms-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre AS credit-ms
WORKDIR /app
COPY --from=builder /app/credit-ms/target/credit-ms-*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]