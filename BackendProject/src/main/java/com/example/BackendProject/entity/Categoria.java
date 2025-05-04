package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Entidad que representa una categoría de materiales en el sistema.
 */
@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    private SubCategoria subCategoria;
    
    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Material> materiales = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales para crear una categoría
     */
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true; // Por defecto, la categoría está activa
    }
    
    /**
     * Constructor con parámetros incluyendo subcategoría
     */
    public Categoria(String nombre, String descripcion, SubCategoria subCategoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.subCategoria = subCategoria;
        this.activo = true;
    }
}
