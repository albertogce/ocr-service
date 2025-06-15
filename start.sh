#!/bin/bash

set -e  # Salir si algún comando falla

echo "🚀 Levantando servicios base con docker-compose (redpanda, postgres, nexus, etc)..."

# Levanta los servicios iniciales en segundo plano
docker-compose up -d redpanda redpanda-console postgres prometheus grafana nexus maven-uploader

echo "⌛ Esperando que Nexus y servicios estén listos..."
until curl -s http://nexus:8081 | grep -q "nexus"; do
  sleep 5
done

echo "🔨 Compilando localmente OCR service..."

cd services/ocr-service

mvn clean package

cd ../../

echo "🚀 Levantando ocr-service..."

docker-compose up -d tesseract-service

echo "✅ Todo listo."