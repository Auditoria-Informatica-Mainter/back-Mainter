package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un sector dentro de un almacén.
 */
@Entity
@Table(name = "sector")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private Double stock;
    private Double capacidad_maxima;
    private String tipo;
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "almacen_id")
    @JsonIgnoreProperties("sectores")
    private Almacen almacen;
    
    @OneToMany(mappedBy = "sector")
    @JsonIgnore
    private List<Material> materiales = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales
     */
    public Sector(String nombre, Double stock, Double capacidad_maxima, String tipo, 
                 String descripcion, Almacen almacen) {
        this.nombre = nombre;
        this.stock = stock;
        this.capacidad_maxima = capacidad_maxima;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.almacen = almacen;
    }
}
