package com.example.BackendProject.controller;

import com.example.BackendProject.dto.OrdenPreProductoDTO;
import com.example.BackendProject.entity.Orden_PreProducto;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.Orden_PreProductoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orden-preproducto")
public class Orden_PreProductoController {

    @Autowired
    private Orden_PreProductoService ordenPreProductoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Orden_PreProducto>>> getAll() {
        List<Orden_PreProducto> ordenes = ordenPreProductoService.findAll();
        return ResponseEntity.ok(ApiResponse.<List<Orden_PreProducto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Lista de ordenes de preproducto")
                .data(ordenes)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Orden_PreProducto>> getById(@PathVariable Long id) {
        return ordenPreProductoService.findById(id)
                .map(orden -> ResponseEntity.ok(ApiResponse.<Orden_PreProducto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Orden de preproducto encontrada")
                        .data(orden)
                        .build()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<Orden_PreProducto>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Orden de preproducto no encontrada")
                                .build()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Orden_PreProducto>> create(@Valid @RequestBody OrdenPreProductoDTO dto) {
        try {
            Orden_PreProducto nueva = ordenPreProductoService.saveFromDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<Orden_PreProducto>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Orden de preproducto creada exitosamente")
                            .data(nueva)
                            .build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Orden_PreProducto>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Orden_PreProducto>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error al crear la orden: " + e.getMessage())
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Orden_PreProducto>> update(@PathVariable Long id, @Valid @RequestBody OrdenPreProductoDTO dto) {
        try {
            Orden_PreProducto actualizada = ordenPreProductoService.updateFromDTO(id, dto);
            return ResponseEntity.ok(ApiResponse.<Orden_PreProducto>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Orden de preproducto actualizada exitosamente")
                    .data(actualizada)
                    .build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Orden_PreProducto>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Orden_PreProducto>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error al actualizar la orden: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!ordenPreProductoService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Orden de preproducto no encontrada")
                            .build());
        }
        ordenPreProductoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("Orden de preproducto eliminada")
                        .build());
    }
} 

