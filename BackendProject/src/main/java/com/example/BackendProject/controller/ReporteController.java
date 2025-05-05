package com.example.BackendProject.controller;

import com.example.BackendProject.dto.ReporteDTO;
import com.example.BackendProject.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Controlador para gestionar operaciones relacionadas con los reportes
 */
@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    /**
     * Obtiene todos los reportes
     * 
     * @return Lista de todos los reportes
     */
    @GetMapping
    public ResponseEntity<List<ReporteDTO>> obtenerTodosLosReportes() {
        return ResponseEntity.ok(reporteService.getAllReportesDTO());
    }

    /**
     * Obtiene un reporte por su ID
     * 
     * @param id ID del reporte
     * @return Reporte encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtenerReportePorId(@PathVariable Long id) {
        ReporteDTO reporte = reporteService.getReporteDTOById(id);
        if (reporte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reporte);
    }

    /**
     * Genera un reporte de stock para un producto específico
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param productoId ID del producto para el reporte
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return Reporte generado con información de stock
     */
    @GetMapping("/stock/producto/{productoId}")
    public ResponseEntity<ReporteDTO> generarReporteStockProducto(
            @RequestParam Long userId,
            @PathVariable Long productoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin) {
        
        try {
            ReporteDTO reporte = reporteService.generateStockReportDTO(userId, productoId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Genera un reporte de stock para todos los productos
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return Reporte generado con información de stock de todos los productos
     */
    @GetMapping("/stock/productos")
    public ResponseEntity<ReporteDTO> generarReporteStockTodosProductos(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin) {
        
        try {
            ReporteDTO reporte = reporteService.stockAllProducts(userId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Genera un reporte de stock para un material específico por categoría y subcategoría
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param materialId ID del material para el reporte
     * @param categoriaId ID de la categoría (opcional)
     * @param subcategoriaId ID de la subcategoría (opcional)
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return Reporte generado con información de stock
     */
    @GetMapping("/stock/material/{materialId}")
    public ResponseEntity<ReporteDTO> generarReporteStockMaterial(
            @RequestParam Long userId,
            @PathVariable Long materialId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long subcategoriaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin) {
        
        try {
            ReporteDTO reporte = reporteService.stockByMaterialCategoriaSubcategoria(
                    userId, materialId, categoriaId, subcategoriaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Genera un reporte de stock para todos los materiales de una subcategoría
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param subcategoriaId ID de la subcategoría
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return Reporte generado con información de stock
     */
    @GetMapping("/stock/subcategoria/{subcategoriaId}/materiales")
    public ResponseEntity<ReporteDTO> generarReporteStockMaterialesPorSubcategoria(
            @RequestParam Long userId,
            @PathVariable Long subcategoriaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin) {
        
        try {
            ReporteDTO reporte = reporteService.stockMaterialesBySubcategoria(
                    userId, subcategoriaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Genera un reporte de stock para todos los materiales de todas las subcategorías
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return Reporte generado con información de stock
     */
    @GetMapping("/stock/subcategorias/materiales")
    public ResponseEntity<ReporteDTO> generarReporteStockTodasSubcategorias(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin) {
        
        try {
            ReporteDTO reporte = reporteService.stockAllSubcategorias(userId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
} 