package com.example.BackendProject.service;

import com.example.BackendProject.dto.DetalleDevolucionDTO;
import com.example.BackendProject.dto.DetalleDevolucionResponseDTO;
import com.example.BackendProject.entity.Detalle_Devolucion;
import com.example.BackendProject.entity.Detalle_pedido;
import com.example.BackendProject.entity.Devolucion;
import com.example.BackendProject.repository.DetalleDevolucionRepository;
import com.example.BackendProject.repository.DetallePedidoRepository;
import com.example.BackendProject.repository.DevolucionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DetalleDevolucionService {

    private final DetalleDevolucionRepository detalleDevolucionRepository;
    private final DevolucionRepository devolucionRepository;
    private final DetallePedidoRepository detallePedidoRepository;



    public List<DetalleDevolucionResponseDTO> listarDetallesPorDevolucion(Long devolucionId) {
        if (!devolucionRepository.existsById(devolucionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolución no encontrada con ID: " + devolucionId);
        }
        return detalleDevolucionRepository.findByDevolucion_Id(devolucionId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public DetalleDevolucionResponseDTO obtenerDetalle(Long devolucionId, Long detalleId) {
        Detalle_Devolucion detalle = detalleDevolucionRepository.findById(detalleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de devolución no encontrado con ID: " + detalleId));

        if (!detalle.getDevolucion().getId().equals(devolucionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El detalle con ID " + detalleId + " no pertenece a la devolución con ID " + devolucionId);
        }
        return convertToResponseDTO(detalle);
    }

    @Transactional
    public DetalleDevolucionResponseDTO crearDetalle(Long devolucionId, DetalleDevolucionDTO detalleDevolucionDTO) {
        Devolucion devolucion = devolucionRepository.findById(devolucionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolución no encontrada"));

        Detalle_pedido detallePedido = detallePedidoRepository.findById(detalleDevolucionDTO.getDetallePedidoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de pedido no encontrado"));

        if (detalleDevolucionDTO.getCantidad() > detallePedido.getCantidad()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad a devolver no puede ser mayor que la cantidad del pedido.");
        }

        Detalle_Devolucion nuevoDetalle = Detalle_Devolucion.builder()
                .devolucion(devolucion)
                .detalle_pedido(detallePedido)
                .cantidad(detalleDevolucionDTO.getCantidad())
                .motivo_detalle(detalleDevolucionDTO.getMotivo_detalle())
                .build();

        Detalle_Devolucion guardado = detalleDevolucionRepository.save(nuevoDetalle);

        return convertToResponseDTO(guardado);
    }

    @Transactional
    public void eliminarDetalle(Long devolucionId, Long detalleId) {
        Detalle_Devolucion detalle = detalleDevolucionRepository.findById(detalleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de devolución no encontrado con ID: " + detalleId));

        if (!detalle.getDevolucion().getId().equals(devolucionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El detalle con ID " + detalleId + " no pertenece a la devolución con ID " + devolucionId);
        }

        detalleDevolucionRepository.deleteById(detalleId);
    }

    private DetalleDevolucionResponseDTO convertToResponseDTO(Detalle_Devolucion detalle) {
        return DetalleDevolucionResponseDTO.builder()
                .id(detalle.getId())
                .detallePedidoId(detalle.getDetalle_pedido().getId())
                .nombreProducto(detalle.getDetalle_pedido().getProducto().getNombre())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getDetalle_pedido().getProducto().getPrecioUnitario())
                .motivo_detalle(detalle.getMotivo_detalle())
                .build();
    }
}