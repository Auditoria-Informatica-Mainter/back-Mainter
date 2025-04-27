package com.example.BackendProject.controller;

import com.example.BackendProject.dto.ProveedorMaterialDTO;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.entity.ProveedorMaterial;
import com.example.BackendProject.repository.MaterialRepository;
import com.example.BackendProject.repository.ProveedorMaterialRepository;
import com.example.BackendProject.repository.ProveedorRepository;
import com.example.BackendProject.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de la relación entre proveedores y materiales.
 * Proporciona endpoints para asociar materiales con proveedores y consultar esas relaciones.
 */
@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorMaterialController {

    private final ProveedorRepository proveedorRepository;
    private final MaterialRepository materialRepository;
    private final ProveedorMaterialRepository proveedorMaterialRepository;

    @Autowired
    public ProveedorMaterialController(
            ProveedorRepository proveedorRepository,
            MaterialRepository materialRepository,
            ProveedorMaterialRepository proveedorMaterialRepository) {
        this.proveedorRepository = proveedorRepository;
        this.materialRepository = materialRepository;
        this.proveedorMaterialRepository = proveedorMaterialRepository;
    }

    @Operation(summary = "Asociar un material a un proveedor")
    @PostMapping("/{proveedorId}/materiales")
    public ResponseEntity<ApiResponse<ProveedorMaterial>> asociarMaterialAProveedor(
            @PathVariable Long proveedorId,
            @RequestBody ProveedorMaterialDTO dto) {
        
        // Verificar que el proveedor existe
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(proveedorId);
        if (proveedorOpt.isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.<ProveedorMaterial>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Proveedor no encontrado con ID: " + proveedorId)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
        
        // Verificar que el material existe
        Optional<Material> materialOpt = materialRepository.findById(dto.getMaterialId());
        if (materialOpt.isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.<ProveedorMaterial>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Material no encontrado con ID: " + dto.getMaterialId())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
        
        Proveedor proveedor = proveedorOpt.get();
        Material material = materialOpt.get();
        
        // Verificar si ya existe la relación
        Optional<ProveedorMaterial> existente = proveedorMaterialRepository.findByProveedorAndMaterial(proveedor, material);
        if (existente.isPresent()) {
            ProveedorMaterial proveedorMaterial = existente.get();
            proveedorMaterial.setPrecio(dto.getPrecio());
            proveedorMaterial.setCantidadMinima(dto.getCantidadMinima());
            proveedorMaterial.setDescripcion(dto.getDescripcion());
            
            ProveedorMaterial actualizado = proveedorMaterialRepository.save(proveedorMaterial);
            
            return new ResponseEntity<>(
                    ApiResponse.<ProveedorMaterial>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Relación proveedor-material actualizada exitosamente")
                            .data(actualizado)
                            .build(),
                    HttpStatus.OK
            );
        }
        
        // Crear nueva relación
        ProveedorMaterial proveedorMaterial = new ProveedorMaterial();
        proveedorMaterial.setProveedor(proveedor);
        proveedorMaterial.setMaterial(material);
        proveedorMaterial.setPrecio(dto.getPrecio());
        proveedorMaterial.setCantidadMinima(dto.getCantidadMinima());
        proveedorMaterial.setDescripcion(dto.getDescripcion());
        
        ProveedorMaterial guardado = proveedorMaterialRepository.save(proveedorMaterial);
        
        return new ResponseEntity<>(
                ApiResponse.<ProveedorMaterial>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Material asociado exitosamente al proveedor")
                        .data(guardado)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Obtener todos los materiales de un proveedor")
    @GetMapping("/{proveedorId}/materiales")
    public ResponseEntity<ApiResponse<List<ProveedorMaterial>>> obtenerMaterialesDeProveedor(@PathVariable Long proveedorId) {
        // Verificar que el proveedor existe
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(proveedorId);
        if (proveedorOpt.isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.<List<ProveedorMaterial>>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Proveedor no encontrado con ID: " + proveedorId)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
        
        List<ProveedorMaterial> materiales = proveedorMaterialRepository.findByProveedorId(proveedorId);
        
        return new ResponseEntity<>(
                ApiResponse.<List<ProveedorMaterial>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Materiales del proveedor ID: " + proveedorId)
                        .data(materiales)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Eliminar asociación entre proveedor y material")
    @DeleteMapping("/{proveedorId}/materiales/{materialId}")
    public ResponseEntity<ApiResponse<Void>> eliminarAsociacion(
            @PathVariable Long proveedorId,
            @PathVariable Long materialId) {
        
        // Verificar si existe la relación
        Optional<ProveedorMaterial> relacion = proveedorMaterialRepository.findByProveedorIdAndMaterialId(proveedorId, materialId);
        if (relacion.isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("No existe relación entre el proveedor ID: " + proveedorId + " y el material ID: " + materialId)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
        
        // Eliminar la relación
        proveedorMaterialRepository.delete(relacion.get());
        
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("Relación eliminada exitosamente")
                        .build(),
                HttpStatus.NO_CONTENT
        );
    }
} 