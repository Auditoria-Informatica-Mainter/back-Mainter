package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la relación entre un producto y los materiales necesarios para su fabricación (BOM - Bill of Materials).
 */
@Entity
@Table(name = "producto_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    @JsonIgnore
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
    
    private Integer cantidad;  // Cantidad del material necesaria para el producto
    
    /**
     * Constructor con parámetros principales
     */
    public ProductoMaterial(Producto producto, Material material, Integer cantidad) {
        this.producto = producto;
        this.material = material;
        this.cantidad = cantidad;
    }
}