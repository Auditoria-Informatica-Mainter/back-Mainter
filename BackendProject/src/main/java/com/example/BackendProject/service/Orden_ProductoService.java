package com.example.BackendProject.service;

import com.example.BackendProject.entity.Orden_Producto;
import com.example.BackendProject.repository.Orden_ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Orden_ProductoService {

    @Autowired
    private Orden_ProductoRepository ordenProductoRepository;

    // Obtener todas las ordenes de producto
    public List<Orden_Producto> findAll() {
        return ordenProductoRepository.findAll();
    }

    // Buscar una orden de producto por su ID
    public Optional<Orden_Producto> findById(Long id) {
        return ordenProductoRepository.findById(id);
    }

    // Crear o actualizar una orden de producto
    public Orden_Producto save(Orden_Producto ordenProducto) {
        return ordenProductoRepository.save(ordenProducto);
    }

    // Eliminar una orden de producto por su ID
    public void deleteById(Long id) {
        ordenProductoRepository.deleteById(id);
    }
} 