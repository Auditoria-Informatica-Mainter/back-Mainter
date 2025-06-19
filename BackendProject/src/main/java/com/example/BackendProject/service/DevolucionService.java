package com.example.BackendProject.service;

import com.example.BackendProject.dto.DevolucionDTO;
import com.example.BackendProject.dto.DetalleDevolucionDTO;
import com.example.BackendProject.entity.Devolucion;
import com.example.BackendProject.entity.Detalle_Devolucion;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Detalle_pedido;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.DevolucionRepository;
import com.example.BackendProject.repository.DetallePedidoRepository;
import com.example.BackendProject.repository.PedidoRepository;
import com.example.BackendProject.repository.ProductoRepository;
import com.example.BackendProject.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DevolucionService {

    private final DevolucionRepository devolucionRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
        private final DetalleDevolucionService detalleDevolucionService;
    private final DetallePedidoRepository detallePedidoRepository;

    public List<Devolucion> listarDevoluciones() {
        return devolucionRepository.findAll();
    }

    public List<Devolucion> listarDevolucionesPorUsuario(Long usuarioId) {
        return devolucionRepository.findByUsuario_Id(usuarioId);
    }

    public Devolucion obtenerDevolucion(Long id) {
        return devolucionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolución no encontrada"));
    }

        public Devolucion crearDevolucion(DevolucionDTO devolucionDTO) {
        Devolucion devolucion = new Devolucion();

        // Set campos simples
        devolucion.setFecha(devolucionDTO.getFecha());
        devolucion.setMotivo(devolucionDTO.getMotivo());
        devolucion.setDescripcion(devolucionDTO.getDescripcion());
        devolucion.setImporte_total(devolucionDTO.getImporte_total());
        devolucion.setEstado(devolucionDTO.getEstado());

        // Set relaciones
        if (devolucionDTO.getUsuario_id() != null) {
            Usuario usuario = usuarioRepository.findById(devolucionDTO.getUsuario_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            devolucion.setUsuario(usuario);
        }

        if (devolucionDTO.getPedido_id() != null) {
            Pedido pedido = pedidoRepository.findById(devolucionDTO.getPedido_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
            devolucion.setPedido(pedido);
        }

        // Procesar y asociar los detalles de la devolución
        if (devolucionDTO.getDetalles() != null && !devolucionDTO.getDetalles().isEmpty()) {
            for (DetalleDevolucionDTO detalleDTO : devolucionDTO.getDetalles()) {
                Detalle_Devolucion detalle = new Detalle_Devolucion();
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setMotivo_detalle(detalleDTO.getMotivo_detalle());

                Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con ID " + detalleDTO.getProductoId() + " no encontrado"));
                detalle.setProducto(producto);

                // Opcional: Relación con el detalle del pedido original
                if (detalleDTO.getDetallePedidoId() != null) {
                    Detalle_pedido detallePedido = detallePedidoRepository.findById(detalleDTO.getDetallePedidoId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de pedido con ID " + detalleDTO.getDetallePedidoId() + " no encontrado"));
                    detalle.setDetalle_pedido(detallePedido);
                }

                // Establecer la relación bidireccional
                devolucion.getDetalles().add(detalle);
                detalle.setDevolucion(devolucion);
            }
        }

        return devolucionRepository.save(devolucion);
    }

    public Devolucion actualizarDevolucion(Long id, DevolucionDTO devolucionDTO) {
        Devolucion devolucion = obtenerDevolucion(id);

        // 1. Actualizar campos simples de la devolución
        if (devolucionDTO.getFecha() != null) devolucion.setFecha(devolucionDTO.getFecha());
        if (devolucionDTO.getMotivo() != null) devolucion.setMotivo(devolucionDTO.getMotivo());
        if (devolucionDTO.getDescripcion() != null) devolucion.setDescripcion(devolucionDTO.getDescripcion());
        if (devolucionDTO.getImporte_total() != null) devolucion.setImporte_total(devolucionDTO.getImporte_total());
        if (devolucionDTO.getEstado() != null) devolucion.setEstado(devolucionDTO.getEstado());

        // 2. Sincronizar la lista de detalles
        if (devolucionDTO.getDetalles() != null) {
            // Mapa de detalles existentes para fácil acceso
            Map<Long, Detalle_Devolucion> detallesExistentesMap = devolucion.getDetalles().stream()
                    .collect(Collectors.toMap(Detalle_Devolucion::getId, Function.identity()));

            // Lista para los detalles actualizados
            List<Detalle_Devolucion> detallesActualizados = devolucionDTO.getDetalles().stream().map(detalleDTO -> {
                Detalle_Devolucion detalle;
                if (detalleDTO.getId() != null) {
                    // Es un detalle existente, lo actualizamos
                    detalle = detallesExistentesMap.get(detalleDTO.getId());
                    if (detalle == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El detalle con ID " + detalleDTO.getId() + " no pertenece a esta devolución.");
                    }
                } else {
                    // Es un detalle nuevo, lo creamos
                    detalle = new Detalle_Devolucion();
                    detalle.setDevolucion(devolucion); // Asociar con la devolución padre
                }

                // Actualizar propiedades del detalle
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setMotivo_detalle(detalleDTO.getMotivo_detalle());
                Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con ID " + detalleDTO.getProductoId() + " no encontrado"));
                detalle.setProducto(producto);

                return detalle;
            }).collect(Collectors.toList());

            // Reemplazamos la lista de detalles de la devolución con la nueva lista sincronizada
            devolucion.getDetalles().clear();
            devolucion.getDetalles().addAll(detallesActualizados);
        }

        return devolucionRepository.save(devolucion);
    }

    public void eliminarDevolucion(Long id) {
        if (!devolucionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolución no encontrada");
        }
        devolucionRepository.deleteById(id);
    }

    // Métodos adicionales
    public List<Devolucion> listarDevolucionesPorEstado(Boolean estado) {
        return devolucionRepository.findByEstado(estado);
    }

    public List<Devolucion> listarDevolucionesPorPedido(Long pedidoId) {
        return devolucionRepository.findByPedido_Id(pedidoId);
    }
    
    @Transactional
    public Devolucion procesarDevolucion(Long devolucionId) {
        Devolucion devolucion = obtenerDevolucion(devolucionId);
        
        if (devolucion.getEstado()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La devolución ya ha sido procesada");
        }
        
        // Los detalles ya están cargados en la entidad gracias a la relación @OneToMany
        List<Detalle_Devolucion> detalles = devolucion.getDetalles();
        
        // Procesar cada detalle
        for (Detalle_Devolucion detalle : detalles) {
            Producto producto = detalle.getProducto();
            
            // Actualizar stock del producto
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
            
            // Si hay un detalle de pedido asociado, podríamos marcarlo como devuelto
            if (detalle.getDetalle_pedido() != null) {
                // Aquí podríamos añadir lógica adicional si es necesario
            }
        }
        
        // Marcar la devolución como procesada
        devolucion.setEstado(true);
        return devolucionRepository.save(devolucion);
    }
}