package com.example.BackendProject.controller;

import com.example.BackendProject.dto.PrePlanoDTO;
import com.example.BackendProject.entity.Pre_plano;
import com.example.BackendProject.service.PrePlanoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preplanos")
@Tag(name = "PrePlanos", description = "API para gestión de pre-planos (relación entre pre-productos y materiales)")
public class PrePlanoController {
    
    @Autowired
    private PrePlanoService prePlanoService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Crear nuevo pre-plano",
            description = "Asocia un material a un pre-producto con cantidad y tiempo estimado",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pre-plano creado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Pre-producto o material no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Pre_plano> crearPrePlano(@Valid @RequestBody PrePlanoDTO prePlanoDTO) {
        try {
            Pre_plano nuevoPrePlano = prePlanoService.crearPrePlano(prePlanoDTO);
            return new ResponseEntity<>(nuevoPrePlano, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(
            summary = "Obtener todos los pre-planos",
            description = "Retorna la lista de todas las relaciones entre pre-productos y materiales"
    )
    public ResponseEntity<List<Pre_plano>> obtenerTodosLosPrePlanos() {
        List<Pre_plano> prePlanos = prePlanoService.obtenerTodosLosPrePlanos();
        return ResponseEntity.ok(prePlanos);
    }
    
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener pre-plano por ID",
            description = "Retorna un pre-plano específico por su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pre-plano encontrado"),
                    @ApiResponse(responseCode = "404", description = "Pre-plano no encontrado")
            }
    )
    public ResponseEntity<Pre_plano> obtenerPrePlanoPorId(@PathVariable Long id) {
        try {
            Pre_plano prePlano = prePlanoService.obtenerPrePlanoPorId(id);
            return ResponseEntity.ok(prePlano);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/preproducto/{preProductoId}")
    @Operation(
            summary = "Obtener pre-planos por pre-producto",
            description = "Retorna la lista de pre-planos asociados a un pre-producto"
    )
    public ResponseEntity<List<Pre_plano>> obtenerPrePlanosPorPreProducto(@PathVariable Long preProductoId) {
        List<Pre_plano> prePlanos = prePlanoService.obtenerPrePlanosPorPreProducto(preProductoId);
        return ResponseEntity.ok(prePlanos);
    }
    
    @GetMapping("/material/{materialId}")
    @Operation(
            summary = "Obtener pre-planos por material",
            description = "Retorna la lista de pre-planos que utilizan un material específico"
    )
    public ResponseEntity<List<Pre_plano>> obtenerPrePlanosPorMaterial(@PathVariable Long materialId) {
        List<Pre_plano> prePlanos = prePlanoService.obtenerPrePlanosPorMaterial(materialId);
        return ResponseEntity.ok(prePlanos);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Actualizar pre-plano",
            description = "Actualiza la información de un pre-plano existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pre-plano actualizado"),
                    @ApiResponse(responseCode = "404", description = "Pre-plano, pre-producto o material no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Pre_plano> actualizarPrePlano(@PathVariable Long id, @Valid @RequestBody PrePlanoDTO prePlanoDTO) {
        try {
            Pre_plano prePlanoActualizado = prePlanoService.actualizarPrePlano(id, prePlanoDTO);
            return ResponseEntity.ok(prePlanoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Eliminar pre-plano",
            description = "Elimina un pre-plano del sistema",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pre-plano eliminado"),
                    @ApiResponse(responseCode = "404", description = "Pre-plano no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Void> eliminarPrePlano(@PathVariable Long id) {
        try {
            prePlanoService.eliminarPrePlano(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}