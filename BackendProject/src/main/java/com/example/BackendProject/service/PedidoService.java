package com.example.BackendProject.service;

import com.example.BackendProject.dto.PedidoDTO;
import com.example.BackendProject.entity.Metodo_pago;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.MetodoPagoRepository;
import com.example.BackendProject.repository.PedidoRepository;
import com.example.BackendProject.repository.UsuarioRepository;
import com.example.BackendProject.service.PedidoService;
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
}
