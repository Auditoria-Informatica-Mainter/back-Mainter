package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * DTO para la entidad Pedido (compra)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private String estado;
    private Date fecha;
    private Double importe_total;
    private Double importe_descuento;
    private Long proveedorId;
    private Long usuarioId;
} 