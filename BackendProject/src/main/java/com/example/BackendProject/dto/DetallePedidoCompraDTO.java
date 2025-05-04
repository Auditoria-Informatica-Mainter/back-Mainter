package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la entidad DetallePedidoCompra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoCompraDTO {
    private Long id;
    private Integer cantidad;
    private String estado;
    private Double importe;
    private Double importe_desc;
    private Double precio;
    private Long compraId;
    private Long materialId;
} 