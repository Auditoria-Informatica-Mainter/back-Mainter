package com.example.BackendProject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un almacén físico en el sistema.
 */
@Entity
@Table(name = "almacen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nombre;
    private Double capacidad;
    
    @OneToMany(mappedBy = "almacen")
    private List<Sector> sectores = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales
     */
    public Almacen(String nombre, Double capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }
}
