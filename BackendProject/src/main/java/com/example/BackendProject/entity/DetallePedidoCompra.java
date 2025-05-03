package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa el detalle de un pedido de compra en el sistema.
 */
@Entity
@Table(name = "detalle_pedido_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer cantidad;
    private String estado;
    private Double importe;
    private Double importe_desc;
    private Double precio;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "material_id")
    @JsonBackReference
    private Material material;
    
    /**
     * Constructor con parámetros principales
     */
    public DetallePedidoCompra(Integer cantidad, Double precio, Double importe, 
                        Double importe_desc, String estado, Pedido pedido, Material material) {
        this.cantidad = cantidad;
        this.precio = precio;
        this.importe = importe;
        this.importe_desc = importe_desc;
        this.estado = estado;
        this.pedido = pedido;
        this.material = material;
    }
} 