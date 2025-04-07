package com.example.BackendProject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subcategorias")
public class SubCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria_id;

    //Constructor
    public SubCategoria(){}
    public SubCategoria(String nombre, String descripcion){
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

}
