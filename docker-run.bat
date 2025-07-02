@echo off

echo 🔨 Construyendo la imagen Docker...
docker build -t mainter-backend .

if %errorlevel% equ 0 (
    echo ✅ Imagen construida exitosamente
    echo 🚀 Ejecutando la aplicación...
    
    REM Ejecutar el contenedor
    docker run -d --name mainter-backend-container -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod mainter-backend
    
    if %errorlevel% equ 0 (
        echo ✅ Aplicación ejecutándose en http://localhost:8080/mrp
        echo 📊 Health check: http://localhost:8080/mrp/actuator/health
        echo.
        echo Para detener la aplicación:
        echo docker stop mainter-backend-container
        echo docker rm mainter-backend-container
    ) else (
        echo ❌ Error al ejecutar el contenedor
    )
) else (
    echo ❌ Error al construir la imagen
)
