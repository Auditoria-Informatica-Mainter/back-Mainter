package com.example.BackendProject.service;

import com.example.BackendProject.entity.Bitacora;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.BitacoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    private static final Logger logger = LoggerFactory.getLogger(BitacoraService.class);

    public void registrarEvento(Usuario usuario, String accion, String detalles, String direccionIp) {
        Bitacora bitacora = Bitacora.builder()
                .usuario(usuario)
                .accion(accion)
                .fecha(LocalDateTime.now())
                .detalles(detalles)
                .direccionIp(direccionIp)
                .build();
        
        bitacoraRepository.save(bitacora);
    }

    public void registrarLogin(Usuario usuario, String direccionIp) {
        registrarEvento(usuario, "LOGIN", "Inicio de sesión exitoso", direccionIp);
    }

    public void registrarLogout(Usuario usuario, String direccionIp) {
        registrarEvento(usuario, "LOGOUT", "Cierre de sesión", direccionIp);
    }

    public void logLoginAttempt(String username, boolean success, String direccionIp) {
        String message = String.format("Intento de inicio de sesión - Usuario: %s, Resultado: %s, Fecha: %s, IP: %s",
                username,
                success ? "EXITOSO" : "FALLIDO",
                LocalDateTime.now(),
                direccionIp);
        if (success) {
            logger.info(message);
        } else {
            logger.warn(message);
        }
    }

    public void logRegistration(String username, String tipo, String direccionIp) {
        String message = String.format("Registro de nuevo usuario - Usuario: %s, Tipo: %s, Fecha: %s, IP: %s",
                username,
                tipo,
                LocalDateTime.now(),
                direccionIp);
        logger.info(message);
    }

    // Métodos adicionales para consultar la bitácora
    public List<Bitacora> obtenerHistorialCompleto() {
        return bitacoraRepository.findAll();
    }

    public List<Bitacora> obtenerHistorialPorUsuario(Usuario usuario) {
        return bitacoraRepository.findByUsuario(usuario);
    }

    public List<Bitacora> obtenerHistorialPorAccion(String accion) {
        return bitacoraRepository.findByAccion(accion);
    }

    public List<Bitacora> obtenerHistorialPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return bitacoraRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public List<Bitacora> obtenerHistorialPorDireccionIp(String direccionIp) {
        return bitacoraRepository.findByDireccionIp(direccionIp);
    }
}