package com.example.BackendProject.controller;

import com.example.BackendProject.dto.DetalleDevolucionDTO;

import com.example.BackendProject.service.DetalleDevolucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.BackendProject.dto.DetalleDevolucionResponseDTO;

@RestController
@RequestMapping(path = "/api/devoluciones", produces = "application/json")
@RequiredArgsConstructor
public class DetalleDevolucionController {

    private final DetalleDevolucionService detalleDevolucionService;

    @GetMapping("/{devolucionId}/detalles")
    public ResponseEntity<List<DetalleDevolucionResponseDTO>> listarDetallesPorDevolucion(@PathVariable Long devolucionId) {
        return ResponseEntity.ok(detalleDevolucionService.listarDetallesPorDevolucion(devolucionId));
    }

    @GetMapping("/{devolucionId}/detalles/{detalleId}")
    public ResponseEntity<DetalleDevolucionResponseDTO> obtenerDetalle(@PathVariable Long devolucionId, @PathVariable Long detalleId) {
        return ResponseEntity.ok(detalleDevolucionService.obtenerDetalle(devolucionId, detalleId));
    }

    @PostMapping(path = "/{devolucionId}/detalles", consumes = "application/json")
    public ResponseEntity<DetalleDevolucionResponseDTO> crearDetalle(@PathVariable Long devolucionId, @RequestBody DetalleDevolucionDTO detalleDto) {
        DetalleDevolucionResponseDTO nuevoDetalle = detalleDevolucionService.crearDetalle(devolucionId, detalleDto);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    @DeleteMapping("/{devolucionId}/detalles/{detalleId}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long devolucionId, @PathVariable Long detalleId) {
        detalleDevolucionService.eliminarDetalle(devolucionId, detalleId);
        return ResponseEntity.noContent().build();
    }
}
