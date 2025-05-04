package com.example.BackendProject.repository;

import com.example.BackendProject.entity.DetallePedidoCompra;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones con la entidad DetallePedidoCompra
 */
@Repository
public interface DetallePedidoCompraRepository extends JpaRepository<DetallePedidoCompra, Long> {
    /**
     * Busca detalles de pedido por compra
     * @param compra la compra a la que pertenecen los detalles
     * @return lista de detalles de la compra especificada
     */
    List<DetallePedidoCompra> findByCompra(Compra compra);
    
    /**
     * Busca detalles de pedido por ID de compra
     * @param compraId el ID de la compra
     * @return lista de detalles de la compra especificada
     */
    List<DetallePedidoCompra> findByCompraId(Long compraId);
    
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