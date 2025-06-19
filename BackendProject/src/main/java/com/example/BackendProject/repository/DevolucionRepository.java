package com.example.BackendProject.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BackendProject.entity.Devolucion;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.entity.Pedido;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {
    List<Devolucion> findByEstado(Boolean estado);
    List<Devolucion> findByFechaBetween(Date desde, Date hasta);
    List<Devolucion> findByUsuario_Id(Long usuarioId);
    List<Devolucion> findByUsuario(Usuario usuario);
    List<Devolucion> findByPedido_Id(Long pedidoId);
    List<Devolucion> findByPedido(Pedido pedido);
}