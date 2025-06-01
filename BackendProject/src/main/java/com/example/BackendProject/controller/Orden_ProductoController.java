package com.example.BackendProject.controller;

import com.example.BackendProject.dto.OrdenProductoDTO;
import com.example.BackendProject.entity.Orden_Producto;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.Orden_ProductoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orden-producto")
public class Orden_ProductoController {

    @Autowired
    private Orden_ProductoService ordenProductoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Orden_Producto>>> getAll() {
        List<Orden_Producto> ordenes = ordenProductoService.findAll();
        return ResponseEntity.ok(ApiResponse.<List<Orden_Producto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Lista de ordenes de producto")
                .data(ordenes)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Orden_Producto>> getById(@PathVariable Long id) {
        return ordenProductoService.findById(id)
                .map(orden -> ResponseEntity.ok(ApiResponse.<Orden_Producto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Orden de producto encontrada")
                        .data(orden)
                        .build()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<Orden_Producto>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Orden de producto no encontrada")
                                .build()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Orden_Producto>> create(@Valid @RequestBody OrdenProductoDTO dto) {
        try {
            Orden_Producto nueva = ordenProductoService.saveFromDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<Orden_Producto>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Orden de producto creada exitosamente")
                            .data(nueva)
                            .build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Orden_Producto>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Orden_Producto>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error al crear la orden: " + e.getMessage())
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Orden_Producto>> update(@PathVariable Long id, @Valid @RequestBody OrdenProductoDTO dto) {
        try {
            Orden_Producto actualizada = ordenProductoService.updateFromDTO(id, dto);
            return ResponseEntity.ok(ApiResponse.<Orden_Producto>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Orden de producto actualizada exitosamente")
                    .data(actualizada)
                    .build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Orden_Producto>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Orden_Producto>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error al actualizar la orden: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!ordenProductoService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Orden de producto no encontrada")
                            .build());
        }
        ordenProductoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("Orden de producto eliminada")
                        .build());
    }
} 