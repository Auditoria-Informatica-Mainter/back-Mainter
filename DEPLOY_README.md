# Despliegue en Render - Mainter Backend

Este documento explica cómo deployar la aplicación Spring Boot en Render usando Docker.

## 📋 Archivos de Configuración

- `Dockerfile` - Configuración multi-stage para construir la imagen
- `.dockerignore` - Archivos a ignorar durante el build
- `render.yaml` - Configuración específica para Render
- `application-prod.properties` - Configuración de producción
- `docker-run.sh` / `docker-run.bat` - Scripts para testing local

## 🚀 Pasos para Deployar en Render

### 1. Preparar el Repositorio

1. Asegúrate de que todos los archivos estén en tu repositorio Git
2. Haz commit y push de los nuevos archivos:
   ```bash
   git add .
   git commit -m "Add Docker configuration for Render deployment"
   git push origin main
   ```

### 2. Crear el Servicio en Render

1. Ve a [Render Dashboard](https://dashboard.render.com/)
2. Haz clic en "New +" → "Web Service"
3. Conecta tu repositorio de GitHub/GitLab
4. Selecciona tu repositorio

### 3. Configurar el Servicio

En la página de configuración:

- **Name**: `mainter-backend` (o el nombre que prefieras)
- **Environment**: `Docker`
- **Region**: Selecciona la más cercana a tus usuarios
- **Branch**: `main` (o tu rama principal)
- **Dockerfile Path**: `./Dockerfile`

### 4. Variables de Entorno

Agrega estas variables de entorno en Render:

#### Base de Datos (PostgreSQL)
```
DATABASE_URL=postgresql://username:password@hostname:port/database
```
> Render proporciona esto automáticamente si creas una base de datos PostgreSQL

#### Seguridad
```
JWT_SECRET=tu-clave-secreta-super-segura-aqui
SPRING_PROFILES_ACTIVE=prod
```

#### CORS (Frontend)
```
ALLOWED_ORIGINS=https://tu-frontend-domain.com
```

#### Stripe (si usas pagos)
```
STRIPE_SECRET_KEY=sk_live_tu_clave_secreta
STRIPE_PUBLISHABLE_KEY=pk_live_tu_clave_publica
```

### 5. Configurar Base de Datos PostgreSQL

1. En Render Dashboard, crea un nuevo "PostgreSQL" service
2. Copia la "External Database URL"
3. Pégala como variable `DATABASE_URL` en tu web service

## 🔧 Testing Local con Docker

### Windows:
```bash
./docker-run.bat
```

### Linux/Mac:
```bash
chmod +x docker-run.sh
./docker-run.sh
```

### Comandos Docker Manuales:

```bash
# Construir la imagen
docker build -t mainter-backend .

# Ejecutar el contenedor
docker run -d \
  --name mainter-backend-container \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=postgresql://localhost:5432/mrp_db \
  mainter-backend

# Ver logs
docker logs mainter-backend-container

# Detener y limpiar
docker stop mainter-backend-container
docker rm mainter-backend-container
```

## 📊 Endpoints Importantes

- **Health Check**: `https://tu-app.onrender.com/mrp/actuator/health`
- **API Base**: `https://tu-app.onrender.com/mrp/`
- **Swagger** (solo en desarrollo): `http://localhost:8080/mrp/swagger-ui.html`

## 🐛 Troubleshooting

### Error de Conexión a Base de Datos
- Verifica que `DATABASE_URL` esté correctamente configurada
- Asegúrate de que la base de datos PostgreSQL esté running

### Error de Memoria
- Ajusta `JAVA_OPTS` en el Dockerfile: `-Xmx1g -Xms512m`
- Considera upgradeear el plan de Render

### Error de CORS
- Configura `ALLOWED_ORIGINS` con el dominio de tu frontend
- Verifica la configuración CORS en tu código

### Logs de la Aplicación
```bash
# En Render Dashboard, ve a tu servicio y haz clic en "Logs"
# O usa curl para health check:
curl https://tu-app.onrender.com/mrp/actuator/health
```

## 📝 Notas Importantes

1. **Puerto**: Render asigna automáticamente el puerto, el Dockerfile está configurado para usar `PORT` environment variable
2. **SSL**: Render proporciona HTTPS automáticamente
3. **Domain**: Render te da un dominio gratuito, pero puedes usar tu propio dominio
4. **Free Tier**: Los servicios gratuitos se "duermen" después de 15 minutos de inactividad

## 🔄 Auto-Deploy

El archivo `render.yaml` está configurado para auto-deploy cuando hagas push a la rama main. Puedes deshabilitarlo en la configuración del servicio si prefieres deployar manualmente.
