package com.example.BackendProject.dto;

import com.example.BackendProject.entity.ItemCarrito;
import lombok.Data;

@Data
public class ItemCarritoDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    
    public static ItemCarritoDTO fromEntity(ItemCarrito item) {
        ItemCarritoDTO dto = new ItemCarritoDTO();
        dto.setId(item.getId());
        dto.setProductoId(item.getProducto().getId());
        dto.setNombreProducto(item.getProducto().getNombre());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getProducto().getPrecioUnitario());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
} 