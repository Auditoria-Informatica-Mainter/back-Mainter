package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * Entidad que representa la relación entre un proveedor y los materiales que suministra.
 */
@Entity
@Table(name = "proveedor_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @JsonBackReference
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
    
    private Double precio;           // Precio unitario
    private Integer cantidadMinima;  // Cantidad mínima de pedido
    private String descripcion;      // Descripción adicional de la relación
    
    /**
     * Constructor con parámetros principales
     */
    public ProveedorMaterial(Proveedor proveedor, Material material, Double precio, 
                            Integer cantidadMinima, String descripcion) {
        this.proveedor = proveedor;
        this.material = material;
        this.precio = precio;
        this.cantidadMinima = cantidadMinima;
        this.descripcion = descripcion;
    }
} 