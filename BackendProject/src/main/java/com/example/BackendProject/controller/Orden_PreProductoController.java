package com.example.BackendProject.controller;

import com.example.BackendProject.entity.Orden_PreProducto;
import com.example.BackendProject.service.Orden_PreProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orden-preproducto")
public class Orden_PreProductoController {

    @Autowired
    private Orden_PreProductoService ordenPreProductoService;

    // Obtener todas las ordenes de preproducto
    @GetMapping
    public List<Orden_PreProducto> getAll() {
        return ordenPreProductoService.findAll();
    }

    // Obtener una orden de preproducto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Orden_PreProducto> getById(@PathVariable Long id) {
        Optional<Orden_PreProducto> orden = ordenPreProductoService.findById(id);
        return orden.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva orden de preproducto
    @PostMapping
    public Orden_PreProducto create(@RequestBody Orden_PreProducto ordenPreProducto) {
        // La fecha debe ser asignada por el dispositivo que la crea
        return ordenPreProductoService.save(ordenPreProducto);
    }

    // Actualizar una orden de preproducto existente
    @PutMapping("/{id}")
    public ResponseEntity<Orden_PreProducto> update(@PathVariable Long id, @RequestBody Orden_PreProducto ordenPreProducto) {
        if (!ordenPreProductoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ordenPreProducto.setId(id);
        return ResponseEntity.ok(ordenPreProductoService.save(ordenPreProducto));
    }

    // Eliminar una orden de preproducto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ordenPreProductoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ordenPreProductoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 

