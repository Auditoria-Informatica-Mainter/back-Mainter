# Multi-stage build para optimizar el tamaño de la imagen final

# Etapa 1: Build
FROM eclipse-temurin:17-jdk AS build

# Instalar Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml primero para aprovechar la cache de Docker
COPY BackendProject/pom.xml .

# Descargar las dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY BackendProject/src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-jammy

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Crear usuario no-root para seguridad
RUN useradd -r -u 1000 -m -c "app user" -d /app -s /bin/false app

# Establecer el directorio de trabajo
WORKDIR /app

# Cambiar al usuario no-root
USER app

# Copiar el JAR desde la etapa de build
COPY --from=build --chown=app:app /app/target/*.jar app.jar

# Exponer el puerto (Render detecta automáticamente el puerto)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/mrp/actuator/health || exit 1

# Variables de entorno por defecto (optimizadas para 512MB)
ENV JAVA_OPTS="-Xmx384m -Xms128m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SERVER_PORT=8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
