package com.example.BackendProject.controller;

import com.example.BackendProject.dto.DetallePedidoCompraDTO;
import com.example.BackendProject.entity.DetallePedidoCompra;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.DetallePedidoCompraService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para la gestión de detalles de pedidos de compra
 */
@RestController
@RequestMapping("/api/compras-detalle")
@CrossOrigin(origins = "*")
public class DetallePedidoCompraController {
    
    private final DetallePedidoCompraService detallePedidoCompraService;
    
    @Autowired
    public DetallePedidoCompraController(DetallePedidoCompraService detallePedidoCompraService) {
        this.detallePedidoCompraService = detallePedidoCompraService;
    }
    
    @Operation(summary = "Obtener todos los detalles de pedidos de compra")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DetallePedidoCompra>>> obtenerTodosLosDetallesPedidos() {
        List<DetallePedidoCompra> detalles = detallePedidoCompraService.listarDetallesPedidos();
        return new ResponseEntity<>(
                ApiResponse.<List<DetallePedidoCompra>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de detalles de pedidos de compra")
                        .data(detalles)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener un detalle de pedido de compra por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DetallePedidoCompra>> obtenerDetallePedidoPorId(@PathVariable Long id) {
        try {
            DetallePedidoCompra detalle = detallePedidoCompraService.obtenerDetallePedido(id);
            return new ResponseEntity<>(
                    ApiResponse.<DetallePedidoCompra>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Detalle de pedido de compra encontrado")
                            .data(detalle)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<DetallePedidoCompra>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Detalle de pedido de compra no encontrado con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Obtener detalles de pedido por compra principal")
    @GetMapping("/compra/{compraId}")
    public ResponseEntity<ApiResponse<List<DetallePedidoCompra>>> obtenerDetallesPorCompra(@PathVariable Long compraId) {
        List<DetallePedidoCompra> detalles = detallePedidoCompraService.obtenerDetallesPorCompra(compraId);
        return new ResponseEntity<>(
                ApiResponse.<List<DetallePedidoCompra>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles de la compra ID: " + compraId)
                        .data(detalles)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Crear un nuevo detalle de pedido de compra")
    @PostMapping
    public ResponseEntity<ApiResponse<DetallePedidoCompra>> crearDetallePedido(@RequestBody DetallePedidoCompraDTO detallePedidoDTO) {
        try {
            DetallePedidoCompra nuevoDetalle = detallePedidoCompraService.crearDetallePedido(detallePedidoDTO);
            return new ResponseEntity<>(
                    ApiResponse.<DetallePedidoCompra>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Detalle de pedido de compra creado exitosamente")
                            .data(nuevoDetalle)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<DetallePedidoCompra>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar un detalle de pedido de compra existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DetallePedidoCompra>> actualizarDetallePedido(@PathVariable Long id, @RequestBody DetallePedidoCompraDTO detallePedidoDTO) {
        try {
            DetallePedidoCompra detalleActualizado = detallePedidoCompraService.actualizarDetallePedido(id, detallePedidoDTO);
            return new ResponseEntity<>(
                    ApiResponse.<DetallePedidoCompra>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Detalle de pedido de compra actualizado exitosamente")
                            .data(detalleActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<DetallePedidoCompra>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Eliminar un detalle de pedido de compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDetallePedido(@PathVariable Long id) {
        try {
            detallePedidoCompraService.eliminarDetallePedido(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Detalle de pedido de compra eliminado exitosamente")
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
    
    @Operation(summary = "Obtener detalles de pedido por material")
    @GetMapping("/material/{materialId}")
    public ResponseEntity<ApiResponse<List<DetallePedidoCompra>>> obtenerDetallesPorMaterial(@PathVariable Long materialId) {
        List<DetallePedidoCompra> detalles = detallePedidoCompraService.obtenerDetallesPorMaterial(materialId);
        return new ResponseEntity<>(
                ApiResponse.<List<DetallePedidoCompra>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles con material ID: " + materialId)
                        .data(detalles)
                        .build(),
                HttpStatus.OK
        );
    }
} 