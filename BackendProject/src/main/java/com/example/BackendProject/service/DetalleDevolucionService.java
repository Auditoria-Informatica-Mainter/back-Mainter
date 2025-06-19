package com.example.BackendProject.service;

import com.example.BackendProject.dto.DetalleDevolucionDTO;
import com.example.BackendProject.entity.Detalle_Devolucion;
import com.example.BackendProject.entity.Detalle_pedido;
import com.example.BackendProject.entity.Devolucion;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.repository.DetalleDevolucionRepository;
import com.example.BackendProject.repository.DetallePedidoRepository;
import com.example.BackendProject.repository.DevolucionRepository;
import com.example.BackendProject.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleDevolucionService {

    @Autowired
    private DetalleDevolucionRepository detalleDevolucionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DevolucionRepository devolucionRepository;
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<Detalle_Devolucion> listarDetalles() {
        return detalleDevolucionRepository.findAll();
    }

    public Detalle_Devolucion obtenerDetalle(Long id) {
        return detalleDevolucionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Detalle devolución no encontrado con ID: " + id));
    }

    public Detalle_Devolucion crearDetalle(DetalleDevolucionDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + dto.getProductoId()));

        Devolucion devolucion = devolucionRepository.findById(dto.getDevolucionId())
                .orElseThrow(() -> new IllegalArgumentException("Devolución no encontrada con ID: " + dto.getDevolucionId()));

        Detalle_Devolucion detalle = Detalle_Devolucion.builder()
                .producto(producto)
                .devolucion(devolucion)
                .cantidad(dto.getCantidad())
                .importe_Total(producto.getPrecioUnitario() * dto.getCantidad())
                .motivo_detalle(dto.getMotivo_detalle())
                .build();
                
        // Si se proporciona un ID de detalle de pedido, establecer la relación
        if (dto.getDetallePedidoId() != null) {
            Detalle_pedido detallePedido = detallePedidoRepository.findById(dto.getDetallePedidoId())
                    .orElseThrow(() -> new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + dto.getDetallePedidoId()));
            detalle.setDetalle_pedido(detallePedido);
        }

        return detalleDevolucionRepository.save(detalle);
    }

    public Detalle_Devolucion actualizarDetalle(Long id, DetalleDevolucionDTO dto) {
        Detalle_Devolucion detalle = obtenerDetalle(id);

        if (dto.getProductoId() != null) {
            Producto producto = productoRepository.findById(dto.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + dto.getProductoId()));
            detalle.setProducto(producto);
        }

        if (dto.getDevolucionId() != null) {
            Devolucion devolucion = devolucionRepository.findById(dto.getDevolucionId())
                    .orElseThrow(() -> new IllegalArgumentException("Devolución no encontrada con ID: " + dto.getDevolucionId()));
            detalle.setDevolucion(devolucion);
        }

        if (dto.getCantidad() != null) {
            detalle.setCantidad(dto.getCantidad());
            // Recalcular el importe total
            detalle.setImporte_Total(detalle.getProducto().getPrecioUnitario() * dto.getCantidad());
        }

        if (dto.getMotivo_detalle() != null) {
            detalle.setMotivo_detalle(dto.getMotivo_detalle());
        }
        
        if (dto.getDetallePedidoId() != null) {
            Detalle_pedido detallePedido = detallePedidoRepository.findById(dto.getDetallePedidoId())
                    .orElseThrow(() -> new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + dto.getDetallePedidoId()));
            detalle.setDetalle_pedido(detallePedido);
        }

        return detalleDevolucionRepository.save(detalle);
    }

    public void eliminarDetalle(Long id) {
        if (!detalleDevolucionRepository.existsById(id)) {
            throw new IllegalArgumentException("Detalle devolución no encontrado con ID: " + id);
        }
        detalleDevolucionRepository.deleteById(id);
    }

    public List<Detalle_Devolucion> listarDetallesPorDevolucion(Long devolucionId) {
        return detalleDevolucionRepository.findByDevolucion_Id(devolucionId);
    }

    public List<Detalle_Devolucion> listarDetallesPorProducto(Long productoId) {
        return detalleDevolucionRepository.findByProducto_Id(productoId);
    }
}