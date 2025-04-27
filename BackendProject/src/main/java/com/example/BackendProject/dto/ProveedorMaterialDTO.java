package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la relación entre proveedores y materiales
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorMaterialDTO {
    private Long materialId;
    private Double precio;
    private Integer cantidadMinima;
    private String descripcion;
} 