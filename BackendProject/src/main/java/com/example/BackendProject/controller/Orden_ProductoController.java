package com.example.BackendProject.controller;

import com.example.BackendProject.entity.Orden_Producto;
import com.example.BackendProject.service.Orden_ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orden-producto")
public class Orden_ProductoController {

    @Autowired
    private Orden_ProductoService ordenProductoService;

    // Obtener todas las ordenes de producto
    @GetMapping
    public List<Orden_Producto> getAll() {
        return ordenProductoService.findAll();
    }

    // Obtener una orden de producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Orden_Producto> getById(@PathVariable Long id) {
        Optional<Orden_Producto> orden = ordenProductoService.findById(id);
        return orden.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva orden de producto
    @PostMapping
    public Orden_Producto create(@RequestBody Orden_Producto ordenProducto) {
        // La fecha debe ser asignada por el dispositivo que la crea
        return ordenProductoService.save(ordenProducto);
    }

    // Actualizar una orden de producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Orden_Producto> update(@PathVariable Long id, @RequestBody Orden_Producto ordenProducto) {
        if (!ordenProductoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ordenProducto.setId(id);
        return ResponseEntity.ok(ordenProductoService.save(ordenProducto));
    }

    // Eliminar una orden de producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ordenProductoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ordenProductoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 