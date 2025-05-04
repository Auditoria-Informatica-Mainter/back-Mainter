package com.example.BackendProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResumidoDTO {
    private Long id;
    private String nombre;
    private String unidadMedida;
    private Double precio;
    private Integer stockActual;
    
    // Información de la relación proveedor-material
    private Double precioProveedor;
    private Integer cantidadMinima;
} 