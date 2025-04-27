package com.example.BackendProject.controller;

import com.example.BackendProject.dto.CategoriaDTO;
import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para la gestión de categorías.
 * Proporciona endpoints para administrar las categorías de productos y materiales.
 * 
 * Funcionalidades:
 * - Gestión básica de categorías (crear, leer, actualizar)
 * - Búsqueda por nombre
 * - Organización jerárquica de productos y materiales
 */
@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Categoria>>> obtenerTodasLasCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        return new ResponseEntity<>(
                ApiResponse.<List<Categoria>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de categorías")
                        .data(categorias)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener una categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.obtenerCategoria(id);
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Categoría encontrada")
                            .data(categoria)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Categoría no encontrada con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Obtener una categoría por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<Categoria>> obtenerCategoriaPorNombre(@PathVariable String nombre) {
        try {
            Categoria categoria = categoriaService.obtenerCategoriaPorNombre(nombre);
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Categoría encontrada por nombre: " + nombre)
                            .data(categoria)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Categoría no encontrada con nombre: " + nombre)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Crear una nueva categoría")
    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> crearCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        try {
            Categoria nuevaCategoria = categoriaService.guardarCategoria(categoriaDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Categoría creada exitosamente")
                            .data(nuevaCategoria)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar una categoría existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        try {
            Categoria categoriaActualizada = categoriaService.modificarCategoria(id, categoriaDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Categoría actualizada exitosamente")
                            .data(categoriaActualizada)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Categoria>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Eliminar una categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCategoria(@PathVariable Long id) {
        // La implementación actual no tiene método de eliminación
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .message("Eliminación de categorías no implementada")
                        .build(),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }
    
    @Operation(summary = "Obtener categorías por subcategoría")
    @GetMapping("/subcategoria/{subCategoriaId}")
    public ResponseEntity<ApiResponse<List<Categoria>>> obtenerCategoriasPorSubCategoria(@PathVariable Long subCategoriaId) {
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasPorSubCategoria(subCategoriaId);
            return new ResponseEntity<>(
                    ApiResponse.<List<Categoria>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Categorías de la subcategoría ID: " + subCategoriaId)
                            .data(categorias)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<List<Categoria>>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
