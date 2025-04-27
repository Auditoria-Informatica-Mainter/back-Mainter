package com.example.BackendProject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la transferencia de datos de la relación ProductoMaterial (BOM)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaterialDTO {
    @NotNull(message = "El ID del material es obligatorio")
    private Long materialId;
    
    // Campo para lectura, no es obligatorio en la creación
    private String nombreMaterial;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
} 