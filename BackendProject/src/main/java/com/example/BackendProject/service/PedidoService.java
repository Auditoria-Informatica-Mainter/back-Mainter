package com.example.BackendProject.service;

import com.example.BackendProject.config.LoggableAction;
import com.example.BackendProject.dto.PedidoDTO;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.PedidoRepository;
import com.example.BackendProject.repository.ProveedorRepository;
import com.example.BackendProject.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestión de pedidos (compras)
 */
@Service
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    
    @Autowired
    public PedidoService(
            PedidoRepository pedidoRepository,
            ProveedorRepository proveedorRepository,
            UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * Obtiene todos los pedidos
     * @return lista de pedidos
     */
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }
    
    /**
     * Obtiene un pedido por su ID
     * @param id el ID del pedido
     * @return el pedido encontrado
     * @throws ResponseStatusException si no se encuentra el pedido
     */
    public Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido no encontrado con ID: " + id));
    }
    
    /**
     * Crea un nuevo pedido
     * @param dto datos del nuevo pedido
     * @return el pedido creado
     * @throws ResponseStatusException si no se encuentra el proveedor o el usuario
     */
    @LoggableAction
    public Pedido crearPedido(PedidoDTO dto) {
        Proveedor proveedor = null;
        Usuario usuario = null;
        
        if (dto.getProveedorId() != null) {
            proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Proveedor no encontrado con ID: " + dto.getProveedorId()));
        }
        
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuario no encontrado con ID: " + dto.getUsuarioId()));
        }
        
        // Si no se proporciona fecha, usar la fecha actual
        Date fecha = dto.getFecha() != null ? dto.getFecha() : new Date();
        
        // Valores iniciales para importe_total e importe_descuento si no se proporcionan
        Double importeTotal = dto.getImporte_total() != null ? dto.getImporte_total() : 0.0;
        Double importeDescuento = dto.getImporte_descuento() != null ? dto.getImporte_descuento() : 0.0;
        
        Pedido pedido = new Pedido(
                dto.getEstado() != null ? dto.getEstado() : "PENDIENTE",
                fecha,
                importeTotal,
                importeDescuento,
                proveedor,
                usuario
        );
        
        return pedidoRepository.save(pedido);
    }
    
    /**
     * Actualiza un pedido existente
     * @param id el ID del pedido a actualizar
     * @param dto los nuevos datos del pedido
     * @return el pedido actualizado
     * @throws ResponseStatusException si no se encuentra el pedido, el proveedor o el usuario
     */
    @LoggableAction
    public Pedido actualizarPedido(Long id, PedidoDTO dto) {
        Pedido pedido = obtenerPedido(id);
        
        if (dto.getEstado() != null) {
            pedido.setEstado(dto.getEstado());
        }
        
        if (dto.getFecha() != null) {
            pedido.setFecha(dto.getFecha());
        }
        
        if (dto.getImporte_total() != null) {
            pedido.setImporte_total(dto.getImporte_total());
        }
        
        if (dto.getImporte_descuento() != null) {
            pedido.setImporte_descuento(dto.getImporte_descuento());
        }
        
        if (dto.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Proveedor no encontrado con ID: " + dto.getProveedorId()));
            pedido.setProveedor(proveedor);
        }
        
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuario no encontrado con ID: " + dto.getUsuarioId()));
            pedido.setUsuario(usuario);
        }
        
        return pedidoRepository.save(pedido);
    }
    
    /**
     * Elimina un pedido
     * @param id el ID del pedido a eliminar
     * @throws ResponseStatusException si no se encuentra el pedido
     */
    @LoggableAction
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Pedido no encontrado con ID: " + id);
        }
        
        pedidoRepository.deleteById(id);
    }
    
    /**
     * Busca pedidos por estado
     * @param estado el estado a buscar
     * @return lista de pedidos con el estado especificado
     */
    public List<Pedido> buscarPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
    
    /**
     * Busca pedidos por proveedor
     * @param proveedorId el ID del proveedor
     * @return lista de pedidos del proveedor
     */
    public List<Pedido> buscarPorProveedor(Long proveedorId) {
        return pedidoRepository.findByProveedorId(proveedorId);
    }
} 