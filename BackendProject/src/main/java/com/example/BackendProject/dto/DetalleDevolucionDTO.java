package com.example.BackendProject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleDevolucionDTO {
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
    

    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    private String motivo_detalle;
    
    // Opcional: ID del detalle de pedido original
    private Long detallePedidoId;
}