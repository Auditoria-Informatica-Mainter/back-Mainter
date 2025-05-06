package com.example.BackendProject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la entidad DetallePedidoCompra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoCompraDTO {
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("cantidad")
    private Integer cantidad;
    
    @JsonProperty("estado")
    private String estado;
    
    @JsonProperty("importe")
    private Double importe;
    
    @JsonProperty("importe_desc")
    private Double importe_desc;
    
    @JsonProperty("precio")
    private Double precio;
    
    @JsonProperty("compraId")
    private Long compraId;
    
    @JsonProperty("materialId")
    private Long materialId;
} 