package com.example.BackendProject.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionDTO {
    
    private Date fecha;
    
    private String motivo;
    
    private String descripcion;
    
    @Min(value = 0, message = "El importe total no puede ser negativo")
    private Double importe_total;
    
    private Boolean estado;

    private Long usuario_id;

    private Long pedido_id;


}