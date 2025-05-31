package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
 * Los productos son el resultado del proceso de fabricación utilizando materiales y preproductos.
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
    private String tiempo;          // Tiempo estimado de producción
    private Double precioUnitario;
    
      // Relación ManyToOne con Categoria (agregar esto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id") // Nombre de la columna en la BD
    private Categoria categoria; 
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductoMaterial> materiales = new ArrayList<>();
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Plano> planos = new ArrayList<>();
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Orden_Producto> ordenesProducto = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales para crear un producto
     */
    public Producto(String nombre, String descripcion, Integer stock, Integer stock_minimo, String tiempo, Double precioUnitario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.stock_minimo = stock_minimo;
        this.tiempo = tiempo;
        this.imagen = null; // La imagen es opcional, por defecto es null
        this.precioUnitario = precioUnitario;
    }
    
    /**
     * Constructor con parámetros principales incluyendo imagen
     */
    public Producto(String nombre, String descripcion, Integer stock, Integer stock_minimo, String tiempo, String imagen, Double precioUnitario) {
        this(nombre, descripcion, stock, stock_minimo, tiempo, precioUnitario);
        this.imagen = imagen;
    }
}
