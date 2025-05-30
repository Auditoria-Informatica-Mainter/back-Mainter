package com.example.BackendProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreMaquinariaDTO {
    private Long id;
    private Integer cantidad;
    private String descripcion;
    private String tiempoEstimado;
    private Long maquinariaId;
    private Long preProductoId;
    
    // Constructor para crear nueva planificación
    public PreMaquinariaDTO(Integer cantidad, String descripcion, String tiempoEstimado, 
                           Long maquinariaId, Long preProductoId) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.tiempoEstimado = tiempoEstimado;
        this.maquinariaId = maquinariaId;
        this.preProductoId = preProductoId;
    }
} 