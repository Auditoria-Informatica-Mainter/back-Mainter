package com.example.BackendProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pre_plano")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pre_plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer cantidad;
    private String descripcion;
    private String tiempo_estimado;
    
    @ManyToOne
    @JoinColumn(name = "pre_producto_id")
    private Pre_producto preProducto;
    
    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
    
    public Pre_plano(Pre_producto preProducto, Material material, Integer cantidad, String descripcion, String tiempo_estimado) {
        this.preProducto = preProducto;
        this.material = material;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.tiempo_estimado = tiempo_estimado;
    }
}
