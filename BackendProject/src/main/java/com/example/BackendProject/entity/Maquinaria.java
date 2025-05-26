package com.example.BackendProject.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "maquinarias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maquinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String descripcion;

    // Relación con Pre_Maquinaria
    @OneToMany(mappedBy = "maquinaria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PreMaquinaria> preMaquinarias = new ArrayList<>();

    // Relación con Maquinaria_Carpintero
    @OneToMany(mappedBy = "maquinaria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MaquinariaCarpintero> maquinariaCarpinteros = new ArrayList<>();
}