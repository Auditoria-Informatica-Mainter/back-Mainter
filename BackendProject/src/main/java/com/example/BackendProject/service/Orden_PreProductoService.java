package com.example.BackendProject.service;

import com.example.BackendProject.entity.Orden_PreProducto;
import com.example.BackendProject.repository.Orden_PreProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Orden_PreProductoService {

    @Autowired
    private Orden_PreProductoRepository ordenPreProductoRepository;

    // Obtener todas las ordenes de preproducto
    public List<Orden_PreProducto> findAll() {
        return ordenPreProductoRepository.findAll();
    }

    // Buscar una orden de preproducto por su ID
    public Optional<Orden_PreProducto> findById(Long id) {
        return ordenPreProductoRepository.findById(id);
    }

    // Crear o actualizar una orden de preproducto
    public Orden_PreProducto save(Orden_PreProducto ordenPreProducto) {
        return ordenPreProductoRepository.save(ordenPreProducto);
    }

    // Eliminar una orden de preproducto por su ID
    public void deleteById(Long id) {
        ordenPreProductoRepository.deleteById(id);
    }
} 