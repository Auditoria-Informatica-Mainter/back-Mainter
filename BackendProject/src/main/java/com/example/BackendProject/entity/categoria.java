package com.example.BackendProject.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorias")
public class categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<subCategoria> subcategorias;

    //constructor
    public categoria() {

    }
    public categoria(String nombre, String descripcion){
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

}
