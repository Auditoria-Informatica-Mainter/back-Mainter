package com.example.BackendProject.controller;

import com.example.BackendProject.dto.MaquinariaCarpinteroDTO;
import com.example.BackendProject.entity.MaquinariaCarpintero;
import com.example.BackendProject.service.MaquinariaCarpinteroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaciones-maquinaria")
public class MaquinariaCarpinteroController {

    @Autowired
    private MaquinariaCarpinteroService maquinariaCarpinteroService;

    // Obtener todas las asignaciones
    @GetMapping
    public ResponseEntity<List<MaquinariaCarpintero>> getAllAsignaciones() {
        List<MaquinariaCarpintero> asignaciones = maquinariaCarpinteroService.getAllAsignaciones();
        return ResponseEntity.ok(asignaciones);
    }

    // Asignar carpintero a maquinaria
    @PostMapping("/asignar")
    public ResponseEntity<MaquinariaCarpintero> asignarCarpinteroAMaquinaria(
            @RequestParam Long maquinariaId,
            @RequestParam Long carpinteroId,
            @RequestParam(defaultValue = "en_uso") String estado) {
        try {
            MaquinariaCarpintero asignacion = maquinariaCarpinteroService.asignarCarpinteroAMaquinaria(
                maquinariaId, carpinteroId, estado);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Liberar maquinaria
    @PutMapping("/liberar/{maquinariaId}")
    public ResponseEntity<MaquinariaCarpintero> liberarMaquinaria(@PathVariable Long maquinariaId) {
        try {
            MaquinariaCarpintero asignacion = maquinariaCarpinteroService.liberarMaquinaria(maquinariaId);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cambiar estado de asignación
    @PutMapping("/{asignacionId}/estado")
    public ResponseEntity<MaquinariaCarpintero> cambiarEstadoAsignacion(
            @PathVariable Long asignacionId,
            @RequestParam String nuevoEstado) {
        try {
            MaquinariaCarpintero asignacion = maquinariaCarpinteroService.cambiarEstadoAsignacion(
                asignacionId, nuevoEstado);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener asignaciones por maquinaria
    @GetMapping("/maquinaria/{maquinariaId}")
    public ResponseEntity<List<MaquinariaCarpintero>> getAsignacionesPorMaquinaria(
            @PathVariable Long maquinariaId) {
        List<MaquinariaCarpintero> asignaciones = maquinariaCarpinteroService.getAsignacionesPorMaquinaria(maquinariaId);
        return ResponseEntity.ok(asignaciones);
    }

    // Obtener asignaciones por carpintero
    @GetMapping("/carpintero/{carpinteroId}")
    public ResponseEntity<List<MaquinariaCarpintero>> getAsignacionesPorCarpintero(
            @PathVariable Long carpinteroId) {
        List<MaquinariaCarpintero> asignaciones = maquinariaCarpinteroService.getAsignacionesPorCarpintero(carpinteroId);
        return ResponseEntity.ok(asignaciones);
    }

    // Obtener maquinarias en uso por carpintero
    @GetMapping("/carpintero/{carpinteroId}/en-uso")
    public ResponseEntity<List<MaquinariaCarpintero>> getMaquinariasEnUsoPorCarpintero(
            @PathVariable Long carpinteroId) {
        List<MaquinariaCarpintero> asignaciones = maquinariaCarpinteroService.getMaquinariasEnUsoPorCarpintero(carpinteroId);
        return ResponseEntity.ok(asignaciones);
    }

    // Obtener asignaciones por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<MaquinariaCarpintero>> getAsignacionesPorEstado(@PathVariable String estado) {
        List<MaquinariaCarpintero> asignaciones = maquinariaCarpinteroService.getAsignacionesPorEstado(estado);
        return ResponseEntity.ok(asignaciones);
    }

    // Verificar disponibilidad de maquinaria
    @GetMapping("/maquinaria/{maquinariaId}/disponible")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidad(@PathVariable Long maquinariaId) {
        boolean disponible = maquinariaCarpinteroService.isMaquinariaDisponible(maquinariaId);
        return ResponseEntity.ok(Map.of("disponible", disponible));
    }

    // Actualizar asignación completa
    @PutMapping("/{asignacionId}")
    public ResponseEntity<MaquinariaCarpintero> actualizarAsignacion(
            @PathVariable Long asignacionId,
            @RequestBody MaquinariaCarpinteroDTO dto) {
        try {
            MaquinariaCarpintero asignacion = maquinariaCarpinteroService.actualizarAsignacion(asignacionId, dto);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar asignación
    @DeleteMapping("/{asignacionId}")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long asignacionId) {
        try {
            maquinariaCarpinteroService.eliminarAsignacion(asignacionId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint combinado para obtener información completa de maquinaria
    @GetMapping("/maquinaria/{maquinariaId}/info-completa")
    public ResponseEntity<Map<String, Object>> getInfoCompletaMaquinaria(@PathVariable Long maquinariaId) {
        List<MaquinariaCarpintero> asignaciones = maquinariaCarpinteroService.getAsignacionesPorMaquinaria(maquinariaId);
        boolean disponible = maquinariaCarpinteroService.isMaquinariaDisponible(maquinariaId);
        
        return ResponseEntity.ok(Map.of(
            "asignaciones", asignaciones,
            "disponible", disponible,
            "totalAsignaciones", asignaciones.size()
        ));
    }

    // Endpoint para obtener resumen por carpintero
    @GetMapping("/carpintero/{carpinteroId}/resumen")
    public ResponseEntity<Map<String, Object>> getResumenCarpintero(@PathVariable Long carpinteroId) {
        List<MaquinariaCarpintero> todasAsignaciones = maquinariaCarpinteroService.getAsignacionesPorCarpintero(carpinteroId);
        List<MaquinariaCarpintero> enUso = maquinariaCarpinteroService.getMaquinariasEnUsoPorCarpintero(carpinteroId);
        
        return ResponseEntity.ok(Map.of(
            "todasAsignaciones", todasAsignaciones,
            "maquinariasEnUso", enUso,
            "totalMaquinariasEnUso", enUso.size(),
            "totalAsignaciones", todasAsignaciones.size()
        ));
    }
} 