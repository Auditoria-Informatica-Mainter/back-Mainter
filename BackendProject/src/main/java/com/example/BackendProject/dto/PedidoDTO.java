package com.example.BackendProject.dto;

import java.util.Date;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    
    private Date fecha;
    
    private String descripcion;
    
    @Min(value = 0, message = "El importe total no puede ser negativo")
    private Double importe_total;
    
    @Min(value = 0, message = "El importe total descuento no puede ser negativo")
    private Double importe_total_desc;
    
    private Boolean estado;

    private Long usuario_id;

    private Long metodo_pago_id;

}
