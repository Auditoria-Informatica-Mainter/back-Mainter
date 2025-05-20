package com.example.BackendProject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entidad que representa una compra en el sistema.
 */
@Entity
@Table(name = "compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String estado;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    private Double importe_total;
    private Double importe_descuento;
    
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @OneToMany(mappedBy = "compra")
    private List<DetallePedidoCompra> detalles = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales
     */
    public Compra(String estado, Date fecha, Double importe_total, 
                 Double importe_descuento, Proveedor proveedor, Usuario usuario) {
        this.estado = estado;
        this.fecha = fecha;
        this.importe_total = importe_total;
        this.importe_descuento = importe_descuento;
        this.proveedor = proveedor;
        this.usuario = usuario;
    }
} 