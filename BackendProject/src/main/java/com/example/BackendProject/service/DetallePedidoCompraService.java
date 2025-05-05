package com.example.BackendProject.service;

import com.example.BackendProject.config.LoggableAction;
import com.example.BackendProject.dto.DetallePedidoCompraDTO;
import com.example.BackendProject.entity.DetallePedidoCompra;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.Compra;
import com.example.BackendProject.repository.DetallePedidoCompraRepository;
import com.example.BackendProject.repository.MaterialRepository;
import com.example.BackendProject.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de detalles de pedidos de compra
 */
@Service
public class DetallePedidoCompraService {
    
    private final DetallePedidoCompraRepository detallePedidoCompraRepository;
    private final CompraRepository compraRepository;
    private final MaterialRepository materialRepository;
    
    @Autowired
    public DetallePedidoCompraService(
            DetallePedidoCompraRepository detallePedidoCompraRepository,
            CompraRepository compraRepository,
            MaterialRepository materialRepository) {
        this.detallePedidoCompraRepository = detallePedidoCompraRepository;
        this.compraRepository = compraRepository;
        this.materialRepository = materialRepository;
    }
    
    /**
     * Obtiene todos los detalles de pedidos de compra
     * @return lista de detalles de pedidos
     */
    public List<DetallePedidoCompra> listarDetallesPedidos() {
        return detallePedidoCompraRepository.findAll();
    }
    
    /**
     * Obtiene un detalle de pedido por su ID
     * @param id el ID del detalle de pedido
     * @return el detalle de pedido encontrado
     * @throws ResponseStatusException si no se encuentra el detalle de pedido
     */
    public DetallePedidoCompra obtenerDetallePedido(Long id) {
        return detallePedidoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Detalle de pedido no encontrado con ID: " + id));
    }
    
    /**
     * Crea un nuevo detalle de pedido
     * @param dto datos del nuevo detalle de pedido
     * @return el detalle de pedido creado
     * @throws ResponseStatusException si no se encuentra la compra o el material
     */
    @LoggableAction
    public DetallePedidoCompra crearDetallePedido(DetallePedidoCompraDTO dto) {
        Compra compra = compraRepository.findById(dto.getCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Compra no encontrada con ID: " + dto.getCompraId()));
        
        Material material = materialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Material no encontrado con ID: " + dto.getMaterialId()));
        
        // Asignar valores por defecto si son null
        Integer cantidad = dto.getCantidad() != null ? dto.getCantidad() : 0;
        Double precio = dto.getPrecio() != null ? dto.getPrecio() : material.getPrecio(); // Usar precio del material si no se especifica
        Double importe = dto.getImporte() != null ? dto.getImporte() : (precio * cantidad);
        Double importeDesc = dto.getImporte_desc() != null ? dto.getImporte_desc() : 0.0;
        String estado = dto.getEstado() != null ? dto.getEstado() : "PENDIENTE";
        
        DetallePedidoCompra detallePedido = new DetallePedidoCompra(
                cantidad,
                precio,
                importe,
                importeDesc,
                estado,
                compra,
                material
        );
        
        return detallePedidoCompraRepository.save(detallePedido);
    }
    
    /**
     * Actualiza un detalle de pedido existente
     * @param id el ID del detalle de pedido a actualizar
     * @param dto los nuevos datos del detalle de pedido
     * @return el detalle de pedido actualizado
     * @throws ResponseStatusException si no se encuentra el detalle de pedido, la compra o el material
     */
    @LoggableAction
    public DetallePedidoCompra actualizarDetallePedido(Long id, DetallePedidoCompraDTO dto) {
        DetallePedidoCompra detallePedido = obtenerDetallePedido(id);
        
        if (dto.getCompraId() != null) {
            Compra compra = compraRepository.findById(dto.getCompraId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Compra no encontrada con ID: " + dto.getCompraId()));
            detallePedido.setCompra(compra);
        }
        
        if (dto.getMaterialId() != null) {
            Material material = materialRepository.findById(dto.getMaterialId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Material no encontrado con ID: " + dto.getMaterialId()));
            detallePedido.setMaterial(material);
        }
        
        // Actualizar cantidad si se proporciona
        if (dto.getCantidad() != null) {
            detallePedido.setCantidad(dto.getCantidad());
        }
        
        // Actualizar precio si se proporciona
        if (dto.getPrecio() != null) {
            detallePedido.setPrecio(dto.getPrecio());
        } else if (detallePedido.getPrecio() == null && detallePedido.getMaterial() != null) {
            // Si no tiene precio, usar el del material
            detallePedido.setPrecio(detallePedido.getMaterial().getPrecio());
        }
        
        // Calcular y actualizar importe si es necesario
        if (dto.getImporte() != null) {
            detallePedido.setImporte(dto.getImporte());
        } else if (detallePedido.getCantidad() != null && detallePedido.getPrecio() != null) {
            // Calcular importe basado en cantidad y precio
            detallePedido.setImporte(detallePedido.getCantidad() * detallePedido.getPrecio());
        }
        
        // Actualizar importe con descuento si se proporciona
        if (dto.getImporte_desc() != null) {
            detallePedido.setImporte_desc(dto.getImporte_desc());
        } else if (detallePedido.getImporte_desc() == null) {
            detallePedido.setImporte_desc(0.0); // Valor por defecto
        }
        
        // Actualizar estado si se proporciona
        if (dto.getEstado() != null) {
            detallePedido.setEstado(dto.getEstado());
        } else if (detallePedido.getEstado() == null) {
            detallePedido.setEstado("PENDIENTE"); // Estado por defecto
        }
        
        return detallePedidoCompraRepository.save(detallePedido);
    }
    
    /**
     * Elimina un detalle de pedido
     * @param id el ID del detalle de pedido a eliminar
     * @throws ResponseStatusException si no se encuentra el detalle de pedido
     */
    @LoggableAction
    public void eliminarDetallePedido(Long id) {
        if (!detallePedidoCompraRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Detalle de pedido no encontrado con ID: " + id);
        }
        
        detallePedidoCompraRepository.deleteById(id);
    }
    
    /**
     * Obtiene los detalles de pedido por compra
     * @param compraId el ID de la compra
     * @return lista de detalles de la compra
     */
    public List<DetallePedidoCompra> obtenerDetallesPorCompra(Long compraId) {
        return detallePedidoCompraRepository.findByCompraId(compraId);
    }
    
    /**
     * Obtiene los detalles de pedido por material
     * @param materialId el ID del material
     * @return lista de detalles que incluyen el material
     */
    public List<DetallePedidoCompra> obtenerDetallesPorMaterial(Long materialId) {
        return detallePedidoCompraRepository.findByMaterialId(materialId);
    }
    
    /**
     * Actualiza los valores NULL en todos los registros existentes
     * @return Número de registros actualizados
     */
    public int actualizarValoresNullEnRegistrosExistentes() {
        List<DetallePedidoCompra> detalles = detallePedidoCompraRepository.findAll();
        int actualizados = 0;
        
        for (DetallePedidoCompra detalle : detalles) {
            boolean requiereActualizacion = false;
            
            // Verificar y actualizar precio si es null
            if (detalle.getPrecio() == null && detalle.getMaterial() != null) {
                detalle.setPrecio(detalle.getMaterial().getPrecio());
                requiereActualizacion = true;
            }
            
            // Verificar y actualizar importe si es null
            if (detalle.getImporte() == null && detalle.getCantidad() != null && detalle.getPrecio() != null) {
                detalle.setImporte(detalle.getCantidad() * detalle.getPrecio());
                requiereActualizacion = true;
            }
            
            // Verificar y actualizar importe_desc si es null
            if (detalle.getImporte_desc() == null) {
                detalle.setImporte_desc(0.0);
                requiereActualizacion = true;
            }
            
            // Verificar y actualizar estado si es null
            if (detalle.getEstado() == null) {
                detalle.setEstado("PENDIENTE");
                requiereActualizacion = true;
            }
            
            // Si algún campo requiere actualización, guardar el detalle
            if (requiereActualizacion) {
                detallePedidoCompraRepository.save(detalle);
                actualizados++;
            }
        }
        
        return actualizados;
    }
} 