package com.example.BackendProject.service;

import com.example.BackendProject.dto.PedidoDTO;
import com.example.BackendProject.dto.PedidoResponseDTO;
import com.example.BackendProject.entity.Metodo_pago;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.MetodoPagoRepository;
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
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final UsuarioRepository usuarioRepository;

   
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    
    public Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    
    public Pedido crearPedido(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();

        // Set campos simples
        pedido.setFecha(pedidoDTO.getFecha());
        pedido.setDescripcion(pedidoDTO.getDescripcion());
        pedido.setImporte_total(pedidoDTO.getImporte_total());
        pedido.setImporte_total_desc(pedidoDTO.getImporte_total_desc());
        pedido.setEstado(pedidoDTO.getEstado());

        // Set relaciones
        Metodo_pago metodoPago = metodoPagoRepository.findById(pedidoDTO.getMetodo_pago_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));
        pedido.setMetodo_pago(metodoPago);

        Usuario usuario = usuarioRepository.findById(pedidoDTO.getUsuario_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        pedido.setUsuario(usuario);

        return pedidoRepository.save(pedido);
    }

    
    public Pedido actualizarPedido(Long id, PedidoDTO pedidoDTO) {

        
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        pedidoExistente.setFecha(pedidoDTO.getFecha());
        pedidoExistente.setDescripcion(pedidoDTO.getDescripcion());
        pedidoExistente.setImporte_total(pedidoDTO.getImporte_total());
        pedidoExistente.setImporte_total_desc(pedidoDTO.getImporte_total_desc());
        pedidoExistente.setEstado(pedidoDTO.getEstado());

        if (pedidoDTO.getMetodo_pago_id() != null) {
            Metodo_pago metodoPago = metodoPagoRepository.findById(pedidoDTO.getMetodo_pago_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));
            
            pedidoExistente.setMetodo_pago(metodoPago);
        }
        
        if (pedidoDTO.getUsuario_id() != null) {
    
            Usuario usuario = usuarioRepository.findById(pedidoDTO.getUsuario_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            pedidoExistente.setUsuario(usuario);
        }
        
        return pedidoRepository.save(pedidoExistente);
        
    }

    
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        pedidoRepository.delete(pedido);
    }

    
    public List<Pedido> buscarPorEstado(Boolean estado) {
        return pedidoRepository.findByEstado(estado);
    }

    
    public List<Pedido> buscarPorMetodoPago(Long metodoPagoId) {
        metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));
        return pedidoRepository.findByMetodo_pagoId(metodoPagoId);
    }

    
    public List<Pedido> buscarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return pedidoRepository.findByUsuario(usuario);
    }

    public Pedido cambiarEstadoPedido(Long id, Boolean estado) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del pedido es requerido");
        }
        if (estado == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado es requerido");
        }
        
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }    /**
     * Convierte una entidad Pedido a PedidoResponseDTO con toda la información necesaria
     */
    public PedidoResponseDTO convertirAPedidoResponseDTO(Pedido pedido) {
        // Debug logging para verificar si las relaciones están cargadas
        System.out.println("=== DEBUG PEDIDO ===");
        System.out.println("Pedido ID: " + pedido.getId());
        System.out.println("Método de pago nulo: " + (pedido.getMetodo_pago() == null));
        System.out.println("Usuario nulo: " + (pedido.getUsuario() == null));
        
        if (pedido.getMetodo_pago() != null) {
            System.out.println("Método de pago ID: " + pedido.getMetodo_pago().getId());
            System.out.println("Método de pago nombre: " + pedido.getMetodo_pago().getNombre());
        }
        
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .descripcion(pedido.getDescripcion())
                .importe_total(pedido.getImporte_total())
                .importe_total_desc(pedido.getImporte_total_desc())
                .estado(pedido.getEstado())
                // Información del usuario
                .usuario_id(pedido.getUsuario() != null ? pedido.getUsuario().getId() : null)
                .usuario_nombre(pedido.getUsuario() != null ? pedido.getUsuario().getNombre() : null)
                .usuario_email(pedido.getUsuario() != null ? pedido.getUsuario().getEmail() : null)
                // Información del método de pago
                .metodo_pago_id(pedido.getMetodo_pago() != null ? pedido.getMetodo_pago().getId() : null)
                .metodo_pago_nombre(pedido.getMetodo_pago() != null ? pedido.getMetodo_pago().getNombre() : "Sin método de pago")
                .metodo_pago_descripcion(pedido.getMetodo_pago() != null ? pedido.getMetodo_pago().getDescripcion() : null)
                .build();
    }/**
     * Obtiene un pedido con toda la información para el frontend
     */
    public PedidoResponseDTO obtenerPedidoCompleto(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        return convertirAPedidoResponseDTO(pedido);
    }

    /**
     * Lista todos los pedidos con información completa para el frontend
     */
    public List<PedidoResponseDTO> listarPedidosCompletos() {
        List<Pedido> pedidos = pedidoRepository.findAllWithRelations();
        return pedidos.stream()
                .map(this::convertirAPedidoResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }    /**
     * Cambia el estado del pedido y retorna la información completa
     */
    public PedidoResponseDTO cambiarEstadoPedidoCompleto(Long id, Boolean estado) {
        Pedido pedido = cambiarEstadoPedido(id, estado);
        // Recargar con relaciones para obtener la información completa
        pedido = pedidoRepository.findByIdWithRelations(pedido.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        return convertirAPedidoResponseDTO(pedido);
    }
}
