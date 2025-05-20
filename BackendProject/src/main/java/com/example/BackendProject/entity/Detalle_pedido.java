package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detalle_pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @JsonBackReference
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @Builder.Default
    private Boolean Estado = false;

    @PositiveOrZero
    @Builder.Default
    private Double importe_Total = 0.0;
    
    @PositiveOrZero
    @Builder.Default
    private Double importe_Total_Desc = 0.0;

    private Double precioUnitario;

}
