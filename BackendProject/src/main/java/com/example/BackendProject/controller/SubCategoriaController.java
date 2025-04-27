package com.example.BackendProject.controller;

import com.example.BackendProject.dto.SubCategoriaDTO;
import com.example.BackendProject.entity.SubCategoria;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.SubCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para la gestión de subcategorías
 */
@RestController
@RequestMapping("/api/subcategorias")
@CrossOrigin(origins = "*")
public class SubCategoriaController {
    
    private final SubCategoriaService subCategoriaService;
    
    @Autowired
    public SubCategoriaController(SubCategoriaService subCategoriaService) {
        this.subCategoriaService = subCategoriaService;
    }
    
    @Operation(summary = "Obtener todas las subcategorías")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubCategoria>>> obtenerTodasLasSubCategorias() {
        List<SubCategoria> subcategorias = subCategoriaService.listarSubCategorias();
        return new ResponseEntity<>(
                ApiResponse.<List<SubCategoria>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de subcategorías")
                        .data(subcategorias)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener una subcategoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoria>> obtenerSubCategoriaPorId(@PathVariable Long id) {
        try {
            SubCategoria subCategoria = subCategoriaService.obtenerSubCategoria(id);
            return new ResponseEntity<>(
                    ApiResponse.<SubCategoria>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Subcategoría encontrada")
                            .data(subCategoria)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<SubCategoria>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Subcategoría no encontrada con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Crear una nueva subcategoría")
    @PostMapping
    public ResponseEntity<ApiResponse<SubCategoria>> crearSubCategoria(@RequestBody SubCategoriaDTO subCategoriaDTO) {
        try {
            SubCategoria nuevaSubCategoria = subCategoriaService.crearSubCategoria(subCategoriaDTO);
            return new ResponseEntity<>(
                    ApiResponse.<SubCategoria>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Subcategoría creada exitosamente")
                            .data(nuevaSubCategoria)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<SubCategoria>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar una subcategoría existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoria>> actualizarSubCategoria(@PathVariable Long id, @RequestBody SubCategoriaDTO subCategoriaDTO) {
        try {
            SubCategoria subCategoriaActualizada = subCategoriaService.actualizarSubCategoria(id, subCategoriaDTO);
            return new ResponseEntity<>(
                    ApiResponse.<SubCategoria>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Subcategoría actualizada exitosamente")
                            .data(subCategoriaActualizada)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<SubCategoria>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Eliminar una subcategoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarSubCategoria(@PathVariable Long id) {
        try {
            subCategoriaService.eliminarSubCategoria(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Subcategoría eliminada exitosamente")
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
    
    @Operation(summary = "Obtener categorías por subcategoría")
    @GetMapping("/{id}/categorias")
    public ResponseEntity<ApiResponse<List<Object>>> obtenerCategoriasPorSubCategoria(@PathVariable Long id) {
        try {
            List<Object> categorias = subCategoriaService.obtenerCategoriasPorSubCategoria(id);
            return new ResponseEntity<>(
                    ApiResponse.<List<Object>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Categorías de la subcategoría ID: " + id)
                            .data(categorias)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<List<Object>>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
