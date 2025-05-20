package com.example.BackendProject.controller;

import com.example.BackendProject.dto.DetallePedidoDTO;
import com.example.BackendProject.entity.Detalle_pedido;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.DetallePedidoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle_pedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Operation(summary = "Listar todos los detalles de pedido")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Detalle_pedido>>> listarDetalles() {
        return ResponseEntity.ok(
                ApiResponse.<List<Detalle_pedido>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles obtenidos")
                        .data(detallePedidoService.listarDetalles())
                        .build()
        );
    }

    @Operation(summary = "Obtener un detalle por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Detalle_pedido>> obtenerDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.<Detalle_pedido>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalle encontrado")
                        .data(detallePedidoService.obtenerDetalle(id))
                        .build()
        );
    }

    @Operation(summary = "Crear un nuevo detalle de pedido")
    @PostMapping
    public ResponseEntity<ApiResponse<Detalle_pedido>> crearDetalle(@RequestBody DetallePedidoDTO dto) {
        Detalle_pedido creado = detallePedidoService.crearDetalle(dto);
        return new ResponseEntity<>(
                ApiResponse.<Detalle_pedido>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Detalle creado")
                        .data(creado)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Actualizar un detalle de pedido")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Detalle_pedido>> actualizarDetalle(@PathVariable Long id,
                                                                          @Valid @RequestBody DetallePedidoDTO dto) {
        Detalle_pedido actualizado = detallePedidoService.actualizarDetalle(id, dto);
        return ResponseEntity.ok(
                ApiResponse.<Detalle_pedido>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalle actualizado")
                        .data(actualizado)
                        .build()
        );
    }

    @Operation(summary = "Actualizar un detalle de pedido")
    @PutMapping("/Estado/{id}/{estado}")
    public ResponseEntity<ApiResponse<Detalle_pedido>> actualizarDetalleEstado(@PathVariable Long id, 
                                                                            @PathVariable Boolean estado
    ) {
        Detalle_pedido actualizado = detallePedidoService.actualizarDetalleEstado(id, estado);
        return ResponseEntity.ok(
                ApiResponse.<Detalle_pedido>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalle actualizado")
                        .data(actualizado)
                        .build()
        );
    }

    @Operation(summary = "Eliminar un detalle de pedido")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDetalle(@PathVariable Long id) {
        detallePedidoService.eliminarDetalle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("Detalle eliminado")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Obtener detalles por ID de pedido")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<ApiResponse<List<Detalle_pedido>>> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(
                ApiResponse.<List<Detalle_pedido>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles por pedido ID: " + pedidoId)
                        .data(detallePedidoService.obtenerPorPedido(pedidoId))
                        .build()
        );
    }

    @Operation(summary = "Obtener detalles por ID de producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ApiResponse<List<Detalle_pedido>>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(
                ApiResponse.<List<Detalle_pedido>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles por producto ID: " + productoId)
                        .data(detallePedidoService.obtenerPorProducto(productoId))
                        .build()
        );
    }
}
