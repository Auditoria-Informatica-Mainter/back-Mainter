package com.example.BackendProject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.bind.DefaultValue;

import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {

    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long pedidoId;

    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    

    @PositiveOrZero
    private Double importe_Total;
    
    @PositiveOrZero
    private Double importe_Total_Desc;

    @Min(value = 0, message = "El precio unitario debe ser mayor o igual a 0")
    private Double precioUnitario;
    
    

    
    
} 