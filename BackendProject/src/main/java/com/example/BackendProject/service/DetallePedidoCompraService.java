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
        
        DetallePedidoCompra detallePedido = new DetallePedidoCompra(
                dto.getCantidad(),
                dto.getPrecio(),
                dto.getImporte(),
                dto.getImporte_desc(),
                dto.getEstado(),
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
        
        detallePedido.setCantidad(dto.getCantidad());
        detallePedido.setPrecio(dto.getPrecio());
        detallePedido.setImporte(dto.getImporte());
        detallePedido.setImporte_desc(dto.getImporte_desc());
        detallePedido.setEstado(dto.getEstado());
        
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
} 