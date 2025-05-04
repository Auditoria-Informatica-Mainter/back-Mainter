package com.example.BackendProject.controller;

import com.example.BackendProject.dto.PlanoDTO;
import com.example.BackendProject.entity.Plano;
import com.example.BackendProject.service.PlanoService;
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
@RequestMapping("/api/planos")
@Tag(name = "Planos", description = "API para gestión de planos (relación entre productos y pre-productos)")
public class PlanoController {
    
    @Autowired
    private PlanoService planoService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Crear nuevo plano",
            description = "Asocia un pre-producto a un producto con cantidad y tiempo estimado",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Plano creado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Producto o pre-producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Plano> crearPlano(@Valid @RequestBody PlanoDTO planoDTO) {
        try {
            Plano nuevoPlano = planoService.crearPlano(planoDTO);
            return new ResponseEntity<>(nuevoPlano, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(
            summary = "Obtener todos los planos",
            description = "Retorna la lista de todas las relaciones entre productos y pre-productos"
    )
    public ResponseEntity<List<Plano>> obtenerTodosLosPlanos() {
        List<Plano> planos = planoService.obtenerTodosLosPlanos();
        return ResponseEntity.ok(planos);
    }
    
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener plano por ID",
            description = "Retorna un plano específico por su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plano encontrado"),
                    @ApiResponse(responseCode = "404", description = "Plano no encontrado")
            }
    )
    public ResponseEntity<Plano> obtenerPlanoPorId(@PathVariable Long id) {
        try {
            Plano plano = planoService.obtenerPlanoPorId(id);
            return ResponseEntity.ok(plano);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/producto/{productoId}")
    @Operation(
            summary = "Obtener planos por producto",
            description = "Retorna la lista de planos asociados a un producto"
    )
    public ResponseEntity<List<Plano>> obtenerPlanosPorProducto(@PathVariable Long productoId) {
        List<Plano> planos = planoService.obtenerPlanosPorProducto(productoId);
        return ResponseEntity.ok(planos);
    }
    
    @GetMapping("/preproducto/{preProductoId}")
    @Operation(
            summary = "Obtener planos por pre-producto",
            description = "Retorna la lista de planos que utilizan un pre-producto específico"
    )
    public ResponseEntity<List<Plano>> obtenerPlanosPorPreProducto(@PathVariable Long preProductoId) {
        List<Plano> planos = planoService.obtenerPlanosPorPreProducto(preProductoId);
        return ResponseEntity.ok(planos);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Actualizar plano",
            description = "Actualiza la información de un plano existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plano actualizado"),
                    @ApiResponse(responseCode = "404", description = "Plano, producto o pre-producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Plano> actualizarPlano(@PathVariable Long id, @Valid @RequestBody PlanoDTO planoDTO) {
        try {
            Plano planoActualizado = planoService.actualizarPlano(id, planoDTO);
            return ResponseEntity.ok(planoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Eliminar plano",
            description = "Elimina un plano del sistema",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Plano eliminado"),
                    @ApiResponse(responseCode = "404", description = "Plano no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Void> eliminarPlano(@PathVariable Long id) {
        try {
            planoService.eliminarPlano(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}