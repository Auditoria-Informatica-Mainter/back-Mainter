package com.example.BackendProject.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pre_maquinarias")
public class PreMaquinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private String descripcion;
    private String tiempoEstimado;

    @ManyToOne
    @JoinColumn(name = "maquinaria_id")
    private Maquinaria maquinaria;
}