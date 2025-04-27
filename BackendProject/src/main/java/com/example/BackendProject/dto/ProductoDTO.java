package com.example.BackendProject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para la transferencia de datos de Producto entre capas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    
    @NotEmpty(message = "El nombre del producto es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "El stock inicial es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stock_minimo;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    // Campo opcional
    private String imagen;
    
    // Lista de materiales necesarios para el producto
    private List<@Valid ProductoMaterialDTO> materiales;
} 