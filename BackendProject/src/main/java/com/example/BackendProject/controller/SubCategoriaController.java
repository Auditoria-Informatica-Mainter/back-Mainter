package com.example.BackendProject.controller;

import com.example.BackendProject.dto.SubCategoriaDTO;
import com.example.BackendProject.entity.SubCategoria;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subcategorias")
public class SubCategoriaController {

    @Autowired
    private SubCategoriaService subcategoriaservice;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubCategoria>>> listarSubCategorias() {
        // System.out.println("asdasdasdasdasdasdasd");
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

    @PostMapping
public ResponseEntity<ApiResponse<SubCategoria>> guardarSubCategoria(@RequestBody SubCategoriaDTO subCategoriaDTO) {
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


        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<SubCategoria>> actualizarSubCategoria(@PathVariable Long id, @RequestBody SubCategoriaDTO subCategoriaDTO) {
        // Llamamos al servicio para modificar la subcategoría
        SubCategoria actualizada = subcategoriaservice.modificarSubcategoria(id, subCategoriaDTO.getNombre(), subCategoriaDTO.getDescripcion());

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
