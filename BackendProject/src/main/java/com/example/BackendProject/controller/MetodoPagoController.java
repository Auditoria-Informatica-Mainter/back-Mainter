package com.example.BackendProject.controller;

import com.example.BackendProject.dto.MetodoPagoDTO;
import com.example.BackendProject.entity.Metodo_pago;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.MetodoPagoService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@CrossOrigin(origins = "*")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    @Autowired
    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @Operation(summary = "Obtener todos los métodos de pago")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Metodo_pago>>> obtenerTodos() {
        List<Metodo_pago> lista = metodoPagoService.listarMetodosPago();
        return ResponseEntity.ok(
                ApiResponse.<List<Metodo_pago>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de métodos de pago")
                        .data(lista)
                        .build()
        );
    }

    @Operation(summary = "Obtener método de pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Metodo_pago>> obtenerPorId(@PathVariable Long id) {
        try {
            Metodo_pago metodo = metodoPagoService.obtenerMetodoPago(id);
            return ResponseEntity.ok(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Método de pago encontrado")
                            .data(metodo)
                            .build()
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build()
            );
        }
    }

    @Operation(summary = "Crear un nuevo método de pago")
    @PostMapping
    public ResponseEntity<ApiResponse<Metodo_pago>> crear(@RequestBody MetodoPagoDTO dto) {
        try {
            Metodo_pago nuevo = metodoPagoService.crearMetodoPago(dto);
            return new ResponseEntity<>(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Método de pago creado exitosamente")
                            .data(nuevo)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build()
            );
        }
    }

    @Operation(summary = "Actualizar un método de pago")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Metodo_pago>> actualizar(@PathVariable Long id, @RequestBody MetodoPagoDTO dto) {
        try {
            Metodo_pago actualizado = metodoPagoService.actualizarMetodoPago(id, dto);
            return ResponseEntity.ok(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Método de pago actualizado exitosamente")
                            .data(actualizado)
                            .build()
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build()
            );
        }
    }

    @Operation(summary = "Eliminar un método de pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            metodoPagoService.eliminarMetodoPago(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Método de pago eliminado exitosamente")
                            .build(),
                    HttpStatus.NO_CONTENT
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build()
            );
        }
    }

    @Operation(summary = "Buscar método de pago por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<Metodo_pago>> buscarPorNombre(@PathVariable String nombre) {
        try {
            Metodo_pago metodo = metodoPagoService.buscarPorNombre(nombre);
            return ResponseEntity.ok(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Método de pago encontrado")
                            .data(metodo)
                            .build()
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Metodo_pago>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build()
            );
        }
    }    @Operation(summary = "Verificar si un método de pago existe")
    @GetMapping("/verificar/{id}")
    public ResponseEntity<ApiResponse<Boolean>> verificarMetodoPago(@PathVariable Long id) {
        try {
            metodoPagoService.obtenerMetodoPago(id);
            return ResponseEntity.ok(
                    ApiResponse.<Boolean>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Método de pago existe")
                            .data(true)
                            .build()
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.ok(
                    ApiResponse.<Boolean>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Método de pago no existe")
                            .data(false)
                            .build()
            );
        }
    }
}
