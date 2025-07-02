#!/bin/bash

# Script para construir y ejecutar la aplicación con Docker

echo "🔨 Construyendo la imagen Docker..."
docker build -t mainter-backend .

if [ $? -eq 0 ]; then
    echo "✅ Imagen construida exitosamente"
    echo "🚀 Ejecutando la aplicación..."
    
    # Ejecutar el contenedor
    docker run -d \
        --name mainter-backend-container \
        -p 8081:8081 \
        -e SPRING_PROFILES_ACTIVE=prod \
        mainter-backend
    
    if [ $? -eq 0 ]; then
        echo "✅ Aplicación ejecutándose en http://localhost:8081/mrp"
        echo "📊 Health check: http://localhost:8081/mrp/actuator/health"
        echo ""
        echo "Para detener la aplicación:"
        echo "docker stop mainter-backend-container"
        echo "docker rm mainter-backend-container"
    else
        echo "❌ Error al ejecutar el contenedor"
    fi
else
    echo "❌ Error al construir la imagen"
fi
