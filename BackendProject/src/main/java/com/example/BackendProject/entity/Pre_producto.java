package com.example.BackendProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pre_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pre_producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    private Integer stock;
    private String tiempo;
    private String url_Image;
    
    @OneToMany(mappedBy = "preProducto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Pre_plano> prePlanos = new ArrayList<>();
    
    @OneToMany(mappedBy = "preProducto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Plano> planos = new ArrayList<>();
    
    public Pre_producto(String nombre, String descripcion, Integer stock, String tiempo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.tiempo = tiempo;
    }
    
    public Pre_producto(String nombre, String descripcion, Integer stock, String tiempo, String url_Image) {
        this(nombre, descripcion, stock, tiempo);
        this.url_Image = url_Image;
    }
}
