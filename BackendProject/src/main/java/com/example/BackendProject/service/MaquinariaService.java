package com.example.BackendProject.service;

import com.example.BackendProject.dto.MaquinariaDTO;
import com.example.BackendProject.entity.Maquinaria;
import com.example.BackendProject.repository.MaquinariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaquinariaService {

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    // Create
    public Maquinaria createMaquinaria(MaquinariaDTO maquinariaDTO) {
        Maquinaria maquinaria = new Maquinaria();
        maquinaria.setNombre(maquinariaDTO.getNombre());
        maquinaria.setEstado(maquinariaDTO.getEstado());
        maquinaria.setDescripcion(maquinariaDTO.getDescripcion());
        return maquinariaRepository.save(maquinaria);
    }

    // Read
    public List<Maquinaria> getAllMaquinarias() {
        return maquinariaRepository.findAll();
    }

    public List<Maquinaria> getMaquinariasByEstado(String estado) {
        return maquinariaRepository.findByEstado(estado);
    }

    // Update
    public Maquinaria updateMaquinaria(Long id, MaquinariaDTO maquinariaDTO) {
        Maquinaria maquinaria = maquinariaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Máquina no encontrada"));
        maquinaria.setNombre(maquinariaDTO.getNombre());
        maquinaria.setEstado(maquinariaDTO.getEstado());
        maquinaria.setDescripcion(maquinariaDTO.getDescripcion());
        return maquinariaRepository.save(maquinaria);
    }

    // Delete
    public void deleteMaquinaria(Long id) {
        maquinariaRepository.deleteById(id);
    }
}