package com.example.BackendProject.service;

import com.example.BackendProject.dto.CarritoDTO;
import com.example.BackendProject.dto.ItemCarritoDTO;
import com.example.BackendProject.entity.Carrito;
import com.example.BackendProject.entity.ItemCarrito;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Detalle_pedido;
import com.example.BackendProject.entity.Metodo_pago;
import com.example.BackendProject.repository.CarritoRepository;
import com.example.BackendProject.repository.ItemCarritoRepository;
import com.example.BackendProject.repository.PedidoRepository;
import com.example.BackendProject.repository.MetodoPagoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private ItemCarritoRepository itemCarritoRepository;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    
    /**
     * Obtiene el carrito activo del usuario o crea uno nuevo si no existe
     */
    @Transactional
    public CarritoDTO obtenerCarritoActivo(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
            .orElseGet(() -> {
                Carrito nuevoCarrito = new Carrito();
                nuevoCarrito.setUsuario(usuarioService.obtenerUserPorId(usuarioId));
                nuevoCarrito.setFechaCreacion(LocalDateTime.now());
                nuevoCarrito.setActivo(true);
                nuevoCarrito.setItems(new ArrayList<>());
                return carritoRepository.save(nuevoCarrito);
            });
        return convertirADTO(carrito);
    }
    
    /**
     * Agrega un producto al carrito
     */
    @Transactional
    public CarritoDTO agregarProducto(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
            .orElseGet(() -> {
                Carrito nuevoCarrito = new Carrito();
                nuevoCarrito.setUsuario(usuarioService.obtenerUserPorId(usuarioId));
                nuevoCarrito.setFechaCreacion(LocalDateTime.now());
                nuevoCarrito.setActivo(true);
                nuevoCarrito.setItems(new ArrayList<>());
                return carritoRepository.save(nuevoCarrito);
            });
            
        Producto producto = productoService.obtenerProductoPorId(productoId);
        
        // Verificar si el producto ya está en el carrito
        Optional<ItemCarrito> itemExistente = itemCarritoRepository
            .findByCarritoIdAndProductoId(carrito.getId(), productoId);
        
        if (itemExistente.isPresent()) {
            // Actualizar cantidad si ya existe
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            itemCarritoRepository.save(item);
        } else {
            // Crear nuevo item si no existe
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            itemCarritoRepository.save(nuevoItem);
        }
        
        return convertirADTO(carritoRepository.findById(carrito.getId()).orElseThrow());
    }
    
    /**
     * Actualiza la cantidad de un producto en el carrito
     */
    @Transactional
    public CarritoDTO actualizarCantidad(Long usuarioId, Long productoId, Integer nuevaCantidad) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado"));
            
        ItemCarrito item = itemCarritoRepository
            .findByCarritoIdAndProductoId(carrito.getId(), productoId)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado en el carrito"));
        
        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);
        
        return convertirADTO(carritoRepository.findById(carrito.getId()).orElseThrow());
    }
    
    /**
     * Elimina un producto del carrito
     */
    @Transactional
    public void eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado"));
            
        ItemCarrito item = itemCarritoRepository
            .findByCarritoIdAndProductoId(carrito.getId(), productoId)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado en el carrito"));
        
        itemCarritoRepository.delete(item);
    }
    
    /**
     * Obtiene el total del carrito
     */
    public Double obtenerTotal(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado"));
            
        return itemCarritoRepository.findByCarritoId(carrito.getId()).stream()
            .map(ItemCarrito::getSubtotal)
            .reduce(0.0, Double::sum);
    }
    
    /**
     * Convierte una entidad Carrito a DTO
     */
    private CarritoDTO convertirADTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setUsuarioId(carrito.getUsuario().getId());
        dto.setFechaCreacion(carrito.getFechaCreacion());
        dto.setActivo(carrito.isActivo());
        
        List<ItemCarritoDTO> itemsDTO = carrito.getItems().stream()
            .map(ItemCarritoDTO::fromEntity)
            .collect(Collectors.toList());
        dto.setItems(itemsDTO);
        
        return dto;
    }
    
    /**
     * Convierte el carrito activo en un pedido
     */
    @Transactional
    public Pedido convertirCarritoEnPedido(Long usuarioId, Long metodoPagoId) {
        // 1. Obtener el carrito activo
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado"));
            
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }
        
        // 2. Crear el pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setFecha(new Date());
        pedido.setEstado(true);
        pedido.setDescripcion("Pedido generado desde carrito");
        
        // 3. Calcular totales
        Double importeTotal = carrito.getItems().stream()
            .map(ItemCarrito::getSubtotal)
            .reduce(0.0, Double::sum);
        pedido.setImporte_total(importeTotal);
        pedido.setImporte_total_desc(importeTotal); // Por defecto sin descuento
        
        // 4. Asignar método de pago
        Metodo_pago metodoPago = metodoPagoRepository.findById(metodoPagoId)
            .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));
        pedido.setMetodo_pago(metodoPago);
        
        // 5. Crear detalles del pedido
        List<Detalle_pedido> detalles = carrito.getItems().stream()
            .map(item -> {
                Detalle_pedido detalle = new Detalle_pedido();
                detalle.setPedido(pedido);
                detalle.setProducto(item.getProducto());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(item.getProducto().getPrecioUnitario());
                detalle.setImporte_Total(item.getSubtotal());
                detalle.setImporte_Total_Desc(item.getSubtotal());
                detalle.setEstado(false); // Pendiente de procesar
                return detalle;
            })
            .collect(Collectors.toList());
        
        pedido.setDetalle_pedidos(detalles);
        
        // 6. Guardar el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        // 7. Desactivar el carrito
        carrito.setActivo(false);
        carritoRepository.save(carrito);
        
        return pedidoGuardado;
    }
} 