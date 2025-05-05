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
    
    @Operation(summary = "Diagnosticar datos de detalle de pedido de compra")
    @PostMapping("/diagnostico")
    public ResponseEntity<ApiResponse<Object>> diagnosticarDetallePedido(@RequestBody DetallePedidoCompraDTO detallePedidoDTO) {
        // Crear un objeto que contenga todos los datos recibidos para diagnóstico
        java.util.Map<String, Object> diagnostico = new java.util.HashMap<>();
        diagnostico.put("dto_recibido", detallePedidoDTO);
        diagnostico.put("campos_no_nulos", new java.util.HashMap<String, Boolean>() {{
            put("id", detallePedidoDTO.getId() != null);
            put("cantidad", detallePedidoDTO.getCantidad() != null);
            put("estado", detallePedidoDTO.getEstado() != null);
            put("importe", detallePedidoDTO.getImporte() != null);
            put("importe_desc", detallePedidoDTO.getImporte_desc() != null);
            put("precio", detallePedidoDTO.getPrecio() != null);
            put("compraId", detallePedidoDTO.getCompraId() != null);
            put("materialId", detallePedidoDTO.getMaterialId() != null);
        }});
        
        // Añadir también diagnóstico de tipos
        diagnostico.put("tipos_dato", new java.util.HashMap<String, String>() {{
            put("id", detallePedidoDTO.getId() != null ? detallePedidoDTO.getId().getClass().getName() : "null");
            put("cantidad", detallePedidoDTO.getCantidad() != null ? detallePedidoDTO.getCantidad().getClass().getName() : "null");
            put("estado", detallePedidoDTO.getEstado() != null ? detallePedidoDTO.getEstado().getClass().getName() : "null");
            put("importe", detallePedidoDTO.getImporte() != null ? detallePedidoDTO.getImporte().getClass().getName() : "null");
            put("importe_desc", detallePedidoDTO.getImporte_desc() != null ? detallePedidoDTO.getImporte_desc().getClass().getName() : "null");
            put("precio", detallePedidoDTO.getPrecio() != null ? detallePedidoDTO.getPrecio().getClass().getName() : "null");
            put("compraId", detallePedidoDTO.getCompraId() != null ? detallePedidoDTO.getCompraId().getClass().getName() : "null");
            put("materialId", detallePedidoDTO.getMaterialId() != null ? detallePedidoDTO.getMaterialId().getClass().getName() : "null");
        }});
        
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Diagnóstico de los datos recibidos")
                        .data(diagnostico)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Actualizar valores NULL en registros existentes")
    @PostMapping("/actualizar-valores-null")
    public ResponseEntity<ApiResponse<Integer>> actualizarValoresNull() {
        int actualizados = detallePedidoCompraService.actualizarValoresNullEnRegistrosExistentes();
        return new ResponseEntity<>(
                ApiResponse.<Integer>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Registros actualizados: " + actualizados)
                        .data(actualizados)
                        .build(),
                HttpStatus.OK
        );
    }
} 