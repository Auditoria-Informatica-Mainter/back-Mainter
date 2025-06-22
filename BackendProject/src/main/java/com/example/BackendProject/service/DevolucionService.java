package com.example.BackendProject.service;

import com.example.BackendProject.dto.DevolucionDTO;
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

@Service
@Transactional
@RequiredArgsConstructor
public class DevolucionService {

    private final DevolucionRepository devolucionRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

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

        return devolucionRepository.save(devolucion);
    }

    public Devolucion actualizarDevolucion(Long id, DevolucionDTO devolucionDTO) {
        Devolucion devolucion = obtenerDevolucion(id);

        // Actualizar campos simples de la devolución
        if (devolucionDTO.getFecha() != null) devolucion.setFecha(devolucionDTO.getFecha());
        if (devolucionDTO.getMotivo() != null) devolucion.setMotivo(devolucionDTO.getMotivo());
        if (devolucionDTO.getDescripcion() != null) devolucion.setDescripcion(devolucionDTO.getDescripcion());
        if (devolucionDTO.getImporte_total() != null) devolucion.setImporte_total(devolucionDTO.getImporte_total());
        if (devolucionDTO.getEstado() != null) devolucion.setEstado(devolucionDTO.getEstado());

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
}