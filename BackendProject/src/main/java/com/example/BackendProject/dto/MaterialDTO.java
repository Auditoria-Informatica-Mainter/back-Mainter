package com.example.BackendProject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la transferencia de datos de Material entre capas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDTO {
    private Long id;
    
    @NotEmpty(message = "El nombre del material es obligatorio")
    private String nombre;
    
    @NotNull(message = "El stock inicial es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockActual;
    
    @NotEmpty(message = "La unidad de medida es obligatoria")
    private String unidadMedida;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    // Campos opcionales
    private String imagen;
    private String descripcion;
    private Long sectorId;
} 