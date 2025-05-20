package com.example.BackendProject.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BackendProject.entity.Metodo_pago;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByEstado(Boolean estado);
    List<Pedido> findByFechaBetween(Date desde, Date hasta);
    @Query("SELECT p FROM Pedido p WHERE p.metodo_pago.id = :metodoPagoId")
    List<Pedido> findByMetodo_pagoId(Long metodoPagoId);
    List<Pedido> findByUsuario_Id(Long usuarioId);
    List<Pedido> findByUsuario(Usuario usuario);
}
