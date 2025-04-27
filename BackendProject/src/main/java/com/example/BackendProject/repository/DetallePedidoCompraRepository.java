package com.example.BackendProject.repository;

import com.example.BackendProject.entity.DetallePedidoCompra;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones con la entidad DetallePedidoCompra
 */
@Repository
public interface DetallePedidoCompraRepository extends JpaRepository<DetallePedidoCompra, Long> {
    /**
     * Busca detalles de pedido por pedido
     * @param pedido el pedido al que pertenecen los detalles
     * @return lista de detalles del pedido especificado
     */
    List<DetallePedidoCompra> findByPedido(Pedido pedido);
    
    /**
     * Busca detalles de pedido por ID de pedido
     * @param pedidoId el ID del pedido
     * @return lista de detalles del pedido especificado
     */
    List<DetallePedidoCompra> findByPedidoId(Long pedidoId);
    
    /**
     * Busca detalles de pedido por material
     * @param material el material incluido en los detalles
     * @return lista de detalles que incluyen el material especificado
     */
    List<DetallePedidoCompra> findByMaterial(Material material);
    
    /**
     * Busca detalles de pedido por ID de material
     * @param materialId el ID del material
     * @return lista de detalles que incluyen el material especificado
     */
    List<DetallePedidoCompra> findByMaterialId(Long materialId);
    
    /**
     * Busca detalles de pedido por estado
     * @param estado el estado de los detalles
     * @return lista de detalles con el estado especificado
     */
    List<DetallePedidoCompra> findByEstado(String estado);
} 