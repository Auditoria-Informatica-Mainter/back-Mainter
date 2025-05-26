package com.example.BackendProject.controller;


import com.example.BackendProject.dto.MaquinariaDTO;
import com.example.BackendProject.entity.Maquinaria;
import com.example.BackendProject.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/maquinarias")
public class MaquinariaController {

    @Autowired
    private MaquinariaService maquinariaService;

    @PostMapping
    public ResponseEntity<Maquinaria> createMaquinaria(@RequestBody MaquinariaDTO maquinariaDTO) {
        Maquinaria nuevaMaquinaria = maquinariaService.createMaquinaria(maquinariaDTO);
        return ResponseEntity.ok(nuevaMaquinaria);
    }

    @GetMapping
    public ResponseEntity<List<Maquinaria>> getAllMaquinarias() {
        List<Maquinaria> maquinarias = maquinariaService.getAllMaquinarias();
        return ResponseEntity.ok(maquinarias);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Maquinaria>> getMaquinariasByEstado(@PathVariable String estado) {
        List<Maquinaria> maquinarias = maquinariaService.getMaquinariasByEstado(estado);
        return ResponseEntity.ok(maquinarias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maquinaria> updateMaquinaria(
            @PathVariable Long id,
            @RequestBody MaquinariaDTO maquinariaDTO) {
        Maquinaria maquinariaActualizada = maquinariaService.updateMaquinaria(id, maquinariaDTO);
        return ResponseEntity.ok(maquinariaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaquinaria(@PathVariable Long id) {
        maquinariaService.deleteMaquinaria(id);
        return ResponseEntity.noContent().build();
    }
}