package com.example.BackendProject.controller;


import com.example.BackendProject.dto.MaquinariaDTO;
import com.example.BackendProject.entity.Maquinaria;
import com.example.BackendProject.entity.MaquinariaCarpintero;
import com.example.BackendProject.service.MaquinariaService;
import com.example.BackendProject.service.MaquinariaCarpinteroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maquinarias")
public class MaquinariaController {

    @Autowired
    private MaquinariaService maquinariaService;

    @Autowired
    private MaquinariaCarpinteroService maquinariaCarpinteroService;

    @PostMapping
    public ResponseEntity<Maquinaria> createMaquinaria(@RequestBody MaquinariaDTO maquinariaDTO) {
        Maquinaria nuevaMaquinaria = maquinariaService.createMaquinaria(maquinariaDTO);
        return ResponseEntity.ok(nuevaMaquinaria);
    }

    @GetMapping
    public ResponseEntity<List<Maquinaria>> getAllMaquinarias() {
        List<Maquinaria> maquinarias = maquinariaService.getAllMaquinarias();
        return ResponseEntity.ok(maquinarias);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Maquinaria>> getMaquinariasByEstado(@PathVariable String estado) {
        List<Maquinaria> maquinarias = maquinariaService.getMaquinariasByEstado(estado);
        return ResponseEntity.ok(maquinarias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maquinaria> updateMaquinaria(
            @PathVariable Long id,
            @RequestBody MaquinariaDTO maquinariaDTO) {
        Maquinaria maquinariaActualizada = maquinariaService.updateMaquinaria(id, maquinariaDTO);
        return ResponseEntity.ok(maquinariaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaquinaria(@PathVariable Long id) {
        maquinariaService.deleteMaquinaria(id);
        return ResponseEntity.noContent().build();
    }

    // NUEVOS ENDPOINTS INTEGRADOS CON ASIGNACIONES

    // Obtener maquinaria con información de asignaciones
    @GetMapping("/{id}/con-asignaciones")
    public ResponseEntity<Map<String, Object>> getMaquinariaConAsignaciones(@PathVariable Long id) {
        Maquinaria maquinaria = maquinariaService.getAllMaquinarias().stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada"));
        
        List<MaquinariaCarpintero> asignaciones = maquinariaCarpinteroService.getAsignacionesPorMaquinaria(id);
        boolean disponible = maquinariaCarpinteroService.isMaquinariaDisponible(id);
        
        return ResponseEntity.ok(Map.of(
            "maquinaria", maquinaria,
            "asignaciones", asignaciones,
            "disponible", disponible
        ));
    }

    // Obtener todas las maquinarias disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Map<String, Object>>> getMaquinariasDisponibles() {
        List<Maquinaria> todasMaquinarias = maquinariaService.getAllMaquinarias();
        List<Map<String, Object>> maquinariasConEstado = todasMaquinarias.stream()
            .map(maquinaria -> {
                boolean disponible = maquinariaCarpinteroService.isMaquinariaDisponible(maquinaria.getId());
                return Map.of(
                    "maquinaria", maquinaria,
                    "disponible", disponible
                );
            })
            .toList();
        
        return ResponseEntity.ok(maquinariasConEstado);
    }

    // Asignar carpintero directamente desde maquinaria
    @PostMapping("/{maquinariaId}/asignar-carpintero/{carpinteroId}")
    public ResponseEntity<MaquinariaCarpintero> asignarCarpintero(
            @PathVariable Long maquinariaId,
            @PathVariable Long carpinteroId,
            @RequestParam(defaultValue = "en_uso") String estado) {
        try {
            MaquinariaCarpintero asignacion = maquinariaCarpinteroService.asignarCarpinteroAMaquinaria(
                maquinariaId, carpinteroId, estado);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Liberar maquinaria directamente
    @PutMapping("/{maquinariaId}/liberar")
    public ResponseEntity<MaquinariaCarpintero> liberarMaquinaria(@PathVariable Long maquinariaId) {
        try {
            MaquinariaCarpintero asignacion = maquinariaCarpinteroService.liberarMaquinaria(maquinariaId);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}