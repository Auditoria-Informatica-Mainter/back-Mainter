package com.example.BackendProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plano")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer cantidad;
    private String descripcion;
    private String tiempo_estimado;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "pre_producto_id")
    private Pre_producto preProducto;
    
    public Plano(Producto producto, Pre_producto preProducto, Integer cantidad, String descripcion, String tiempo_estimado) {
        this.producto = producto;
        this.preProducto = preProducto;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.tiempo_estimado = tiempo_estimado;
    }
}
