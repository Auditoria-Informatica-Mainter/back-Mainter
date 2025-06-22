package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleDevolucionResponseDTO {
    private Long id;
    private Long detallePedidoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private String motivo_detalle;
}
