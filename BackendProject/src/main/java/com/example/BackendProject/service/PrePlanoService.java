package com.example.BackendProject.service;

import com.example.BackendProject.dto.PrePlanoDTO;
import com.example.BackendProject.entity.Pre_plano;
import com.example.BackendProject.entity.Pre_producto;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.repository.PrePlanoRepository;
import com.example.BackendProject.repository.PreProductoRepository;
import com.example.BackendProject.repository.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrePlanoService {
    
    @Autowired
    private PrePlanoRepository prePlanoRepository;
    
    @Autowired
    private PreProductoRepository preProductoRepository;
    
    @Autowired
    private MaterialRepository materialRepository;
    
    @Transactional
    public Pre_plano crearPrePlano(PrePlanoDTO prePlanoDTO) {
        Pre_producto preProducto = preProductoRepository.findById(prePlanoDTO.getPreProductoId())
                .orElseThrow(() -> new EntityNotFoundException("PreProducto no encontrado con ID: " + prePlanoDTO.getPreProductoId()));
                
        Material material = materialRepository.findById(prePlanoDTO.getMaterialId())
                .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + prePlanoDTO.getMaterialId()));
                
        Pre_plano prePlano = new Pre_plano(
            preProducto,
            material,
            prePlanoDTO.getCantidad(),
            prePlanoDTO.getDescripcion(),
            prePlanoDTO.getTiempo_estimado()
        );
        
        return prePlanoRepository.save(prePlano);
    }
    
    public List<Pre_plano> obtenerTodosLosPrePlanos() {
        return prePlanoRepository.findAll();
    }
    
    public Pre_plano obtenerPrePlanoPorId(Long id) {
        return prePlanoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PrePlano no encontrado con ID: " + id));
    }
    
    public List<Pre_plano> obtenerPrePlanosPorPreProducto(Long preProductoId) {
        return prePlanoRepository.findByPreProductoId(preProductoId);
    }
    
    public List<Pre_plano> obtenerPrePlanosPorMaterial(Long materialId) {
        return prePlanoRepository.findByMaterialId(materialId);
    }
    
    @Transactional
    public Pre_plano actualizarPrePlano(Long id, PrePlanoDTO prePlanoDTO) {
        Pre_plano prePlano = obtenerPrePlanoPorId(id);
        
        if (!prePlano.getPreProducto().getId().equals(prePlanoDTO.getPreProductoId())) {
            Pre_producto nuevoPreProducto = preProductoRepository.findById(prePlanoDTO.getPreProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("PreProducto no encontrado con ID: " + prePlanoDTO.getPreProductoId()));
            prePlano.setPreProducto(nuevoPreProducto);
        }
        
        if (!prePlano.getMaterial().getId().equals(prePlanoDTO.getMaterialId())) {
            Material nuevoMaterial = materialRepository.findById(prePlanoDTO.getMaterialId())
                    .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + prePlanoDTO.getMaterialId()));
            prePlano.setMaterial(nuevoMaterial);
        }
        
        prePlano.setCantidad(prePlanoDTO.getCantidad());
        prePlano.setDescripcion(prePlanoDTO.getDescripcion());
        prePlano.setTiempo_estimado(prePlanoDTO.getTiempo_estimado());
        
        return prePlanoRepository.save(prePlano);
    }
    
    @Transactional
    public void eliminarPrePlano(Long id) {
        if (!prePlanoRepository.existsById(id)) {
            throw new EntityNotFoundException("PrePlano no encontrado con ID: " + id);
        }
        prePlanoRepository.deleteById(id);
    }
    
    @Transactional
    public void eliminarPrePlanosPorPreProducto(Long preProductoId) {
        prePlanoRepository.deleteByPreProductoId(preProductoId);
    }
}