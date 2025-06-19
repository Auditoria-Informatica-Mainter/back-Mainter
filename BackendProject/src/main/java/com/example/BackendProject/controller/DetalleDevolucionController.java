package com.example.BackendProject.controller;

import com.example.BackendProject.dto.DetalleDevolucionDTO;
import com.example.BackendProject.entity.Detalle_Devolucion;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.DetalleDevolucionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle_devolucion")
@CrossOrigin(origins = "*")
public class DetalleDevolucionController {

    @Autowired
    private DetalleDevolucionService detalleDevolucionService;

    @Operation(summary = "Listar todos los detalles de devolución")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Detalle_Devolucion>>> listarDetalles() {
        return ResponseEntity.ok(
                ApiResponse.<List<Detalle_Devolucion>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles obtenidos")
                        .data(detalleDevolucionService.listarDetalles())
                        .build()
        );
    }

    @Operation(summary = "Obtener un detalle por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Detalle_Devolucion>> obtenerDetalle(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Detalle encontrado")
                            .data(detalleDevolucionService.obtenerDetalle(id))
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Crear un nuevo detalle de devolución")
    @PostMapping
    public ResponseEntity<ApiResponse<Detalle_Devolucion>> crearDetalle(@Valid @RequestBody DetalleDevolucionDTO dto) {
        try {
            Detalle_Devolucion nuevoDetalle = detalleDevolucionService.crearDetalle(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Detalle creado exitosamente")
                            .data(nuevoDetalle)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Actualizar un detalle de devolución")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Detalle_Devolucion>> actualizarDetalle(
            @PathVariable Long id,
            @Valid @RequestBody DetalleDevolucionDTO dto) {
        try {
            Detalle_Devolucion detalleActualizado = detalleDevolucionService.actualizarDetalle(id, dto);
            return ResponseEntity.ok(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Detalle actualizado exitosamente")
                            .data(detalleActualizado)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Eliminar un detalle de devolución")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDetalle(@PathVariable Long id) {
        try {
            detalleDevolucionService.eliminarDetalle(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Detalle eliminado exitosamente")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Listar detalles por devolución")
    @GetMapping("/devolucion/{devolucionId}")
    public ResponseEntity<ApiResponse<List<Detalle_Devolucion>>> listarDetallesPorDevolucion(@PathVariable Long devolucionId) {
        return ResponseEntity.ok(
                ApiResponse.<List<Detalle_Devolucion>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles de la devolución con ID: " + devolucionId)
                        .data(detalleDevolucionService.listarDetallesPorDevolucion(devolucionId))
                        .build()
        );
    }

    @Operation(summary = "Listar detalles por producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ApiResponse<List<Detalle_Devolucion>>> listarDetallesPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(
                ApiResponse.<List<Detalle_Devolucion>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles del producto con ID: " + productoId)
                        .data(detalleDevolucionService.listarDetallesPorProducto(productoId))
                        .build()
        );
    }
}