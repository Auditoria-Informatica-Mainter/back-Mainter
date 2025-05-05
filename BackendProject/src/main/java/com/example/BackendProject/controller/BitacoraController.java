package com.example.BackendProject.controller;

import com.example.BackendProject.dto.BitacoraDTO;
import com.example.BackendProject.entity.Bitacora;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.BitacoraService;
import com.example.BackendProject.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bitacora")
@CrossOrigin(origins = "*")
@Tag(name = "Bitácora", description = "Endpoints para la gestión de la bitácora del sistema")
public class BitacoraController {

    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private UsuarioService usuarioService;

    private BitacoraDTO convertToDTO(Bitacora bitacora) {
        return new BitacoraDTO(
            bitacora.getId(),
            bitacora.getAccion(),
            bitacora.getDetalles(),
            bitacora.getFecha(),
            bitacora.getUsuario().getId(),
            bitacora.getUsuario().getNombre() + " " + bitacora.getUsuario().getApellido(),
            bitacora.getDireccionIp()
        );
    }

    @Operation(summary = "Obtener historial completo de la bitácora")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BitacoraDTO>>> obtenerHistorialCompleto() {
        List<BitacoraDTO> historial = bitacoraService.obtenerHistorialCompleto()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                ApiResponse.<List<BitacoraDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historial de bitácora recuperado exitosamente")
                        .data(historial)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener historial por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<BitacoraDTO>>> obtenerHistorialPorUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUserPorId(usuarioId);
        List<BitacoraDTO> historial = bitacoraService.obtenerHistorialPorUsuario(usuario)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                ApiResponse.<List<BitacoraDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historial de bitácora del usuario recuperado exitosamente")
                        .data(historial)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener historial por acción")
    @GetMapping("/accion/{accion}")
    public ResponseEntity<ApiResponse<List<BitacoraDTO>>> obtenerHistorialPorAccion(@PathVariable String accion) {
        List<BitacoraDTO> historial = bitacoraService.obtenerHistorialPorAccion(accion)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                ApiResponse.<List<BitacoraDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historial de bitácora por acción recuperado exitosamente")
                        .data(historial)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener historial por rango de fechas")
    @GetMapping("/fecha")
    public ResponseEntity<ApiResponse<List<BitacoraDTO>>> obtenerHistorialPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        List<BitacoraDTO> historial = bitacoraService.obtenerHistorialPorRangoFechas(fechaInicio, fechaFin)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                ApiResponse.<List<BitacoraDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historial de bitácora por rango de fechas recuperado exitosamente")
                        .data(historial)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener historial por dirección IP")
    @GetMapping("/ip/{direccionIp}")
    public ResponseEntity<ApiResponse<List<BitacoraDTO>>> obtenerHistorialPorIp(@PathVariable String direccionIp) {
        List<BitacoraDTO> historial = bitacoraService.obtenerHistorialPorDireccionIp(direccionIp)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                ApiResponse.<List<BitacoraDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historial de bitácora por IP recuperado exitosamente")
                        .data(historial)
                        .build(),
                HttpStatus.OK
        );
    }
}