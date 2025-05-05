package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
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
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "estado", nullable = true)
    private String estado;
    
    @Column(name = "importe", nullable = true)
    private Double importe;
    
    @Column(name = "importe_desc", nullable = true)
    private Double importe_desc;
    
    @Column(name = "precio", nullable = true)
    private Double precio;
    
    @ManyToOne
    @JoinColumn(name = "compra_id")
    @JsonBackReference
    private Compra compra;
    
    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
    
    /**
     * Constructor con parámetros principales
     */
    public DetallePedidoCompra(Integer cantidad, Double precio, Double importe, 
                        Double importe_desc, String estado, Compra compra, Material material) {
        this.cantidad = cantidad;
        this.precio = precio;
        this.importe = importe;
        this.importe_desc = importe_desc;
        this.estado = estado;
        this.compra = compra;
        this.material = material;
    }
} 