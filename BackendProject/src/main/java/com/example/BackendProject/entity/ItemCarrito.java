package com.example.BackendProject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "item_carrito")
@Data
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    private Integer cantidad;
    
    public Double getSubtotal() {
        return producto.getPrecioUnitario() * cantidad;
    }
} 