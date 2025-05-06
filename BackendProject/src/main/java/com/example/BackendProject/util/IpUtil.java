package com.example.BackendProject.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class IpUtil {
    private static final String[] IP_HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "X-Real-IP",
        "X-RealIP",
        "CF-Connecting-IP", // Header específico de Cloudflare
        "True-Client-IP",   // Header adicional usado por algunos CDNs
        "REMOTE_ADDR"
    };

    public String getClientIp(HttpServletRequest request) {
        String ip = null;
        
        // Intentar obtener IP de los headers conocidos
        for (String header : IP_HEADERS) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                // Si es una lista de IPs (proxies), tomar la primera
                if (ip.contains(",")) {
                    // Buscar la primera IP válida en la lista
                    String[] ips = ip.split(",");
                    for (String singleIp : ips) {
                        singleIp = singleIp.trim();
                        if (isValidIpv4(singleIp)) {
                            return singleIp;
                        }
                    }
                    // Si no encontramos IPv4, usar la primera IP válida
                    ip = ips[0].trim();
                }
                break;
            }
        }
        
        // Si no se encontró en los headers, usar getRemoteAddr
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // Normalizar localhost IPv6 a IPv4
        if (ip != null) {
            if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("::1")) {
                ip = "127.0.0.1";
            } else if (isValidIpv6(ip)) {
                // Intentar convertir otras IPv6 a IPv4 si es posible
                String ipv4 = convertIpv6ToIpv4(ip);
                if (ipv4 != null) {
                    ip = ipv4;
                }
            }
        }

        return ip != null ? ip : "0.0.0.0";
    }

    private boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
            return false;
        }
        return isValidIpv4(ip) || isValidIpv6(ip);
    }

    private boolean isValidIpv4(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipv4Pattern);
    }

    private boolean isValidIpv6(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        String ipv6Pattern = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$|^([0-9a-fA-F]{1,4}:){0,6}:[0-9a-fA-F]{1,4}$";
        return ip.matches(ipv6Pattern);
    }

    private String convertIpv6ToIpv4(String ipv6) {
        // Convertir direcciones IPv6 mapeadas a IPv4
        if (ipv6.startsWith("::ffff:")) {
            String ipv4Part = ipv6.substring(7);
            if (isValidIpv4(ipv4Part)) {
                return ipv4Part;
            }
        }
        return null;
    }
}