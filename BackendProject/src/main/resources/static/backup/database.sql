-- Backup de Base de Datos MRP System
-- Fecha: 2024-12-15
-- ARCHIVO SENSIBLE - NO DEBERÍA ESTAR EXPUESTO

CREATE DATABASE IF NOT EXISTS mrp_system;
USE mrp_system;

-- Tabla de usuarios con credenciales
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Datos sensibles de usuarios
INSERT INTO usuarios (username, password, email, role) VALUES
('admin', 'admin123', 'admin@company.com', 'ADMIN'),
('root', 'root2024', 'root@company.com', 'SUPER_ADMIN'),
('manager', 'manager456', 'manager@company.com', 'MANAGER'),
('user1', 'password123', 'user1@company.com', 'USER'),
('guest', 'guest', 'guest@company.com', 'GUEST'),
('developer', 'dev123', 'dev@company.com', 'DEVELOPER'),
('operator', 'op2024', 'operator@company.com', 'OPERATOR');

-- Tabla de API Keys
CREATE TABLE api_keys (
    id INT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(50),
    api_key VARCHAR(200),
    secret_key VARCHAR(200),
    environment VARCHAR(20)
);

INSERT INTO api_keys (service_name, api_key, secret_key, environment) VALUES
('payment_gateway', 'pk_live_1234567890abcdef', 'sk_live_abcdef1234567890', 'production'),
('email_service', 'key_email_987654321', 'secret_email_123456789', 'production'),
('sms_service', 'sms_api_key_2024', 'sms_secret_2024', 'production'),
('storage_service', 'storage_key_prod', 'storage_secret_prod', 'production');

-- Configuraciones del sistema
CREATE TABLE system_config (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value TEXT,
    is_sensitive BOOLEAN DEFAULT FALSE
);

INSERT INTO system_config (config_key, config_value, is_sensitive) VALUES
('database.url', 'jdbc:mysql://prod-db.internal:3306/mrp_system', TRUE),
('database.username', 'db_admin', TRUE),
('database.password', 'SuperSecretDBPass2024!', TRUE),
('jwt.secret', 'myJWTSecretKeyThatShouldNeverBeExposed2024', TRUE),
('encryption.key', 'AES256-MasterKey-2024-Production', TRUE),
('admin.default.password', 'admin123', TRUE);

-- Logs de acceso (información sensible)
CREATE TABLE access_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    ip_address VARCHAR(45),
    action VARCHAR(100),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO access_logs (username, ip_address, action) VALUES
('admin', '192.168.1.100', 'LOGIN_SUCCESS'),
('admin', '192.168.1.100', 'DATABASE_BACKUP_CREATED'),
('root', '10.0.0.5', 'SYSTEM_CONFIG_CHANGED'),
('developer', '172.16.0.25', 'API_KEY_ACCESSED');

COMMIT;
