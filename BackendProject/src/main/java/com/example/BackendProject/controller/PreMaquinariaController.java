package com.example.BackendProject.controller;

import com.example.BackendProject.dto.PreMaquinariaDTO;
import com.example.BackendProject.entity.PreMaquinaria;
import com.example.BackendProject.service.PreMaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pre-maquinarias")
public class PreMaquinariaController {

    @Autowired
    private PreMaquinariaService preMaquinariaService;

    // Crear nueva planificación de maquinaria
    @PostMapping
    public ResponseEntity<PreMaquinaria> crearPreMaquinaria(@RequestBody PreMaquinariaDTO dto) {
        try {
            PreMaquinaria preMaquinaria = preMaquinariaService.crearPreMaquinaria(dto);
            return ResponseEntity.ok(preMaquinaria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener todas las planificaciones
    @GetMapping
    public ResponseEntity<List<PreMaquinaria>> getAllPreMaquinarias() {
        List<PreMaquinaria> preMaquinarias = preMaquinariaService.getAllPreMaquinarias();
        return ResponseEntity.ok(preMaquinarias);
    }

    // Obtener planificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<PreMaquinaria> getPreMaquinariaById(@PathVariable Long id) {
        try {
            PreMaquinaria preMaquinaria = preMaquinariaService.getAllPreMaquinarias().stream()
                .filter(pm -> pm.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pre-maquinaria no encontrada"));
            return ResponseEntity.ok(preMaquinaria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener planificaciones por pre-producto
    @GetMapping("/producto/{preProductoId}")
    public ResponseEntity<List<PreMaquinaria>> getPreMaquinariasPorProducto(@PathVariable Long preProductoId) {
        List<PreMaquinaria> preMaquinarias = preMaquinariaService.getPreMaquinariasPorProducto(preProductoId);
        return ResponseEntity.ok(preMaquinarias);
    }

    // Obtener planificaciones por maquinaria
    @GetMapping("/maquinaria/{maquinariaId}")
    public ResponseEntity<List<PreMaquinaria>> getPreMaquinariasPorMaquinaria(@PathVariable Long maquinariaId) {
        List<PreMaquinaria> preMaquinarias = preMaquinariaService.getPreMaquinariasPorMaquinaria(maquinariaId);
        return ResponseEntity.ok(preMaquinarias);
    }

    // Obtener maquinarias requeridas para un producto
    @GetMapping("/producto/{preProductoId}/maquinarias-requeridas")
    public ResponseEntity<List<PreMaquinaria>> getMaquinariasRequeridas(@PathVariable Long preProductoId) {
        List<PreMaquinaria> maquinariasRequeridas = preMaquinariaService.getMaquinariasRequeridas(preProductoId);
        return ResponseEntity.ok(maquinariasRequeridas);
    }

    // Obtener productos que requieren una maquinaria específica
    @GetMapping("/maquinaria/{maquinariaId}/productos-que-requieren")
    public ResponseEntity<List<PreMaquinaria>> getProductosQueRequierenMaquinaria(@PathVariable Long maquinariaId) {
        List<PreMaquinaria> productos = preMaquinariaService.getProductosQueRequierenMaquinaria(maquinariaId);
        return ResponseEntity.ok(productos);
    }

    // Calcular tiempo total estimado para un producto
    @GetMapping("/producto/{preProductoId}/tiempo-total")
    public ResponseEntity<Map<String, Integer>> calcularTiempoTotalEstimado(@PathVariable Long preProductoId) {
        Integer tiempoTotal = preMaquinariaService.calcularTiempoTotalEstimado(preProductoId);
        return ResponseEntity.ok(Map.of("tiempoTotalEstimado", tiempoTotal));
    }

    // Obtener resumen completo de planificación para un producto
    @GetMapping("/producto/{preProductoId}/resumen")
    public ResponseEntity<Map<String, Object>> getResumenPlanificacion(@PathVariable Long preProductoId) {
        Map<String, Object> resumen = preMaquinariaService.getResumenPlanificacion(preProductoId);
        return ResponseEntity.ok(resumen);
    }

    // Actualizar planificación
    @PutMapping("/{id}")
    public ResponseEntity<PreMaquinaria> actualizarPreMaquinaria(
            @PathVariable Long id,
            @RequestBody PreMaquinariaDTO dto) {
        try {
            PreMaquinaria preMaquinaria = preMaquinariaService.actualizarPreMaquinaria(id, dto);
            return ResponseEntity.ok(preMaquinaria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar planificación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPreMaquinaria(@PathVariable Long id) {
        try {
            preMaquinariaService.eliminarPreMaquinaria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Buscar por descripción
    @GetMapping("/buscar")
    public ResponseEntity<List<PreMaquinaria>> buscarPorDescripcion(@RequestParam String descripcion) {
        List<PreMaquinaria> resultados = preMaquinariaService.buscarPorDescripcion(descripcion);
        return ResponseEntity.ok(resultados);
    }

    // Verificar si existe planificación para una combinación
    @GetMapping("/verificar")
    public ResponseEntity<Map<String, Boolean>> verificarPlanificacion(
            @RequestParam Long preProductoId,
            @RequestParam Long maquinariaId) {
        boolean existe = preMaquinariaService.existePlanificacion(preProductoId, maquinariaId);
        return ResponseEntity.ok(Map.of("existe", existe));
    }

    // Duplicar planificación de un producto a otro
    @PostMapping("/duplicar")
    public ResponseEntity<List<PreMaquinaria>> duplicarPlanificacion(
            @RequestParam Long preProductoOrigenId,
            @RequestParam Long preProductoDestinoId) {
        try {
            List<PreMaquinaria> nuevasPlanificaciones = preMaquinariaService.duplicarPlanificacion(
                preProductoOrigenId, preProductoDestinoId);
            return ResponseEntity.ok(nuevasPlanificaciones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener estadísticas de uso de maquinarias
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticasUsoMaquinarias() {
        Map<String, Object> estadisticas = preMaquinariaService.getEstadisticasUsoMaquinarias();
        return ResponseEntity.ok(estadisticas);
    }

    // Endpoint para planificación rápida
    @PostMapping("/planificacion-rapida")
    public ResponseEntity<List<PreMaquinaria>> crearPlanificacionRapida(
            @RequestParam Long preProductoId,
            @RequestBody List<PreMaquinariaDTO> planificaciones) {
        try {
            List<PreMaquinaria> nuevasPlanificaciones = planificaciones.stream()
                .map(dto -> {
                    dto.setPreProductoId(preProductoId);
                    return preMaquinariaService.crearPreMaquinaria(dto);
                })
                .toList();
            return ResponseEntity.ok(nuevasPlanificaciones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para eliminar todas las planificaciones de un producto
    @DeleteMapping("/producto/{preProductoId}")
    public ResponseEntity<Void> eliminarPlanificacionesProducto(@PathVariable Long preProductoId) {
        try {
            List<PreMaquinaria> planificaciones = preMaquinariaService.getPreMaquinariasPorProducto(preProductoId);
            planificaciones.forEach(pm -> preMaquinariaService.eliminarPreMaquinaria(pm.getId()));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 