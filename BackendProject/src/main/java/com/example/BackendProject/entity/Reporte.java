package com.example.BackendProject.entity;
//clase no reflejada en el diagrama, pero necesaria para el programa

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.BackendProject.entity.Usuario;

import java.util.Date;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_Inicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_Fin;

    @Column(length = 4000)
    private String descripcion;

    @ManyToOne
    private Usuario solictado_por;

    @ManyToOne
    private Compra compra;

    @ManyToOne
    private Material material;

    @ManyToOne
    private Producto producto;
}
