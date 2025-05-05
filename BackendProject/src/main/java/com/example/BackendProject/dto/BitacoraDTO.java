package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraDTO {
    private Long id;
    private String accion;
    private String detalles;
    private LocalDateTime fecha;
    private Long usuarioId;
    private String nombreUsuario;
    private String direccionIp;
}