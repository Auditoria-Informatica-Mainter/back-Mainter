package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "devolucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Devolucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Date fecha;
    private String motivo;
    private String descripcion;
    private Double importe_total;
    private Boolean estado; // true = procesada, false = pendiente
    
    // Relación con Usuario (cliente que realiza la devolución)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario-devoluciones")
    private Usuario usuario;
    
    // Relación con Pedido (pedido original que se está devolviendo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonBackReference("pedido-devoluciones")
    private Pedido pedido;
    
    // Relación con los detalles de la devolución
    @OneToMany(mappedBy = "devolucion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("devolucion-detalles")
    private List<Detalle_Devolucion> detalles = new ArrayList<>();
}