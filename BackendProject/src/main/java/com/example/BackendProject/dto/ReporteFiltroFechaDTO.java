package com.example.BackendProject.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteFiltroFechaDTO {
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private Long proveedorId;
        private Long productoId;
        private Long materialId;
        private Long compraId;
    
    
}
