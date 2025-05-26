package com.example.BackendProject.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "maquinaria_carpinteros")
public class MaquinariaCarpintero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estado; 

    @ManyToOne
    @JoinColumn(name = "maquinaria_id")
    private Maquinaria maquinaria;

    @ManyToOne
    @JoinColumn(name = "carpintero_id")
    private Usuario carpintero; // Relación con Usuario (Carpintero)
}
