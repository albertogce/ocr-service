FROM openjdk:21-slim

# Crea el directorio de trabajo
WORKDIR /app

# Copia solo el JAR generado previamente
COPY services/grpc-uploader-service/target/grpc-uploader-service-0.0.1-SNAPSHOT.jar app.jar

# Comando por defecto
CMD ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]