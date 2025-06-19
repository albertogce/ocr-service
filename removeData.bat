echo Eliminando todas las imágenes, volumenes y conexiones de Docker existentes...

docker image prune -a -f
docker network prune -f
docker volume prune -a -f