package com.example.BackendProject.service;

import com.example.BackendProject.dto.PreProductoDTO;
import com.example.BackendProject.entity.Pre_producto;
import com.example.BackendProject.repository.PreProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PreProductoService {
    
    @Autowired
    private PreProductoRepository preProductoRepository;
    
    @Transactional
    public Pre_producto crearPreProducto(PreProductoDTO preProductoDTO) {
        Pre_producto preProducto = new Pre_producto(
            preProductoDTO.getNombre(),
            preProductoDTO.getDescripcion(),
            preProductoDTO.getStock(),
            preProductoDTO.getTiempo(),
            preProductoDTO.getUrl_Image()
        );
        
        return preProductoRepository.save(preProducto);
    }
    
    public List<Pre_producto> obtenerTodosLosPreProductos() {
        return preProductoRepository.findAll();
    }
    
    public Pre_producto obtenerPreProductoPorId(Long id) {
        return preProductoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PreProducto no encontrado con ID: " + id));
    }
    
    public Optional<Pre_producto> buscarPorNombre(String nombre) {
        return preProductoRepository.findByNombre(nombre);
    }
    
    @Transactional
    public Pre_producto actualizarPreProducto(Long id, PreProductoDTO preProductoDTO) {
        Pre_producto preProducto = obtenerPreProductoPorId(id);
        
        preProducto.setNombre(preProductoDTO.getNombre());
        preProducto.setDescripcion(preProductoDTO.getDescripcion());
        preProducto.setStock(preProductoDTO.getStock());
        preProducto.setTiempo(preProductoDTO.getTiempo());
        if (preProductoDTO.getUrl_Image() != null) {
            preProducto.setUrl_Image(preProductoDTO.getUrl_Image());
        }
        
        return preProductoRepository.save(preProducto);
    }
    
    @Transactional
    public void eliminarPreProducto(Long id) {
        if (!preProductoRepository.existsById(id)) {
            throw new EntityNotFoundException("PreProducto no encontrado con ID: " + id);
        }
        preProductoRepository.deleteById(id);
    }
}