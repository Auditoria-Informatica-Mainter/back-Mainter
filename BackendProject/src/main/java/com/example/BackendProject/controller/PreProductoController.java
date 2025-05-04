package com.example.BackendProject.controller;

import com.example.BackendProject.dto.PreProductoDTO;
import com.example.BackendProject.entity.Pre_producto;
import com.example.BackendProject.service.PreProductoService;
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
@RequestMapping("/api/preproductos")
@Tag(name = "PreProductos", description = "API para gestión de pre-productos")
public class PreProductoController {
    
    @Autowired
    private PreProductoService preProductoService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Crear nuevo pre-producto",
            description = "Crea un nuevo pre-producto en el sistema",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pre-producto creado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Pre_producto> crearPreProducto(@Valid @RequestBody PreProductoDTO preProductoDTO) {
        Pre_producto nuevoPreProducto = preProductoService.crearPreProducto(preProductoDTO);
        return new ResponseEntity<>(nuevoPreProducto, HttpStatus.CREATED);
    }
    
    @GetMapping
    @Operation(
            summary = "Obtener todos los pre-productos",
            description = "Retorna la lista de todos los pre-productos registrados"
    )
    public ResponseEntity<List<Pre_producto>> obtenerTodosLosPreProductos() {
        List<Pre_producto> preProductos = preProductoService.obtenerTodosLosPreProductos();
        return ResponseEntity.ok(preProductos);
    }
    
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener pre-producto por ID",
            description = "Retorna un pre-producto específico por su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pre-producto encontrado"),
                    @ApiResponse(responseCode = "404", description = "Pre-producto no encontrado")
            }
    )
    public ResponseEntity<Pre_producto> obtenerPreProductoPorId(@PathVariable Long id) {
        try {
            Pre_producto preProducto = preProductoService.obtenerPreProductoPorId(id);
            return ResponseEntity.ok(preProducto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Actualizar pre-producto",
            description = "Actualiza la información de un pre-producto existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pre-producto actualizado"),
                    @ApiResponse(responseCode = "404", description = "Pre-producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Pre_producto> actualizarPreProducto(@PathVariable Long id, @Valid @RequestBody PreProductoDTO preProductoDTO) {
        try {
            Pre_producto preProductoActualizado = preProductoService.actualizarPreProducto(id, preProductoDTO);
            return ResponseEntity.ok(preProductoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Eliminar pre-producto",
            description = "Elimina un pre-producto del sistema",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pre-producto eliminado"),
                    @ApiResponse(responseCode = "404", description = "Pre-producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Void> eliminarPreProducto(@PathVariable Long id) {
        try {
            preProductoService.eliminarPreProducto(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}