package com.example.BackendProject.service;

import com.example.BackendProject.dto.OrdenPreProductoDTO;
import com.example.BackendProject.entity.Orden_PreProducto;
import com.example.BackendProject.entity.Pre_producto;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.Orden_PreProductoRepository;
import com.example.BackendProject.service.PreProductoService;
import com.example.BackendProject.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Orden_PreProductoService {

    @Autowired
    private Orden_PreProductoRepository ordenPreProductoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PreProductoService preProductoService;

    // Obtener todas las ordenes de preproducto
    public List<Orden_PreProducto> findAll() {
        return ordenPreProductoRepository.findAll();
    }

    // Buscar una orden de preproducto por su ID
    public Optional<Orden_PreProducto> findById(Long id) {
        return ordenPreProductoRepository.findById(id);
    }

    // Crear o actualizar una orden de preproducto
    public Orden_PreProducto saveFromDTO(OrdenPreProductoDTO dto) {
        Usuario usuario = usuarioService.obtenerUserPorId(dto.getUsuarioId());
        Pre_producto preProducto = preProductoService.obtenerPreProductoPorId(dto.getPreProductoId());
        if (usuario == null || preProducto == null) {
            throw new EntityNotFoundException("Usuario o PreProducto no encontrado");
        }
        Orden_PreProducto orden = new Orden_PreProducto();
        orden.setId(dto.getId());
        orden.setCantidad(dto.getCantidad());
        orden.setDescripcion(dto.getDescripcion());
        orden.setEstado(dto.getEstado());
        orden.setFecha(dto.getFecha());
        orden.setUsuario(usuario);
        orden.setPreProducto(preProducto);
        return ordenPreProductoRepository.save(orden);
    }

    // Actualizar una orden de preproducto
    public Orden_PreProducto updateFromDTO(Long id, OrdenPreProductoDTO dto) {
        Orden_PreProducto existente = ordenPreProductoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Orden_PreProducto no encontrada con ID: " + id));
        Usuario usuario = usuarioService.obtenerUserPorId(dto.getUsuarioId());
        Pre_producto preProducto = preProductoService.obtenerPreProductoPorId(dto.getPreProductoId());
        if (usuario == null || preProducto == null) {
            throw new EntityNotFoundException("Usuario o PreProducto no encontrado");
        }
        existente.setCantidad(dto.getCantidad());
        existente.setDescripcion(dto.getDescripcion());
        existente.setEstado(dto.getEstado());
        existente.setFecha(dto.getFecha());
        existente.setUsuario(usuario);
        existente.setPreProducto(preProducto);
        return ordenPreProductoRepository.save(existente);
    }

    // Eliminar una orden de preproducto por su ID
    public void deleteById(Long id) {
        ordenPreProductoRepository.deleteById(id);
    }
} 