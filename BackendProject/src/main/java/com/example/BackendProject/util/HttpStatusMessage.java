package com.example.BackendProject.util;

import org.springframework.http.HttpStatus;

public class HttpStatusMessage {
	
	public static String getMessage(HttpStatus status) {
        switch (status) {
            case OK:
                return "Operación completada con éxito.";
            case CREATED:
                return "Recurso creado con éxito.";
            case NO_CONTENT:
                return "Operación completada con éxito. No hay contenido para devolver.";
            case BAD_REQUEST:
                return "Solicitud incorrecta. Por favor, revisa los datos enviados.";
            case UNAUTHORIZED:
                return "No estás autorizado para acceder a este recurso.";
            case FORBIDDEN:
                return "No tienes permiso para realizar esta operación.";
            case NOT_FOUND:
                return "Recurso no encontrado.";
            case INTERNAL_SERVER_ERROR:
                return "Error interno del servidor. Por favor, inténtalo de nuevo más tarde.";
            default:
                return "";
        }
    }

}
