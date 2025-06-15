#!/bin/bash
set -e

echo "🕐 Esperando a que Nexus esté listo..."
until curl -s http://nexus:8081 | grep -q "nexus"; do
  sleep 10
done

export NEXUS_PASSWORD="admin123"
echo "📤 Usando credenciales -> admin:$NEXUS_PASSWORD"

echo "📝 Generando settings.xml en /root/.m2/settings.xml..."
mkdir -p /root/.m2

cat <<EOF > /root/.m2/settings.xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>internal</id>
      <username>admin</username>
      <password>${NEXUS_PASSWORD}</password>
    </server>
  </servers>
</settings>
EOF

# Normalizar saltos de línea
sed -i 's/\r//' /scripts/grant-deploy-admin.groovy

# Crear el script vía API
echo "📤 Subiendo script Groovy..."

SCRIPT_CONTENT=$(sed ':a;N;$!ba;s/\n/\\n/g' /scripts/grant-deploy-admin.groovy | sed 's/"/\\"/g')

cat <<EOF > /tmp/script.json
{
  "name": "grant-deploy-admin",
  "type": "groovy",
  "content": "$SCRIPT_CONTENT"
}
EOF

curl -u admin:$NEXUS_PASSWORD --header "Content-Type: application/json" \
  --data @/tmp/script.json \
  http://nexus:8081/service/rest/v1/script || echo "⚠️  Script ya existente, ignorando error..."

echo "🚀 Ejecutando script Groovy..."
curl -u admin:$NEXUS_PASSWORD -X POST http://nexus:8081/service/rest/v1/script/grant-deploy-admin/run || true

echo "📦 Compilando y subiendo common-data..."
cd /workspace/common-data
mvn clean deploy -DaltDeploymentRepository=internal::default::http://nexus:8081/repository/maven-snapshots
