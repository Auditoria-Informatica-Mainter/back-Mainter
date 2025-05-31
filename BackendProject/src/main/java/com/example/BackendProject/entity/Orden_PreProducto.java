package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orden_preproducto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Orden_PreProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private String descripcion;
    private String estado;
    private LocalDateTime fecha;

    // Relación muchos a uno con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    // Relación muchos a uno con Pre_producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preproducto_id")
    @JsonBackReference
    private Pre_producto preProducto;
} 