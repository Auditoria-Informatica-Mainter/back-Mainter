package com.example.BackendProject.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pre_maquinarias")
public class PreMaquinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private String descripcion;
    private String tiempoEstimado;

    @ManyToOne
    @JoinColumn(name = "maquinaria_id")
    private Maquinaria maquinaria;

    // RELACIÓN FALTANTE CON PRE_PRODUCTO
    @ManyToOne
    @JoinColumn(name = "pre_producto_id")
    private Pre_producto preProducto;

    // Constructor para crear nueva pre-maquinaria
    public PreMaquinaria(Integer cantidad, String descripcion, String tiempoEstimado, 
                        Maquinaria maquinaria, Pre_producto preProducto) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.tiempoEstimado = tiempoEstimado;
        this.maquinaria = maquinaria;
        this.preProducto = preProducto;
    }
}