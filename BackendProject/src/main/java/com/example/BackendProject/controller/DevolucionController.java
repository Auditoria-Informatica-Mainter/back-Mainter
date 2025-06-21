package com.example.BackendProject.controller;

import com.example.BackendProject.dto.DetalleDevolucionDTO;
import com.example.BackendProject.dto.DevolucionDTO;
import com.example.BackendProject.entity.Detalle_Devolucion;
import com.example.BackendProject.entity.Devolucion;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.DetalleDevolucionService;
import com.example.BackendProject.service.DevolucionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/devoluciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DevolucionController {

    private final DevolucionService devolucionService;
    private final DetalleDevolucionService detalleDevolucionService;

    // --- Endpoints para Devoluciones ---

    @Operation(summary = "Crear una nueva devolución con detalles")
    @PostMapping
    public ResponseEntity<ApiResponse<Devolucion>> crearDevolucion(@RequestBody DevolucionDTO devolucionDTO) {
        try {
            Devolucion nuevaDevolucion = devolucionService.crearDevolucion(devolucionDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Devolución creada exitosamente")
                            .data(nuevaDevolucion)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @Operation(summary = "Obtener todas las devoluciones")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Devolucion>>> obtenerTodasLasDevoluciones() {
        List<Devolucion> devoluciones = devolucionService.listarDevoluciones();
        return new ResponseEntity<>(
                ApiResponse.<List<Devolucion>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de todas las devoluciones")
                        .data(devoluciones)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener una devolución por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Devolucion>> obtenerDevolucionPorId(@PathVariable Long id) {
        try {
            Devolucion devolucion = devolucionService.obtenerDevolucion(id);
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Devolución encontrada")
                            .data(devolucion)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @Operation(summary = "Actualizar una devolución existente y sus detalles")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Devolucion>> actualizarDevolucion(@PathVariable Long id, @RequestBody DevolucionDTO devolucionDTO) {
        try {
            Devolucion devolucionActualizada = devolucionService.actualizarDevolucion(id, devolucionDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Devolución actualizada exitosamente")
                            .data(devolucionActualizada)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @Operation(summary = "Eliminar una devolución y sus detalles")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDevolucion(@PathVariable Long id) {
        try {
            devolucionService.eliminarDevolucion(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Devolución eliminada exitosamente")
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    // --- Endpoints para Detalles de Devolución como sub-recurso ---

    @Operation(summary = "Añadir un nuevo detalle a una devolución existente")
    @PostMapping("/{devolucionId}/detalles")
    public ResponseEntity<ApiResponse<Detalle_Devolucion>> crearDetalleParaDevolucion(@PathVariable Long devolucionId, @RequestBody DetalleDevolucionDTO detalleDto) {
        try {
            Detalle_Devolucion nuevoDetalle = detalleDevolucionService.crearDetalle(devolucionId, detalleDto);
            return new ResponseEntity<>(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Detalle añadido a la devolución " + devolucionId)
                            .data(nuevoDetalle)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Detalle_Devolucion>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @Operation(summary = "Eliminar un detalle de devolución específico")
    @DeleteMapping("/{devolucionId}/detalles/{detalleId}")
    public ResponseEntity<ApiResponse<Void>> eliminarDetalle(@PathVariable Long devolucionId, @PathVariable Long detalleId) {
        try {
            detalleDevolucionService.eliminarDetalle(devolucionId, detalleId);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Detalle de devolución eliminado exitosamente")
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @Operation(summary = "Obtener devoluciones por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Devolucion>>> obtenerDevolucionesPorUsuario(@PathVariable Long usuarioId) {
        List<Devolucion> devoluciones = devolucionService.listarDevolucionesPorUsuario(usuarioId);
        return new ResponseEntity<>(
                ApiResponse.<List<Devolucion>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Devoluciones del usuario con ID: " + usuarioId)
                        .data(devoluciones)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener devoluciones por pedido")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<ApiResponse<List<Devolucion>>> obtenerDevolucionesPorPedido(@PathVariable Long pedidoId) {
        List<Devolucion> devoluciones = devolucionService.listarDevolucionesPorPedido(pedidoId);
        return new ResponseEntity<>(
                ApiResponse.<List<Devolucion>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Devoluciones del pedido con ID: " + pedidoId)
                        .data(devoluciones)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Procesar una devolución")
    @PostMapping("/{id}/procesar")
    public ResponseEntity<ApiResponse<Devolucion>> procesarDevolucion(@PathVariable Long id) {
        try {
            Devolucion devolucionProcesada = devolucionService.procesarDevolucion(id);
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Devolución procesada exitosamente")
                            .data(devolucionProcesada)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Devolucion>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }
}