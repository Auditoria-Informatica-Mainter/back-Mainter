package com.example.BackendProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorConAlmacenDTO {
    private Long id;
    private String nombre;
    private Double stock;
    private Double capacidad_maxima;
    private String tipo;
    private String descripcion;

    // Almacén resumido
    private Long almacenId;
    private String almacenNombre;
} 