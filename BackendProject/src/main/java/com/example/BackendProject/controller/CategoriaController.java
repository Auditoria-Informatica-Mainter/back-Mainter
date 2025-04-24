package com.example.BackendProject.controller;

import com.example.BackendProject.dto.CategoriaDTO;
import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "API para gestionar categorías")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Categoria>>> listarCategorias() {
        List<Categoria> lista = categoriaService.listarCategorias();
        return new ResponseEntity<>(
                ApiResponse.<List<Categoria>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de categorías")
                        .data(lista)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> obtenerCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaService.obtenerCategoria(id);
        return new ResponseEntity<>(
                ApiResponse.<Categoria>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Categoría encontrada")
                        .data(categoria)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<Categoria>> obtenerCategoriaPorNombre(@PathVariable String nombre) {
        Categoria categoria = categoriaService.obtenerRolnnombre(nombre);
        return new ResponseEntity<>(
                ApiResponse.<Categoria>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Rol encontrado")
                        .data(categoria)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Crear una nueva categoría")
    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> guardarCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        Categoria nueva = categoriaService.guardarCategoria(categoriaDTO);
        return new ResponseEntity<>(
                ApiResponse.<Categoria>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Categoría creada exitosamente")
                        .data(nueva)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Actualizar una categoría existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> modificarCategoria(
            @PathVariable Long id, 
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        Categoria actualizada = categoriaService.modificarCategoria(id, categoriaDTO);
        return new ResponseEntity<>(
                ApiResponse.<Categoria>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Categoría actualizada exitosamente")
                        .data(actualizada)
                        .build(),
                HttpStatus.OK
        );
    }
}
