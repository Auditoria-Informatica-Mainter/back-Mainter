package com.example.BackendProject.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "categoria_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategoria> subcategorias;

    //constructor
    public Categoria() {

    }
    public Categoria(String nombre, String descripcion){
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

}
