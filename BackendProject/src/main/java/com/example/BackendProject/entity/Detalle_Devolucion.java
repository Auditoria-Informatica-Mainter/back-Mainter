package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_devolucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detalle_Devolucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer cantidad;
    private Double importe_Total;
    private String motivo_detalle; // Motivo específico para este producto
    
    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    // Relación con Devolución
    // Relación con Devolución
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devolucion_id")
    @JsonBackReference("devolucion-detalles")
    private Devolucion devolucion;
    
    // Opcional: Relación con el detalle del pedido original
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_pedido_id")
    private Detalle_pedido detalle_pedido;
}