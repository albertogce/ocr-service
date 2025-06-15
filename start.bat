@echo off
setlocal enabledelayedexpansion
set JAVA_HOME=C:\Program Files\Java\jdk-23
set PATH=%JAVA_HOME%\bin;%PATH%


echo Levantando servicios base con docker-compose (redpanda, postgres, nexus, etc)...

:: Levanta los servicios iniciales en segundo plano
docker-compose up -d redpanda redpanda-console postgres prometheus grafana nexus maven-uploader

echo Esperando que Nexus y servicios estén listos...

:wait_nexus
curl -s http://localhost:8081 > nul
if errorlevel 1 (
    echo Esperando Nexus...
    timeout /t 5 > nul
    goto wait_nexus
)

echo Compilando localmente OCR service usando mvnw.cmd...

cd services\ocr-service

call mvnw.cmd clean package -Dspring.profiles.active=docker -DskipTests

cd ..\..

echo Levantando tesseract-service...

docker-compose up -d tesseract-service

echo Todo listo.

endlocal
pause
