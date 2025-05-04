package com.example.BackendProject.controller;

import com.example.BackendProject.dto.ProveedorDTO;
import com.example.BackendProject.dto.ProveedorConMaterialesDTO;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para la gestión de proveedores.
 * Proporciona endpoints para administrar proveedores y sus relaciones con materiales.
 * Incluye funcionalidades para búsqueda por diferentes criterios como nombre,
 * así como la gestión del estado activo/inactivo de los proveedores.
 */
@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {
    
    private final ProveedorService proveedorService;
    
    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }
    
    @Operation(summary = "Obtener todos los proveedores")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProveedorDTO>>> obtenerTodosLosProveedores() {
        List<ProveedorDTO> proveedores = proveedorService.obtenerTodosLosProveedores();
        return new ResponseEntity<>(
                ApiResponse.<List<ProveedorDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de proveedores")
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener un proveedor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProveedorDTO>> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id)
                .map(proveedor -> new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Proveedor encontrado")
                                .data(proveedor)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Proveedor no encontrado con ID: " + id)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Obtener un proveedor con sus materiales por ID")
    @GetMapping("/{id}/con-materiales")
    public ResponseEntity<ApiResponse<ProveedorConMaterialesDTO>> obtenerProveedorConMateriales(@PathVariable Long id) {
        try {
            ProveedorConMaterialesDTO proveedor = proveedorService.obtenerProveedorConMateriales(id);
            return new ResponseEntity<>(
                    ApiResponse.<ProveedorConMaterialesDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Proveedor con materiales encontrado")
                            .data(proveedor)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<ProveedorConMaterialesDTO>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Obtener un proveedor por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<ProveedorDTO>> obtenerProveedorPorNombre(@PathVariable String nombre) {
        return proveedorService.obtenerProveedorPorNombre(nombre)
                .map(proveedor -> new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Proveedor encontrado")
                                .data(proveedor)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Proveedor no encontrado con nombre: " + nombre)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Buscar proveedores por término")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ProveedorDTO>>> buscarProveedoresPorNombre(@RequestParam("texto") String texto) {
        List<ProveedorDTO> proveedores = proveedorService.buscarProveedoresPorNombre(texto);
        return new ResponseEntity<>(
                ApiResponse.<List<ProveedorDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Resultados de búsqueda para: '" + texto + "'")
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener proveedores por estado activo/inactivo")
    @GetMapping("/estado")
    public ResponseEntity<ApiResponse<List<ProveedorDTO>>> obtenerProveedoresPorEstado(@RequestParam("activo") Boolean activo) {
        List<ProveedorDTO> proveedores = proveedorService.obtenerProveedoresPorEstado(activo);
        return new ResponseEntity<>(
                ApiResponse.<List<ProveedorDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Proveedores con estado activo: " + activo)
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Crear un nuevo proveedor")
    @PostMapping
    public ResponseEntity<ApiResponse<ProveedorDTO>> crearProveedor(@RequestBody ProveedorDTO proveedorDTO) {
        ProveedorDTO nuevoProveedor = proveedorService.guardarProveedor(proveedorDTO);
        return new ResponseEntity<>(
                ApiResponse.<ProveedorDTO>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Proveedor creado exitosamente")
                        .data(nuevoProveedor)
                        .build(),
                HttpStatus.CREATED
        );
    }
    
    @Operation(summary = "Actualizar un proveedor existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProveedorDTO>> actualizarProveedor(@PathVariable Long id, @RequestBody ProveedorDTO proveedorDTO) {
        return proveedorService.actualizarProveedor(id, proveedorDTO)
                .map(proveedorActualizado -> new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Proveedor actualizado exitosamente")
                                .data(proveedorActualizado)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Proveedor no encontrado con ID: " + id)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Cambiar el estado de un proveedor")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<ProveedorDTO>> cambiarEstadoProveedor(@PathVariable Long id, @RequestParam("activo") Boolean activo) {
        return proveedorService.cambiarEstadoProveedor(id, activo)
                .map(proveedorActualizado -> new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Estado del proveedor actualizado")
                                .data(proveedorActualizado)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<ProveedorDTO>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Proveedor no encontrado con ID: " + id)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Eliminar un proveedor")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProveedor(@PathVariable Long id) {
        if (proveedorService.obtenerProveedorPorId(id).isPresent()) {
            proveedorService.eliminarProveedor(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Proveedor eliminado exitosamente")
                            .build(),
                    HttpStatus.NO_CONTENT
            );
        } else {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Proveedor no encontrado con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
} 