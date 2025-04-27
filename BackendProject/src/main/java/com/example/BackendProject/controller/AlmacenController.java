package com.example.BackendProject.controller;

import com.example.BackendProject.dto.AlmacenDTO;
import com.example.BackendProject.entity.Almacen;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.AlmacenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para la gestión de almacenes
 */
@RestController
@RequestMapping("/api/almacenes")
@CrossOrigin(origins = "*")
public class AlmacenController {
    
    private final AlmacenService almacenService;
    
    @Autowired
    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }
    
    @Operation(summary = "Obtener todos los almacenes")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Almacen>>> obtenerTodosLosAlmacenes() {
        List<Almacen> almacenes = almacenService.listarAlmacenes();
        return new ResponseEntity<>(
                ApiResponse.<List<Almacen>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de almacenes")
                        .data(almacenes)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener un almacén por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Almacen>> obtenerAlmacenPorId(@PathVariable Long id) {
        try {
            Almacen almacen = almacenService.obtenerAlmacen(id);
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Almacén encontrado")
                            .data(almacen)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Almacén no encontrado con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Crear un nuevo almacén")
    @PostMapping
    public ResponseEntity<ApiResponse<Almacen>> crearAlmacen(@RequestBody AlmacenDTO almacenDTO) {
        try {
            Almacen nuevoAlmacen = almacenService.crearAlmacen(almacenDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Almacén creado exitosamente")
                            .data(nuevoAlmacen)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar un almacén existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Almacen>> actualizarAlmacen(@PathVariable Long id, @RequestBody AlmacenDTO almacenDTO) {
        try {
            Almacen almacenActualizado = almacenService.actualizarAlmacen(id, almacenDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Almacén actualizado exitosamente")
                            .data(almacenActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Eliminar un almacén")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarAlmacen(@PathVariable Long id) {
        try {
            almacenService.eliminarAlmacen(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Almacén eliminado exitosamente")
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
    
    @Operation(summary = "Buscar almacén por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<Almacen>> obtenerAlmacenPorNombre(@PathVariable String nombre) {
        try {
            Almacen almacen = almacenService.buscarPorNombre(nombre);
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Almacén encontrado")
                            .data(almacen)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Almacen>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Almacén no encontrado con nombre: " + nombre)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Buscar almacenes con capacidad mayor a la especificada")
    @GetMapping("/capacidad/{capacidad}")
    public ResponseEntity<ApiResponse<List<Almacen>>> obtenerAlmacenesPorCapacidadMayorQue(@PathVariable Double capacidad) {
        List<Almacen> almacenes = almacenService.buscarPorCapacidadMayorQue(capacidad);
        return new ResponseEntity<>(
                ApiResponse.<List<Almacen>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Almacenes con capacidad mayor a: " + capacidad)
                        .data(almacenes)
                        .build(),
                HttpStatus.OK
        );
    }
} 