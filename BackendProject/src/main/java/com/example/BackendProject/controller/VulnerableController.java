package com.example.BackendProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.nio.file.*;

/**
 * CONTROLADOR VULNERABLE PARA AUDITORÍA INFORMÁTICA
 * Este controlador contiene vulnerabilidades intencionales para demostración académica
 * NUNCA usar en producción
 */
@RestController
@RequestMapping("/")
public class VulnerableController {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    // VULNERABILIDAD 1: Information Disclosure - Banner Grabbing (detectable con Nmap)
    @GetMapping("/debug/server-info")
    public Map<String, Object> getServerInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("server", "Apache Tomcat/9.0.65");
        info.put("java_version", System.getProperty("java.version"));
        info.put("os", System.getProperty("os.name"));
        info.put("user", System.getProperty("user.name"));
        info.put("working_directory", System.getProperty("user.dir"));
        info.put("environment", System.getenv());
        return info;
    }

    // VULNERABILIDAD 2: Remote Code Execution (RCE) - Explotable con Metasploit
    @GetMapping("/admin/execute")
    public ResponseEntity<String> executeCommand(@RequestParam String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Debug-Info", "Command executed successfully");
            headers.add("X-Server-Version", "BackendProject-1.0-VULNERABLE");
            
            return ResponseEntity.ok().headers(headers).body(output.toString());
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    // VULNERABILIDAD 3: SQL Injection (detectable con SQLMap)
    @GetMapping("/admin/user/{id}")
    public ResponseEntity<String> getUserById(@PathVariable String id) {
        if (jdbcTemplate != null) {
            try {
                // Consulta SQL vulnerable sin parámetros
                String query = "SELECT * FROM usuarios WHERE id = " + id;
                List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
                return ResponseEntity.ok(result.toString());
            } catch (Exception e) {
                return ResponseEntity.ok("SQL Error: " + e.getMessage() + " Query: SELECT * FROM usuarios WHERE id = " + id);
            }
        }
        return ResponseEntity.ok("Database not configured, but SQL injection would work with: SELECT * FROM usuarios WHERE id = " + id);
    }

    // VULNERABILIDAD 4: Directory Traversal/Path Traversal (detectable con Dirb/Gobuster)
    @GetMapping("/admin/file")
    public ResponseEntity<String> readFile(@RequestParam String filename) {
        try {
            // Vulnerable a directory traversal
            Path path = Paths.get(filename);
            String content = Files.readString(path);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.ok("File not found or error: " + e.getMessage());
        }
    }

    // VULNERABILIDAD 5: Sensitive Data Exposure (detectable con Nessus/OpenVAS)
    @GetMapping("/config/database")
    public Map<String, String> getDatabaseConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("database_url", "jdbc:mysql://db-server:3306/production");
        config.put("username", "admin");
        config.put("password", "admin123"); // Credencial hardcodeada
        config.put("api_key", "sk-1234567890abcdef");
        config.put("secret_key", "supersecretkey2024");
        return config;
    }

    // VULNERABILIDAD 6: XML External Entity (XXE) Injection
    @PostMapping("/admin/xml")
    public ResponseEntity<String> processXml(@RequestBody String xmlData) {
        try {
            // Procesamiento XML vulnerable
            return ResponseEntity.ok("XML processed: " + xmlData);
        } catch (Exception e) {
            return ResponseEntity.ok("XML Error: " + e.getMessage());
        }
    }

    // VULNERABILIDAD 7: Unrestricted File Upload
    @PostMapping("/admin/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") String fileContent, 
                                           @RequestParam("filename") String filename) {
        try {
            // Guardado de archivo sin validación
            Files.write(Paths.get("/tmp/" + filename), fileContent.getBytes());
            return ResponseEntity.ok("File uploaded: " + filename);
        } catch (Exception e) {
            return ResponseEntity.ok("Upload error: " + e.getMessage());
        }
    }

    // VULNERABILIDAD 8: Weak Session Management
    @GetMapping("/admin/session")
    public Map<String, Object> getSessionInfo(HttpServletRequest request) {
        Map<String, Object> session = new HashMap<>();
        session.put("session_id", "FIXED_SESSION_123456"); // ID de sesión fijo
        session.put("user_role", "ADMIN");
        session.put("created", new Date());
        session.put("client_ip", request.getRemoteAddr());
        return session;
    }

    // VULNERABILIDAD 9: Backup Files Exposure (detectable con Dirb/Gobuster)
    @GetMapping("/backup/{filename}")
    public ResponseEntity<String> getBackupFile(@PathVariable String filename) {
        try {
            // Simula archivos de backup expuestos
            Map<String, String> backupFiles = new HashMap<>();
            backupFiles.put("database.sql", "-- Database backup\nCREATE TABLE usuarios (id INT, username VARCHAR(50), password VARCHAR(50));\nINSERT INTO usuarios VALUES (1, 'admin', 'admin123');");
            backupFiles.put("config.bak", "database.password=admin123\napi.key=sk-1234567890abcdef\nsecret.key=supersecretkey2024");
            backupFiles.put("users.csv", "id,username,password,email\n1,admin,admin123,admin@company.com\n2,user,user123,user@company.com");
            
            String content = backupFiles.getOrDefault(filename, "Backup file not found");
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    // VULNERABILIDAD 10: Error-based Information Disclosure
    @GetMapping("/test/error")
    public ResponseEntity<String> triggerError() {
        try {
            // Provoca un error que expone información del sistema
            throw new RuntimeException("Database connection failed: jdbc:mysql://internal-db:3306/production with user 'admin'");
        } catch (Exception e) {
            return ResponseEntity.ok("Internal Server Error: " + e.getMessage() + 
                                   "\nStack trace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    // VULNERABILIDAD 11: Weak Authentication Bypass
    @PostMapping("/admin/login")
    public Map<String, Object> adminLogin(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        
        // Bypass de autenticación con credenciales débiles
        if ("admin".equals(username) && "admin".equals(password)) {
            response.put("status", "success");
            response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.FIXED_TOKEN_ADMIN.signature");
            response.put("role", "ADMIN");
        } else if ("guest".equals(username) && "guest".equals(password)) {
            response.put("status", "success");
            response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.FIXED_TOKEN_GUEST.signature");
            response.put("role", "USER");
        } else {
            response.put("status", "failed");
            response.put("message", "Invalid credentials. Try admin/admin or guest/guest");
        }
        
        return response;
    }
}
