# VULNERABILIDADES IMPLEMENTADAS PARA AUDITORÍA INFORMÁTICA

## ⚠️ ATENCIÓN: ESTE DOCUMENTO ES SOLO PARA FINES ACADÉMICOS
**Estas vulnerabilidades están implementadas ÚNICAMENTE para demostración en auditoría informática. NUNCA usar en producción.**

---

## MAPEO DE VULNERABILIDADES POR FASE DE AUDITORÍA

### 1. RECONOCIMIENTO (Detectable con theHarvester, Maltego, búsquedas Google)
- **Information Disclosure**: Endpoint `/debug/server-info` expone información del sistema
- **Swagger UI**: Documentación API expuesta sin autenticación
- **Actuator Endpoints**: Todos los endpoints de Spring Actuator expuestos

### 2. EXPLORACIÓN (Detectable con Nmap, Masscan, Nessus)
- **Port Scanning**: Aplicación web en puerto estándar (detecta servicio HTTP/HTTPS)
- **Service Enumeration**: Banner grabbing revela tecnologías (Spring Boot, Java, Tomcat)
- **Vulnerability Scanning**: OpenVAS/Nessus detectarán CORS permisivo, headers inseguros

### 3. ENUMERACIÓN (Detectable con Dirb, Dirbuster, Gobuster)
- **Directory Brute Force**: Rutas expuestas:
  - `/admin/*` - Panel administrativo sin autenticación
  - `/debug/*` - Información de debug
  - `/config/*` - Archivos de configuración
  - `/backup/*` - Archivos de respaldo
  - `/test/*` - Endpoints de prueba
- **File Enumeration**: Archivos sensibles accesibles:
  - `/backup/database.sql`
  - `/backup/config.bak`  
  - `/backup/users.csv`

### 4. OBTENCIÓN DE ACCESO (Explotable con Metasploit, scripts custom)
- **Remote Code Execution (RCE)**: 
  - Endpoint: `GET /admin/execute?cmd=COMMAND`
  - Ejecuta comandos del sistema operativo
  - Explotable con payloads de Metasploit
- **SQL Injection**:
  - Endpoint: `GET /admin/user/{id}`
  - Vulnerable a UNION-based y Error-based SQLi
  - Detectable con SQLMap
- **Authentication Bypass**:
  - Credenciales débiles: admin/admin, guest/guest
  - Endpoint: `POST /admin/login`
- **Directory Traversal**:
  - Endpoint: `GET /admin/file?filename=../../../etc/passwd`
  - Permite lectura de archivos del sistema

### 5. MANTENIMIENTO DE ACCESO (Persistencia)
- **Weak Session Management**: IDs de sesión fijos y predecibles
- **File Upload**: Subida de archivos sin validación (`POST /admin/upload`)
- **Backdoor Simulation**: RCE permite instalar herramientas de persistencia

### 6. ANÁLISIS DE DATOS (Logs y evidencias)
- **Error Messages**: Stacktraces con información sensible
- **Database Queries**: Logs de SQL injection attempts
- **Command Execution**: Logs de comandos ejecutados

---

## VULNERABILIDADES ESPECÍFICAS IMPLEMENTADAS

### 1. Information Disclosure (CVE-2019-12086 similar)
**Endpoint**: `GET /debug/server-info`
**Riesgo**: Alto
**Descripción**: Expone información crítica del sistema incluyendo versiones, variables de entorno, directorios de trabajo.

### 2. Remote Code Execution - RCE (CVE-2022-22965 similar)  
**Endpoint**: `GET /admin/execute?cmd=COMMAND`
**Riesgo**: Crítico
**Descripción**: Permite ejecución de comandos arbitrarios del sistema operativo.
**Exploit**: `curl "https://tu-app.koyeb.app/admin/execute?cmd=whoami"`

### 3. SQL Injection (OWASP Top 10 #03)
**Endpoint**: `GET /admin/user/{id}`
**Riesgo**: Alto
**Descripción**: Inyección SQL sin parámetros preparados.
**Exploit**: `curl "https://tu-app.koyeb.app/admin/user/1' UNION SELECT version()--"`

### 4. Directory Traversal (CVE-2021-41773 similar)
**Endpoint**: `GET /admin/file?filename=PATH`
**Riesgo**: Alto  
**Descripción**: Permite lectura de archivos fuera del directorio web.
**Exploit**: `curl "https://tu-app.koyeb.app/admin/file?filename=../../../etc/passwd"`

### 5. Sensitive Data Exposure (OWASP Top 10 #02)
**Endpoint**: `GET /config/database`
**Riesgo**: Crítico
**Descripción**: Expone credenciales de base de datos, API keys y secretos.

### 6. CORS Misconfiguration (CWE-942)
**Configuración**: `Access-Control-Allow-Origin: *`
**Riesgo**: Medio
**Descripción**: Permite ataques desde cualquier origen web.

### 7. Weak Authentication (CWE-287)
**Endpoint**: `POST /admin/login`
**Credenciales**: admin/admin, guest/guest
**Riesgo**: Alto
**Descripción**: Credenciales débiles y predecibles.

### 8. File Upload Vulnerability (CWE-434)
**Endpoint**: `POST /admin/upload`
**Riesgo**: Alto
**Descripción**: Subida de archivos sin validación de tipo o contenido.

### 9. XML External Entity - XXE (CWE-611)
**Endpoint**: `POST /admin/xml`  
**Riesgo**: Alto
**Descripción**: Procesamiento XML vulnerable a ataques XXE.

### 10. Backup Files Exposure (CWE-200)
**Endpoints**: `/backup/{filename}`
**Archivos**: database.sql, config.bak, users.csv
**Riesgo**: Alto
**Descripción**: Archivos de respaldo accesibles públicamente.

---

## HERRAMIENTAS DE DETECCIÓN RECOMENDADAS

### Reconocimiento:
- `theHarvester -d tu-dominio.com -b google`
- `whois tu-dominio.com`

### Exploración:
- `nmap -sS -sV -O tu-app.koyeb.app`
- `nessus` - Vulnerability scan
- `openvas` - Comprehensive security scan

### Enumeración:
- `gobuster dir -u https://tu-app.koyeb.app -w /usr/share/wordlists/dirbuster/directory-list-2.3-medium.txt`
- `dirb https://tu-app.koyeb.app /usr/share/wordlists/dirb/common.txt`

### Explotación:
- `sqlmap -u "https://tu-app.koyeb.app/admin/user/1" --dbs`
- `msfconsole` - Para exploits de RCE
- `curl` - Para pruebas manuales

### Análisis:
- `nikto -h https://tu-app.koyeb.app`
- `burpsuite` - Proxy para interceptar tráfico

---

## COMANDOS DE DEMOSTRACIÓN

```bash
# 1. Reconocimiento
nmap -sS -sV tu-app.koyeb.app

# 2. Enumeración de directorios  
gobuster dir -u https://tu-app.koyeb.app -w /usr/share/wordlists/dirbuster/directory-list-2.3-medium.txt

# 3. Information Disclosure
curl https://tu-app.koyeb.app/debug/server-info

# 4. RCE Exploit
curl "https://tu-app.koyeb.app/admin/execute?cmd=whoami"
curl "https://tu-app.koyeb.app/admin/execute?cmd=cat /etc/passwd"

# 5. SQL Injection
curl "https://tu-app.koyeb.app/admin/user/1' UNION SELECT version()--"

# 6. Directory Traversal
curl "https://tu-app.koyeb.app/admin/file?filename=../../../etc/passwd"

# 7. Backup Files Access
curl https://tu-app.koyeb.app/backup/database.sql
curl https://tu-app.koyeb.app/backup/config.bak

# 8. Sensitive Data Exposure
curl https://tu-app.koyeb.app/config/database
```

---

## MITIGACIONES IMPLEMENTADAS DURANTE LA FASE 9

1. **Deshabilitar endpoints vulnerables**
2. **Implementar autenticación robusta**
3. **Configurar CORS restrictivo**
4. **Validar todas las entradas**
5. **Usar consultas preparadas**
6. **Eliminar archivos de backup expuestos**
7. **Configurar headers de seguridad**
8. **Implementar rate limiting**
9. **Deshabilitar información de debug**
10. **Actualizar configuración de Spring Security**

---

## NOTAS PARA LA PRESENTACIÓN

- Demostrar cada vulnerabilidad en vivo
- Mostrar detección con herramientas específicas
- Documentar impacto de cada vulnerabilidad
- Presentar mitigaciones efectivas
- Generar reportes de Nessus/OpenVAS
- Crear matriz de riesgos CVSS

**Recuerda**: Estas vulnerabilidades están implementadas solo para fines educativos. Al finalizar la auditoría, restaura la configuración segura.
