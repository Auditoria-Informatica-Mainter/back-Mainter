package com.example.BackendProject.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {
    
    private Long id;
    private Date fecha;
    private String descripcion;
    private Double importe_total;
    private Double importe_total_desc;
    private Boolean estado;
    
    // Información del usuario
    private Long usuario_id;
    private String usuario_nombre;
    private String usuario_email;
    
    // Información del método de pago
    private Long metodo_pago_id;
    
    @JsonProperty("metodo_pago_nombre")
    private String metodo_pago_nombre;
    
    // También agregamos este campo alternativo por si el frontend busca por este nombre
    @JsonProperty("metodoPago")
    public String getMetodoPago() {
        return this.metodo_pago_nombre;
    }
    
    private String metodo_pago_descripcion;
}
