package com.example.BackendProject.config;

import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.service.BitacoraService;
import com.example.BackendProject.service.UsuarioService;
import com.example.BackendProject.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private IpUtil ipUtil;

    @Autowired
    private UsuarioService usuarioService;

    @Around("@annotation(loggableAction)")
    public Object logAction(ProceedingJoinPoint joinPoint, LoggableAction loggableAction) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        // Obtener el usuario actual
        Usuario usuario = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            usuario = usuarioService.getUser(username);
        }

        // Obtener IP de forma segura
        String ip = "0.0.0.0";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null && attributes.getRequest() != null) {
            ip = ipUtil.getClientIp(attributes.getRequest());
        }

        // Ejecutar el método original
        Object result = null;
        try {
            result = joinPoint.proceed();
            // Registrar la acción exitosa
            bitacoraService.registrarEvento(
                usuario,
                methodName.toUpperCase(),
                String.format("Acción exitosa en %s.%s por usuario %s", 
                    className, methodName, 
                    usuario != null ? usuario.getEmail() : "sistema"),
                ip
            );
        } catch (Exception e) {
            // Registrar la acción fallida
            bitacoraService.registrarEvento(
                usuario,
                methodName.toUpperCase(),
                String.format("Error en %s.%s por usuario %s: %s", 
                    className, methodName,
                    usuario != null ? usuario.getEmail() : "sistema",
                    e.getMessage()),
                ip
            );
            throw e;
        }

        return result;
    }
}