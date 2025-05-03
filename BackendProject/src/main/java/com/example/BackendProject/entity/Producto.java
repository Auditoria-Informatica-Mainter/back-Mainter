package com.example.BackendProject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
 * Entidad que representa un producto en el sistema.
 * Los productos son el resultado del proceso de fabricación utilizando materiales.
 */
@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    private Integer stock;          // Cantidad actual disponible
    private Integer stock_minimo;   // Límite para generar alertas
    private String imagen;          // URL de imagen del producto (opcional)
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;    // Categorización del producto
    
    @OneToMany(mappedBy = "producto")
    private List<ProductoMaterial> materiales = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales para crear un producto
     */
    public Producto(String nombre, String descripcion, Integer stock, Integer stock_minimo, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.stock_minimo = stock_minimo;
        this.categoria = categoria;
        this.imagen = null; // La imagen es opcional, por defecto es null
    }
    
    /**
     * Constructor con parámetros principales incluyendo imagen
     */
    public Producto(String nombre, String descripcion, Integer stock, Integer stock_minimo, String imagen, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.stock_minimo = stock_minimo;
        this.imagen = imagen;
        this.categoria = categoria;
    }
}
