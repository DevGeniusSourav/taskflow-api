FROM eclipse-temurin:17-jdk-jammy
LABEL authors="sourav"

COPY target/TaskFlow-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]