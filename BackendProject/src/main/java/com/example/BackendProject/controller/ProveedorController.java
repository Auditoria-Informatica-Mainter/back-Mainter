package com.example.BackendProject.controller;

import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de proveedores.
 * Proporciona endpoints para administrar proveedores y sus relaciones con materiales.
 * Incluye funcionalidades para búsqueda por diferentes criterios como nombre, ciudad y país,
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
    public ResponseEntity<ApiResponse<List<Proveedor>>> obtenerTodosLosProveedores() {
        List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();
        return new ResponseEntity<>(
                ApiResponse.<List<Proveedor>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de proveedores")
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener un proveedor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id)
                .map(proveedor -> new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Proveedor encontrado")
                                .data(proveedor)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Proveedor no encontrado con ID: " + id)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Obtener un proveedor por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<Proveedor>> obtenerProveedorPorNombre(@PathVariable String nombre) {
        return proveedorService.obtenerProveedorPorNombre(nombre)
                .map(proveedor -> new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Proveedor encontrado")
                                .data(proveedor)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Proveedor no encontrado con nombre: " + nombre)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Buscar proveedores por término")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Proveedor>>> buscarProveedoresPorNombre(@RequestParam("texto") String texto) {
        List<Proveedor> proveedores = proveedorService.buscarProveedoresPorNombre(texto);
        return new ResponseEntity<>(
                ApiResponse.<List<Proveedor>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Resultados de búsqueda para: '" + texto + "'")
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener proveedores por estado activo/inactivo")
    @GetMapping("/estado")
    public ResponseEntity<ApiResponse<List<Proveedor>>> obtenerProveedoresPorEstado(@RequestParam("activo") Boolean activo) {
        List<Proveedor> proveedores = proveedorService.obtenerProveedoresPorEstado(activo);
        return new ResponseEntity<>(
                ApiResponse.<List<Proveedor>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Proveedores con estado activo: " + activo)
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener proveedores por ciudad")
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<ApiResponse<List<Proveedor>>> obtenerProveedoresPorCiudad(@PathVariable String ciudad) {
        List<Proveedor> proveedores = proveedorService.obtenerProveedoresPorCiudad(ciudad);
        return new ResponseEntity<>(
                ApiResponse.<List<Proveedor>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Proveedores en: " + ciudad)
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener proveedores por país")
    @GetMapping("/pais/{pais}")
    public ResponseEntity<ApiResponse<List<Proveedor>>> obtenerProveedoresPorPais(@PathVariable String pais) {
        List<Proveedor> proveedores = proveedorService.obtenerProveedoresPorPais(pais);
        return new ResponseEntity<>(
                ApiResponse.<List<Proveedor>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Proveedores en: " + pais)
                        .data(proveedores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Crear un nuevo proveedor")
    @PostMapping
    public ResponseEntity<ApiResponse<Proveedor>> crearProveedor(@RequestBody Proveedor proveedor) {
        Proveedor nuevoProveedor = proveedorService.guardarProveedor(proveedor);
        return new ResponseEntity<>(
                ApiResponse.<Proveedor>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Proveedor creado exitosamente")
                        .data(nuevoProveedor)
                        .build(),
                HttpStatus.CREATED
        );
    }
    
    @Operation(summary = "Actualizar un proveedor existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return proveedorService.actualizarProveedor(id, proveedor)
                .map(proveedorActualizado -> new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Proveedor actualizado exitosamente")
                                .data(proveedorActualizado)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Proveedor no encontrado con ID: " + id)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Cambiar el estado de un proveedor")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Proveedor>> cambiarEstadoProveedor(@PathVariable Long id, @RequestParam("activo") Boolean activo) {
        return proveedorService.cambiarEstadoProveedor(id, activo)
                .map(proveedorActualizado -> new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Estado del proveedor actualizado")
                                .data(proveedorActualizado)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<Proveedor>builder()
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