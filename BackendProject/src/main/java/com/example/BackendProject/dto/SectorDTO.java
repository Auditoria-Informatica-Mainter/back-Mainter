package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la entidad Sector
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorDTO {
    private Long id;
    private String nombre;
    private Double stock;
    private Double capacidad_maxima;
    private String tipo;
    private String descripcion;
    private Long almacenId;
} 