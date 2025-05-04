@echo off
echo Configurando entorno para Java 17...
set "JAVA_HOME=C:\Program Files\Java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo JAVA_HOME configurado a: %JAVA_HOME%
echo.

cd BackendProject
echo Ejecutando aplicación Spring Boot...
call .\mvnw spring-boot:run

echo.
pause 