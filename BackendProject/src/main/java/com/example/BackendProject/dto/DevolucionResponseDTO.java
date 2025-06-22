package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionResponseDTO {
    private Long id;
    private Date fecha;
    private String motivo;
    private String descripcion;
    private Double importe_total;
    private Boolean estado;
    
    // Datos básicos del usuario
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    
    // Datos básicos del pedido
    private Long pedidoId;
    private Date pedidoFecha;
    
    // Lista de detalles de la devolución
    private List<DetalleDevolucionResponseDTO> detalles;
}
