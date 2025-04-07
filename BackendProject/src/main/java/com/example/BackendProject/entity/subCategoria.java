package com.example.BackendProject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subcategorias")
public class subCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private categoria idcategoria;

    //Constructor
    public subCategoria(){}
    public subCategoria(String nombre, String descripcion){
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

}
