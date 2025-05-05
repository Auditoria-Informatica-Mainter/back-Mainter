# 📘 Documentación de la API - Sistema MRP Carpintería

Esta API forma parte del sistema MRP para una carpintería, y está diseñada para manejar módulos como gestión de usuarios, roles, permisos, inventario, producción, entre otros.

---

## 🚀 Acceso a Swagger UI

La API cuenta con documentación interactiva gracias a Swagger, que permite visualizar y probar los endpoints disponibles.

🔗 **URL de Swagger UI:**

[http://localhost:8081/mrp/swagger-ui/index.html#/](http://localhost:8080/mrp/swagger-ui/index.html#/)

---

## 🛡️ Autenticación y Autorización

### Paso 1: Registrar un usuario administrador (solo si no existe)

Endpoint:  
**POST** `/api/auth/registeradmin`

Body ejemplo:
```json
{
  "nombre": "Admin",
  "apellido": "Principal",
  "telefono": "123456789",
  "email": "admin@mrp.com",
  "password": "admin123"
}
```

---

### Paso 2: Hacer login

Endpoint:  
**POST** `/api/auth/login`

Body:
```json
{
  "email": "admin@mrp.com",
  "password": "admin123"
}
```

Obtendrás un **token JWT** como respuesta. Ejemplo:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### Paso 3: Autorizar en Swagger

1. Haz clic en el botón **"Authorize"** en la parte superior derecha de Swagger UI.
2. Copia el token obtenido y pégalo con el prefijo **Bearer**, así:
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
3. Una vez autorizado, podrás usar todos los endpoints protegidos sin problemas.

---

## 🧪 Pruebas desde Postman

Puedes hacer las pruebas directamente desde Postman también.  
La URL base para tus requests será:

```
http://localhost:8080/mrp/
```

Ejemplos:

- `POST http://localhost:8080/mrp/api/auth/registeradmin`
- `POST http://localhost:8080/mrp/api/auth/login`
- `GET http://localhost:8080/mrp/api/usuarios`

---

## ⚙️ Variables configurables en `application.properties`

Si vas a correr este proyecto en otra máquina o entorno, deberías revisar y cambiar las siguientes variables del archivo `src/main/resources/application.properties`:

```properties
# Puerto del servidor
server.port=8080

# URL de la base de datos (ajustar si es local o remota)
spring.datasource.url=jdbc:postgresql://localhost:5432/mrp

# Credenciales de base de datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Para mostrar Swagger incluso en producción
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

Opcionalmente, puedes externalizar estas variables usando un archivo `.env` o un sistema de perfiles (`application-dev.properties`, etc.).

---

## 💻 Requisitos del Proyecto

- **Spring Boot**: 3.4.4
- **JDK**: 17

---

## 📂 Estructura del Proyecto

- `api/auth`: Login y registro de usuarios
- `api/usuarios`: Gestión de usuarios
- `api/roles`: Gestión de roles y permisos
- `api/inventario`: Módulo de inventario (producto, almacenes, stock, etc.)
- `api/produccion`: Gestión de órdenes y procesos de producción
- `swagger-ui`: Interfaz de documentación y pruebas

---

## ✅ Recomendaciones

- Siempre iniciar creando un usuario admin si es la primera vez que se corre el sistema.
- Proteger bien las variables sensibles (`.env`, perfiles, configuración por entorno).
- Usar Swagger para probar rápidamente nuevos endpoints.
- Usar Postman para pruebas automatizadas o secuenciales.

---

## 📞 Contacto

Para dudas técnicas o soporte, contactar al desarrollador principal del sistema.

---