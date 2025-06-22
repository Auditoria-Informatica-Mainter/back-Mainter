package com.example.BackendProject.controller;

import com.example.BackendProject.dto.DevolucionDTO;
import com.example.BackendProject.dto.DevolucionResponseDTO;
import com.example.BackendProject.service.DevolucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/devoluciones", produces = "application/json")
@RequiredArgsConstructor
public class DevolucionController {

    private final DevolucionService devolucionService;

    @GetMapping
    public List<DevolucionResponseDTO> listarDevoluciones() {
        return devolucionService.listarDevoluciones();
    }

    @GetMapping("/{id}")
    public DevolucionResponseDTO obtenerDevolucion(@PathVariable Long id) {
        return devolucionService.obtenerDevolucion(id);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DevolucionResponseDTO> crearDevolucion(@RequestBody DevolucionDTO devolucionDTO) {
        DevolucionResponseDTO nuevaDevolucion = devolucionService.crearDevolucion(devolucionDTO);
        return new ResponseEntity<>(nuevaDevolucion, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public DevolucionResponseDTO actualizarDevolucion(@PathVariable Long id, @RequestBody DevolucionDTO devolucionDTO) {
        return devolucionService.actualizarDevolucion(id, devolucionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDevolucion(@PathVariable Long id) {
        devolucionService.eliminarDevolucion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<DevolucionResponseDTO> listarDevolucionesPorUsuario(@PathVariable Long usuarioId) {
        return devolucionService.listarDevolucionesPorUsuario(usuarioId);
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<DevolucionResponseDTO> listarDevolucionesPorPedido(@PathVariable Long pedidoId) {
        return devolucionService.listarDevolucionesPorPedido(pedidoId);
    }

    @GetMapping("/estado")
    public List<DevolucionResponseDTO> listarDevolucionesPorEstado(@RequestParam Boolean estado) {
        return devolucionService.listarDevolucionesPorEstado(estado);
    }
}
