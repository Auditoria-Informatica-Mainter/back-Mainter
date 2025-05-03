package com.example.BackendProject.controller;

import com.example.BackendProject.dto.SectorDTO;
import com.example.BackendProject.dto.SectorConAlmacenDTO;
import com.example.BackendProject.entity.Sector;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para la gestión de sectores
 */
@RestController
@RequestMapping("/api/sectores")
@CrossOrigin(origins = "*")
public class SectorController {
    
    private final SectorService sectorService;
    
    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }
    
    @Operation(summary = "Obtener todos los sectores")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Sector>>> obtenerTodosLosSectores() {
        List<Sector> sectores = sectorService.listarSectores();
        return new ResponseEntity<>(
                ApiResponse.<List<Sector>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de sectores")
                        .data(sectores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Obtener un sector por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sector>> obtenerSectorPorId(@PathVariable Long id) {
        try {
            Sector sector = sectorService.obtenerSector(id);
            return new ResponseEntity<>(
                    ApiResponse.<Sector>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Sector encontrado")
                            .data(sector)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Sector>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Sector no encontrado con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Crear un nuevo sector")
    @PostMapping
    public ResponseEntity<ApiResponse<Sector>> crearSector(@RequestBody SectorDTO sectorDTO) {
        try {
            Sector nuevoSector = sectorService.crearSector(sectorDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Sector>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Sector creado exitosamente")
                            .data(nuevoSector)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Sector>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    
    @Operation(summary = "Actualizar un sector existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Sector>> actualizarSector(@PathVariable Long id, @RequestBody SectorDTO sectorDTO) {
        try {
            Sector sectorActualizado = sectorService.actualizarSector(id, sectorDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Sector>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Sector actualizado exitosamente")
                            .data(sectorActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Sector>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Eliminar un sector")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarSector(@PathVariable Long id) {
        try {
            sectorService.eliminarSector(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Sector eliminado exitosamente")
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
    
    @Operation(summary = "Buscar sectores por almacén")
    @GetMapping("/almacen/{almacenId}")
    public ResponseEntity<ApiResponse<List<Sector>>> obtenerSectoresPorAlmacen(@PathVariable Long almacenId) {
        List<Sector> sectores = sectorService.buscarPorAlmacen(almacenId);
        return new ResponseEntity<>(
                ApiResponse.<List<Sector>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Sectores del almacén ID: " + almacenId)
                        .data(sectores)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @Operation(summary = "Buscar sectores por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<Sector>>> obtenerSectoresPorTipo(@PathVariable String tipo) {
        List<Sector> sectores = sectorService.buscarPorTipo(tipo);
        return new ResponseEntity<>(
                ApiResponse.<List<Sector>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Sectores de tipo: " + tipo)
                        .data(sectores)
                        .build(),
                HttpStatus.OK
        );
    }
    @Operation(summary = "Obtener un sector con su almacén")
    @GetMapping("/{id}/con-almacen")
    public ResponseEntity<ApiResponse<SectorConAlmacenDTO>> obtenerSectorConAlmacen(@PathVariable Long id) {
        try {
            SectorConAlmacenDTO dto = sectorService.obtenerSectorConAlmacen(id);
            return new ResponseEntity<>(
                    ApiResponse.<SectorConAlmacenDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Sector con almacén encontrado")
                            .data(dto)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<SectorConAlmacenDTO>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    
    @Operation(summary = "Obtener todos los sectores con su almacén")
    @GetMapping("/con-almacen")
    public ResponseEntity<ApiResponse<List<SectorConAlmacenDTO>>> obtenerTodosLosSectoresConAlmacen() {
        List<SectorConAlmacenDTO> sectores = sectorService.listarSectoresConAlmacen();
        return new ResponseEntity<>(
                ApiResponse.<List<SectorConAlmacenDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de sectores con almacén")
                        .data(sectores)
                        .build(),
                HttpStatus.OK
        );
    }
} 