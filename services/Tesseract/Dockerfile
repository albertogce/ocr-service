FROM openjdk:21-slim

# Crea el directorio de trabajo
WORKDIR /app

# # Instala Maven, Tesseract, español y utilidades básicas
RUN apt-get update && \
    apt-get install -y tesseract-ocr tesseract-ocr-spa git && \
    apt-get clean

# Copia solo el JAR generado previamente
COPY services/ocr-service/target/ocr-service-0.0.1-SNAPSHOT.jar app.jar

# Comando por defecto
CMD ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]