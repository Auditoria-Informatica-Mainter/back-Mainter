package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * DTO para mostrar información detallada de productos en un pedido
 * Combina información del producto con los detalles del pedido
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoPedidoDTO {
    
    // Información del detalle del pedido
    private Long detalleId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double importe_Total;
    private Double importe_Total_Desc;
    private Boolean estado;
    
    // Información del producto
    private Long productoId;
    private String nombreProducto;
    private String descripcionProducto;
    private String imagenProducto;
    private String tiempoProduccion;
    private Integer stockDisponible;
    private Integer stockMinimo;
    
    // Información de la categoría del producto (si existe)
    private Long categoriaId;
    private String nombreCategoria;
}
