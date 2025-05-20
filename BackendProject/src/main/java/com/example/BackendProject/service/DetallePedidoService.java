package com.example.BackendProject.service;

import com.example.BackendProject.dto.DetallePedidoDTO;
import com.example.BackendProject.entity.Detalle_pedido;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.repository.DetallePedidoRepository;
import com.example.BackendProject.repository.PedidoRepository;
import com.example.BackendProject.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Detalle_pedido> listarDetalles() {
        return detallePedidoRepository.findAll();
    }

    public Detalle_pedido obtenerDetalle(Long id) {
        return detallePedidoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Detalle pedido no encontrado con ID: " + id));
    }

    public Detalle_pedido crearDetalle(DetallePedidoDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + dto.getProductoId()));

        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + dto.getPedidoId()));

        Detalle_pedido detalle = Detalle_pedido.builder()
                .producto(producto)
                .pedido(pedido)
                .cantidad(dto.getCantidad())
                .importe_Total(producto.getPrecioUnitario() * dto.getCantidad())
                .importe_Total_Desc(dto.getImporte_Total_Desc())
                .precioUnitario(producto.getPrecioUnitario())
                .build();

        if (detalle.getImporte_Total_Desc() == null) {
            detalle.setImporte_Total_Desc(0.0);
        }
        if (detalle.getImporte_Total() == null) {
            detalle.setImporte_Total(0.0);
        }

        Integer stockMenosCantidad = producto.getStock() - dto.getCantidad();
        
        if (stockMenosCantidad < 0) {
            throw new IllegalArgumentException("Stock insuficiente");
        }



        pedido.setImporte_total(pedido.getImporte_total() + detalle.getImporte_Total());
        pedido.setImporte_total_desc(detalle.getImporte_Total_Desc());

        pedidoRepository.save(pedido);

        return detallePedidoRepository.save(detalle);
    }

    public Detalle_pedido actualizarDetalle(Long id, DetallePedidoDTO dto) {
        Detalle_pedido existente = obtenerDetalle(id);

        Producto producto = productoRepository.findById(existente.getProducto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + dto.getProductoId()));

        Pedido pedido = pedidoRepository.findById(existente.getPedido().getId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + dto.getPedidoId()));

        
                
        Integer stockMenosCantidad = producto.getStock() - dto.getCantidad();
                
        if (stockMenosCantidad < 0) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        
        Double importe =  producto.getPrecioUnitario() * dto.getCantidad();

        pedido.setImporte_total((pedido.getImporte_total() - existente.getImporte_Total()) + importe );  
        
        existente.setCantidad(dto.getCantidad());
        existente.setImporte_Total(importe);
        
        
        existente.setImporte_Total_Desc(dto.getImporte_Total_Desc());
        pedido.setImporte_total_desc(existente.getImporte_Total_Desc());
        
        existente.setPrecioUnitario(producto.getPrecioUnitario());

        return detallePedidoRepository.save(existente);
    }

    public Detalle_pedido actualizarDetalleEstado(Long id, Boolean estado) {
        Detalle_pedido existente = obtenerDetalle(id);

        existente.setEstado(estado);

        Long idproducto = existente.getProducto().getId();

        Long idPedido = existente.getPedido().getId();

        Producto producto = productoRepository.findById(idproducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + idproducto));

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));
        

        Integer stockMenosCantidad = producto.getStock() - existente.getCantidad();
        
        if (stockMenosCantidad < 0) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        producto.setStock(stockMenosCantidad);

        pedido.setEstado(estado);

        pedidoRepository.save(pedido);

        productoRepository.save(producto);

        return detallePedidoRepository.save(existente);
    }

    public void eliminarDetalle(Long id) {
        Detalle_pedido existente = obtenerDetalle(id);

        Long idproducto = existente.getProducto().getId();

        Long idPedido = existente.getPedido().getId();

        

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));

        if (existente.getEstado() == true) {
            Producto producto = productoRepository.findById(idproducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + idproducto));

            producto.setStock(producto.getStock() + existente.getCantidad());

            productoRepository.save(producto);
        }

        pedido.setImporte_total(pedido.getImporte_total() - existente.getImporte_Total());
        pedido.setImporte_total_desc(pedido.getImporte_total_desc() - existente.getImporte_Total_Desc());
        pedidoRepository.save(pedido);

        detallePedidoRepository.delete(existente);
    }

    public List<Detalle_pedido> obtenerPorPedido(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    public List<Detalle_pedido> obtenerPorProducto(Long productoId) {
        return detallePedidoRepository.findByProductoId(productoId);
    }

    
}
