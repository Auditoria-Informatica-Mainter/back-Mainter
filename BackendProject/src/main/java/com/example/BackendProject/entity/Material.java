package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
 * Entidad que representa un material o producto en el sistema.
 */
@Entity
@Table(name = "material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    private String unidadMedida;      // kg, litro, unidad, etc.
    private Double precio;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer puntoReorden;     // Nivel de stock para lanzar reabastecimiento
    private String categoriaText;     // Nombre de la categoría (para compatibilidad)
    private Boolean activo;           // Indica si el material está activo
    private String imagen;            // URL de la imagen del material
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;      // Relación con la entidad Categoria
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "sector_id", nullable = true)
    private Sector sector;            // Relación con la entidad Sector
    
    @OneToMany(mappedBy = "material")
    @JsonIgnore
    private List<ProveedorMaterial> proveedores = new ArrayList<>();
    
    @OneToMany(mappedBy = "material")
    @JsonIgnore
    private List<DetallePedidoCompra> detallesPedidos = new ArrayList<>();
    
    @OneToMany(mappedBy = "material")
    @JsonIgnore
    private List<ProductoMaterial> productos = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales para crear un material
     */
    public Material(String nombre, String descripcion, String unidadMedida, 
                  Double precio, Integer stockActual, Integer stockMinimo, Integer puntoReorden, 
                  Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.precio = precio;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.puntoReorden = puntoReorden;
        this.categoria = categoria;
        this.categoriaText = categoria != null ? categoria.getNombre() : null;
        this.activo = true; // Por defecto, el material está activo
    }
    
    /**
     * Constructor con parámetros principales incluyendo sector
     */
    public Material(String nombre, String descripcion, String unidadMedida, 
                  Double precio, Integer stockActual, Integer stockMinimo, Integer puntoReorden, 
                  Categoria categoria, Sector sector) {
        this(nombre, descripcion, unidadMedida, precio, stockActual, stockMinimo, puntoReorden, categoria);
        this.sector = sector;
    }
    
    /**
     * Constructor con parámetros principales incluyendo imagen
     */
    public Material(String nombre, String descripcion, String unidadMedida, 
                  Double precio, Integer stockActual, Integer stockMinimo, Integer puntoReorden, 
                  Categoria categoria, String imagen) {
        this(nombre, descripcion, unidadMedida, precio, stockActual, stockMinimo, puntoReorden, categoria);
        this.imagen = imagen;
    }
    
    /**
     * Constructor con todos los parámetros principales
     */
    public Material(String nombre, String descripcion, String unidadMedida, 
                  Double precio, Integer stockActual, Integer stockMinimo, Integer puntoReorden, 
                  Categoria categoria, Sector sector, String imagen) {
        this(nombre, descripcion, unidadMedida, precio, stockActual, stockMinimo, puntoReorden, categoria);
        this.sector = sector;
        this.imagen = imagen;
    }
}
