package com.example.BackendProject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrePlanoDTO {
    private Long id;
    
    @NotNull(message = "El ID del preproducto es obligatorio")
    private Long preProductoId;
    
    @NotNull(message = "El ID del material es obligatorio")
    private Long materialId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
    
    private String descripcion;
    
    @NotNull(message = "El tiempo estimado es obligatorio")
    private String tiempo_estimado;
}