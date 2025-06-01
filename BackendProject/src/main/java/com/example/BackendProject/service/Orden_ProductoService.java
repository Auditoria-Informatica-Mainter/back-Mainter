package com.example.BackendProject.service;

import com.example.BackendProject.dto.OrdenProductoDTO;
import com.example.BackendProject.entity.Orden_Producto;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.Orden_ProductoRepository;
import com.example.BackendProject.service.ProductoService;
import com.example.BackendProject.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Orden_ProductoService {

    @Autowired
    private Orden_ProductoRepository ordenProductoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoService productoService;

    // Obtener todas las ordenes de producto
    public List<Orden_Producto> findAll() {
        return ordenProductoRepository.findAll();
    }

    // Buscar una orden de producto por su ID
    public Optional<Orden_Producto> findById(Long id) {
        return ordenProductoRepository.findById(id);
    }

    // Crear o actualizar una orden de producto
    public Orden_Producto saveFromDTO(OrdenProductoDTO dto) {
        Usuario usuario = usuarioService.obtenerUserPorId(dto.getUsuarioId());
        Producto producto = productoService.obtenerProductoPorId(dto.getProductoId());
        if (usuario == null || producto == null) {
            throw new EntityNotFoundException("Usuario o Producto no encontrado");
        }
        Orden_Producto orden = new Orden_Producto();
        orden.setId(dto.getId());
        orden.setCantidad(dto.getCantidad());
        orden.setDescripcion(dto.getDescripcion());
        orden.setEstado(dto.getEstado());
        orden.setFecha(dto.getFecha());
        orden.setUsuario(usuario);
        orden.setProducto(producto);
        return ordenProductoRepository.save(orden);
    }

    // Actualizar una orden de producto
    public Orden_Producto updateFromDTO(Long id, OrdenProductoDTO dto) {
        Orden_Producto existente = ordenProductoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Orden_Producto no encontrada con ID: " + id));
        Usuario usuario = usuarioService.obtenerUserPorId(dto.getUsuarioId());
        Producto producto = productoService.obtenerProductoPorId(dto.getProductoId());
        if (usuario == null || producto == null) {
            throw new EntityNotFoundException("Usuario o Producto no encontrado");
        }
        existente.setCantidad(dto.getCantidad());
        existente.setDescripcion(dto.getDescripcion());
        existente.setEstado(dto.getEstado());
        existente.setFecha(dto.getFecha());
        existente.setUsuario(usuario);
        existente.setProducto(producto);
        return ordenProductoRepository.save(existente);
    }

    // Eliminar una orden de producto por su ID
    public void deleteById(Long id) {
        ordenProductoRepository.deleteById(id);
    }
} 