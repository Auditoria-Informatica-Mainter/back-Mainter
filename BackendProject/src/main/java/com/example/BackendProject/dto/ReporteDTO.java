package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO para transferir información de reportes al cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO {
    private Long id;
    private Date fechaInicio;
    private Date fechaFin;
    private String descripcion;
    
    // Información resumida del usuario que solicitó el reporte
    private Long usuarioId;
    private String nombreUsuario;
    private String apellidoUsuario;
    
    // Información resumida del producto (si aplica)
    private Long productoId;
    private String nombreProducto;
    private Integer stockActual;
    private Integer stockMinimo;
    
    // Información resumida del material (si aplica)
    private Long materialId;
    private String nombreMaterial;
    
    // Información resumida de la compra (si aplica)
    private Long compraId;
    private String estadoCompra;
    
    // Campos calculados o adicionales para el reporte de stock
    private Boolean stockBajoMinimo;
    private Boolean materialesSuficientes;
    private Integer produccionPosible;
    
    // Campos para reportes agrupados o estadísticos
    private Double valorTotalInventario;
    private Integer totalProductosBajoStock;
}
