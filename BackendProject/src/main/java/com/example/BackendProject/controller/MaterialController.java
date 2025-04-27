package com.example.BackendProject.controller;

import com.example.BackendProject.dto.MaterialDTO;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controlador REST para la gestión de materiales.
 * Proporciona endpoints para crear, leer, actualizar y eliminar materiales,
 * así como funcionalidades específicas como búsqueda por nombre, gestión de stock
 * y control de inventario.
 */
@RestController
@RequestMapping("/api/materiales")
@CrossOrigin(origins = "*")
public class MaterialController {
    
    private final MaterialService materialService;
    
    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }
    
    @Operation(summary = "Listar todos los materiales")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Material>>> obtenerTodosLosMateriales() {
        List<Material> materiales = materialService.obtenerTodosLosMateriales();
        return new ResponseEntity<>(
                ApiResponse.<List<Material>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de materiales")
                        .data(materiales)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener un material por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Material>> obtenerMaterialPorId(@PathVariable Long id) {
        try {
            Material material = materialService.obtenerMaterialPorId(id);
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Material encontrado")
                            .data(material)
                            .build(),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Obtener un material por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<Material>> obtenerMaterialPorNombre(@PathVariable String nombre) {
        return materialService.buscarPorNombre(nombre)
                .map(material -> new ResponseEntity<>(
                        ApiResponse.<Material>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Material encontrado")
                                .data(material)
                                .build(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ApiResponse.<Material>builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Material no encontrado con nombre: " + nombre)
                                .build(),
                        HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Buscar materiales por término")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Material>>> buscarMaterialesPorTermino(@RequestParam("q") String texto) {
        List<Material> materiales = materialService.buscarPorTermino(texto);
        return new ResponseEntity<>(
                ApiResponse.<List<Material>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Resultados de búsqueda para: '" + texto + "'")
                        .data(materiales)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener materiales con stock bajo")
    @GetMapping("/bajo-stock")
    public ResponseEntity<ApiResponse<List<Material>>> obtenerMaterialesConStockBajo() {
        List<Material> materiales = materialService.obtenerMaterialesConStockBajo();
        return new ResponseEntity<>(
                ApiResponse.<List<Material>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Materiales con stock bajo")
                        .data(materiales)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener materiales que necesitan reabastecimiento")
    @GetMapping("/necesitan-reabastecimiento")
    public ResponseEntity<ApiResponse<List<Material>>> obtenerMaterialesParaReabastecer() {
        List<Material> materiales = materialService.obtenerMaterialesNecesitanReabastecimiento();
        return new ResponseEntity<>(
                ApiResponse.<List<Material>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Materiales que necesitan reabastecimiento")
                        .data(materiales)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener materiales por proveedor")
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<ApiResponse<List<Material>>> obtenerMaterialesPorProveedor(@PathVariable Long proveedorId) {
        try {
            List<Material> materiales = materialService.obtenerMaterialesPorProveedor(proveedorId);
            return new ResponseEntity<>(
                    ApiResponse.<List<Material>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Materiales del proveedor ID: " + proveedorId)
                            .data(materiales)
                            .build(),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<List<Material>>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Crear un nuevo material")
    @PostMapping
    public ResponseEntity<ApiResponse<Material>> crearMaterial(@RequestBody MaterialDTO materialDTO) {
        try {
            Material nuevoMaterial = materialService.crearMaterial(materialDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Material creado exitosamente")
                            .data(nuevoMaterial)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error al crear material: " + e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar un material existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Material>> actualizarMaterial(@PathVariable Long id, @RequestBody MaterialDTO materialDTO) {
        try {
            Material materialActualizado = materialService.actualizarMaterial(id, materialDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Material actualizado exitosamente")
                            .data(materialActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error al actualizar material: " + e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar imagen de un material")
    @PutMapping("/{id}/imagen")
    public ResponseEntity<ApiResponse<Material>> actualizarImagen(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String imagenUrl = payload.get("imagen");
            if (imagenUrl == null) {
                return new ResponseEntity<>(
                        ApiResponse.<Material>builder()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .message("URL de imagen no proporcionada")
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }
            
            Material materialActualizado = materialService.actualizarImagen(id, imagenUrl);
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Imagen de material actualizada exitosamente")
                            .data(materialActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Actualizar stock de un material")
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Material>> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            Material materialActualizado = materialService.actualizarStock(id, cantidad);
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Stock de material actualizado exitosamente")
                            .data(materialActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Material>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Eliminar un material")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarMaterial(@PathVariable Long id) {
        try {
            materialService.eliminarMaterial(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Material eliminado exitosamente")
                            .build(),
                    HttpStatus.NO_CONTENT
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
} 