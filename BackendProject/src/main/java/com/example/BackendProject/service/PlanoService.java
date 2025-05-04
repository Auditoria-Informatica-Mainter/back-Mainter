package com.example.BackendProject.service;

import com.example.BackendProject.dto.PlanoDTO;
import com.example.BackendProject.entity.Plano;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.entity.Pre_producto;
import com.example.BackendProject.repository.PlanoRepository;
import com.example.BackendProject.repository.ProductoRepository;
import com.example.BackendProject.repository.PreProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanoService {
    
    @Autowired
    private PlanoRepository planoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private PreProductoRepository preProductoRepository;
    
    @Transactional
    public Plano crearPlano(PlanoDTO planoDTO) {
        Producto producto = productoRepository.findById(planoDTO.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + planoDTO.getProductoId()));
                
        Pre_producto preProducto = preProductoRepository.findById(planoDTO.getPreProductoId())
                .orElseThrow(() -> new EntityNotFoundException("PreProducto no encontrado con ID: " + planoDTO.getPreProductoId()));
                
        Plano plano = new Plano(
            producto,
            preProducto,
            planoDTO.getCantidad(),
            planoDTO.getDescripcion(),
            planoDTO.getTiempo_estimado()
        );
        
        return planoRepository.save(plano);
    }
    
    public List<Plano> obtenerTodosLosPlanos() {
        return planoRepository.findAll();
    }
    
    public Plano obtenerPlanoPorId(Long id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plano no encontrado con ID: " + id));
    }
    
    public List<Plano> obtenerPlanosPorProducto(Long productoId) {
        return planoRepository.findByProductoId(productoId);
    }
    
    public List<Plano> obtenerPlanosPorPreProducto(Long preProductoId) {
        return planoRepository.findByPreProductoId(preProductoId);
    }
    
    @Transactional
    public Plano actualizarPlano(Long id, PlanoDTO planoDTO) {
        Plano plano = obtenerPlanoPorId(id);
        
        if (!plano.getProducto().getId().equals(planoDTO.getProductoId())) {
            Producto nuevoProducto = productoRepository.findById(planoDTO.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + planoDTO.getProductoId()));
            plano.setProducto(nuevoProducto);
        }
        
        if (!plano.getPreProducto().getId().equals(planoDTO.getPreProductoId())) {
            Pre_producto nuevoPreProducto = preProductoRepository.findById(planoDTO.getPreProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("PreProducto no encontrado con ID: " + planoDTO.getPreProductoId()));
            plano.setPreProducto(nuevoPreProducto);
        }
        
        plano.setCantidad(planoDTO.getCantidad());
        plano.setDescripcion(planoDTO.getDescripcion());
        plano.setTiempo_estimado(planoDTO.getTiempo_estimado());
        
        return planoRepository.save(plano);
    }
    
    @Transactional
    public void eliminarPlano(Long id) {
        if (!planoRepository.existsById(id)) {
            throw new EntityNotFoundException("Plano no encontrado con ID: " + id);
        }
        planoRepository.deleteById(id);
    }
    
    @Transactional
    public void eliminarPlanosPorProducto(Long productoId) {
        planoRepository.deleteByProductoId(productoId);
    }
    
    @Transactional
    public void eliminarPlanosPorPreProducto(Long preProductoId) {
        planoRepository.deleteByPreProductoId(preProductoId);
    }
}