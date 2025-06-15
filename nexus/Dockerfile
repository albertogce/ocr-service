FROM sonatype/nexus3:3.68.0

ENV NEXUS_SECURITY_RANDOMPASSWORD false

# Copiar scripts de inicialización
COPY setup /opt/sonatype/nexus/setup

# Reemplazar el archivo vmoptions completo
COPY nexus.vmoptions /opt/sonatype/nexus/etc/nexus.vmoptions