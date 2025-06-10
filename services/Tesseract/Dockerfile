FROM openjdk:21-slim

# Instala dependencias necesarias: Maven, Tesseract y español
RUN apt-get update && \
    apt-get install -y maven tesseract-ocr tesseract-ocr-spa git && \
    apt-get clean

# Crea directorio de trabajo
WORKDIR /app

# Copia el código fuente del microservicio OCR
COPY services/ocr-service /app

# Establecemos el perfil de Spring a 'docker'
ENV SPRING_PROFILES_ACTIVE=docker

# Compila el proyecto con Maven (solo el microservicio ocr-service)
RUN mvn clean package -DskipTests

# Ejecuta el JAR generado (asumiendo estructura estándar de Spring Boot)
CMD ["java", "-jar", "target/ocr-service-0.0.1-SNAPSHOT.jar"]
