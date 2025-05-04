package com.example.BackendProject.controller;

import com.example.BackendProject.dto.CompraDTO;
import com.example.BackendProject.entity.Compra;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para la gestión de compras
 */
@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {
    
    private final CompraService compraService;
    
    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }
    
    @Operation(summary = "Obtener todas las compras")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Compra>>> obtenerTodasLasCompras() {
        List<Compra> compras = compraService.listarCompras();
        return new ResponseEntity<>(
                ApiResponse.<List<Compra>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de compras")
                        .data(compras)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener una compra por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Compra>> obtenerCompraPorId(@PathVariable Long id) {
        try {
            Compra compra = compraService.obtenerCompra(id);
            return new ResponseEntity<>(
                    ApiResponse.<Compra>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Compra encontrada")
                            .data(compra)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Compra>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Crear una nueva compra")
    @PostMapping
    public ResponseEntity<ApiResponse<Compra>> crearCompra(@RequestBody CompraDTO compraDTO) {
        try {
            Compra nuevaCompra = compraService.crearCompra(compraDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Compra>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Compra creada exitosamente")
                            .data(nuevaCompra)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Compra>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar una compra existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Compra>> actualizarCompra(@PathVariable Long id, @RequestBody CompraDTO compraDTO) {
        try {
            Compra compraActualizada = compraService.actualizarCompra(id, compraDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Compra>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Compra actualizada exitosamente")
                            .data(compraActualizada)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Compra>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Eliminar una compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCompra(@PathVariable Long id) {
        try {
            compraService.eliminarCompra(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Compra eliminada exitosamente")
                            .build(),
                    HttpStatus.NO_CONTENT
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Buscar compras por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<Compra>>> obtenerComprasPorEstado(@PathVariable String estado) {
        List<Compra> compras = compraService.buscarPorEstado(estado);
        return new ResponseEntity<>(
                ApiResponse.<List<Compra>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Compras con estado: " + estado)
                        .data(compras)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Buscar compras por proveedor")
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<ApiResponse<List<Compra>>> obtenerComprasPorProveedor(@PathVariable Long proveedorId) {
        List<Compra> compras = compraService.buscarPorProveedor(proveedorId);
        return new ResponseEntity<>(
                ApiResponse.<List<Compra>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Compras del proveedor ID: " + proveedorId)
                        .data(compras)
                        .build(),
                HttpStatus.OK
        );
    }
} 