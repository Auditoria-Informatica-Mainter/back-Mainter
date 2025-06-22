package com.example.BackendProject.service;

import com.example.BackendProject.dto.DevolucionDTO;
import com.example.BackendProject.dto.DevolucionResponseDTO;
import com.example.BackendProject.dto.DetalleDevolucionResponseDTO;
import com.example.BackendProject.entity.Devolucion;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.DevolucionRepository;
import com.example.BackendProject.repository.PedidoRepository;
import com.example.BackendProject.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DevolucionService {

    private final DevolucionRepository devolucionRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<DevolucionResponseDTO> listarDevoluciones() {
        return devolucionRepository.findAll().stream()
                .map(this::convertirADevolucionResponseDTO)
                .collect(Collectors.toList());
    }

    public List<DevolucionResponseDTO> listarDevolucionesPorUsuario(Long usuarioId) {
        return devolucionRepository.findByUsuario_Id(usuarioId).stream()
                .map(this::convertirADevolucionResponseDTO)
                .collect(Collectors.toList());
    }

    public DevolucionResponseDTO obtenerDevolucion(Long id) {
        Devolucion devolucion = devolucionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolución no encontrada"));
        return convertirADevolucionResponseDTO(devolucion);
    }

    public DevolucionResponseDTO crearDevolucion(DevolucionDTO devolucionDTO) {
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

        Devolucion devolucionGuardada = devolucionRepository.save(devolucion);
        return convertirADevolucionResponseDTO(devolucionGuardada);
    }

    public DevolucionResponseDTO actualizarDevolucion(Long id, DevolucionDTO devolucionDTO) {
        Devolucion devolucion = devolucionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolución no encontrada"));

        // Actualizar campos simples de la devolución
        if (devolucionDTO.getFecha() != null) devolucion.setFecha(devolucionDTO.getFecha());
        if (devolucionDTO.getMotivo() != null) devolucion.setMotivo(devolucionDTO.getMotivo());
        if (devolucionDTO.getDescripcion() != null) devolucion.setDescripcion(devolucionDTO.getDescripcion());
        if (devolucionDTO.getImporte_total() != null) devolucion.setImporte_total(devolucionDTO.getImporte_total());
        if (devolucionDTO.getEstado() != null) devolucion.setEstado(devolucionDTO.getEstado());

        Devolucion devolucionActualizada = devolucionRepository.save(devolucion);
        return convertirADevolucionResponseDTO(devolucionActualizada);
    }

    public void eliminarDevolucion(Long id) {
        if (!devolucionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolución no encontrada");
        }
        devolucionRepository.deleteById(id);
    }

    // Métodos adicionales
    public List<DevolucionResponseDTO> listarDevolucionesPorEstado(Boolean estado) {
        return devolucionRepository.findByEstado(estado).stream()
                .map(this::convertirADevolucionResponseDTO)
                .collect(Collectors.toList());
    }

    public List<DevolucionResponseDTO> listarDevolucionesPorPedido(Long pedidoId) {
        return devolucionRepository.findByPedido_Id(pedidoId).stream()
                .map(this::convertirADevolucionResponseDTO)
                .collect(Collectors.toList());
    }

    // Método para convertir de entidad a DTO
    private DevolucionResponseDTO convertirADevolucionResponseDTO(Devolucion devolucion) {
        DevolucionResponseDTO.DevolucionResponseDTOBuilder builder = DevolucionResponseDTO.builder()
                .id(devolucion.getId())
                .fecha(devolucion.getFecha())
                .motivo(devolucion.getMotivo())
                .descripcion(devolucion.getDescripcion())
                .importe_total(devolucion.getImporte_total())
                .estado(devolucion.getEstado());

        // Datos del usuario
        if (devolucion.getUsuario() != null) {
            builder.usuarioId(devolucion.getUsuario().getId())
                   .usuarioNombre(devolucion.getUsuario().getNombre())
                   .usuarioEmail(devolucion.getUsuario().getEmail());
        }

        // Datos del pedido
        if (devolucion.getPedido() != null) {
            builder.pedidoId(devolucion.getPedido().getId())
                   .pedidoFecha(devolucion.getPedido().getFecha());
        }

        // Convertir detalles
        if (devolucion.getDetalles() != null && !devolucion.getDetalles().isEmpty()) {
            List<DetalleDevolucionResponseDTO> detallesDTO = devolucion.getDetalles().stream()
                    .map(detalle -> DetalleDevolucionResponseDTO.builder()
                            .id(detalle.getId())
                            .detallePedidoId(detalle.getDetalle_pedido() != null ? detalle.getDetalle_pedido().getId() : null)
                            .nombreProducto(detalle.getDetalle_pedido() != null && detalle.getDetalle_pedido().getProducto() != null 
                                    ? detalle.getDetalle_pedido().getProducto().getNombre() : "Producto no disponible")
                            .cantidad(detalle.getCantidad())
                            .precioUnitario(detalle.getDetalle_pedido() != null ? detalle.getDetalle_pedido().getPrecioUnitario() : 0.0)
                            .motivo_detalle(detalle.getMotivo_detalle())
                            .build())
                    .collect(Collectors.toList());
            builder.detalles(detallesDTO);
        }

        return builder.build();
    }
}