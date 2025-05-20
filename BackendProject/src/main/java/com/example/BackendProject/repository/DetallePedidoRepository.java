package com.example.BackendProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BackendProject.entity.Detalle_pedido;

public interface DetallePedidoRepository extends JpaRepository<Detalle_pedido, Long>{
    List<Detalle_pedido> findByPedidoId(Long pedidoId);
    List<Detalle_pedido> findByProductoId(Long productoId);
}
