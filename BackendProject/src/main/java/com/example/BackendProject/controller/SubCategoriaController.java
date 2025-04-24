package com.example.BackendProject.controller;

import com.example.BackendProject.dto.SubCategoriaDTO;
import com.example.BackendProject.entity.SubCategoria;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.SubCategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subcategorias", description = "API para gestionar subcategorías")
@RestController
@RequestMapping("/subcategorias")
public class SubCategoriaController {

    @Autowired
    private SubCategoriaService subcategoriaservice;

    @Operation(summary = "Listar todas las subcategorías")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubCategoria>>> listarSubCategorias() {
        List<SubCategoria> lista = subcategoriaservice.listarSubcategorias();
        return new ResponseEntity<>(
                ApiResponse.<List<SubCategoria>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de subcategorías")
                        .data(lista)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener una subcategoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoria>> obtenerSubCategoria(@PathVariable Long id) {
        SubCategoria subCategoria = subcategoriaservice.obtenerSubcategoria(id);
        return new ResponseEntity<>(
                ApiResponse.<SubCategoria>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Subcategoría encontrada")
                        .data(subCategoria)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener una subcategoría por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<SubCategoria>> obtenerCategoriaPorNombre(@PathVariable String nombre) {
        SubCategoria subcategoria = subcategoriaservice.obtenerRolnnombre(nombre);
        return new ResponseEntity<>(
                ApiResponse.<SubCategoria>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Subcategoría encontrada")
                        .data(subcategoria)
                        .build(),
                HttpStatus.OK
        );
    }

        @PostMapping
    public ResponseEntity<ApiResponse<SubCategoria>> guardarSubCategoria(@Valid @RequestBody SubCategoriaDTO subCategoriaDTO) {
        SubCategoria nuevaSubCategoria = subcategoriaservice.guardarSubcategoria(subCategoriaDTO);
        return new ResponseEntity<>(
                ApiResponse.<SubCategoria>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Subcategoría creada exitosamente")
                        .data(nuevaSubCategoria)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Actualizar una subcategoría existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoria>> actualizarSubCategoria(
            @PathVariable Long id, 
            @Valid @RequestBody SubCategoriaDTO subCategoriaDTO) {
        SubCategoria actualizada = subcategoriaservice.modificarSubcategoria(id, subCategoriaDTO);
        return new ResponseEntity<>(
                ApiResponse.<SubCategoria>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Subcategoría actualizada exitosamente")
                        .data(actualizada)
                        .build(),
                HttpStatus.OK
        );
    }
}
