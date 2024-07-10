FROM openjdk:8-jdk-alpine
LABEL authors="Xieningjun"
COPY app-1.0.0.jar /app.jar
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]