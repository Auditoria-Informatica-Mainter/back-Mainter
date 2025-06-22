package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Detalle_Devolucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer cantidad;
    private Double importe_Total;
    private String motivo_detalle; // Motivo específico para este producto
    
    // Relación con Devolución
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devolucion_id", nullable = false)
    @JsonBackReference("devolucion-detalles")
    private Devolucion devolucion;
    
    // Relación obligatoria con el detalle del pedido original
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_pedido_id", nullable = false)
    private Detalle_pedido detalle_pedido;
}