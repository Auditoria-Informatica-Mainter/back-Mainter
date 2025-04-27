package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para operaciones con la entidad Pedido (Compra)
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    /**
     * Busca pedidos por estado
     * @param estado el estado del pedido a buscar
     * @return lista de pedidos con el estado especificado
     */
    List<Pedido> findByEstado(String estado);
    
    /**
     * Busca pedidos por rango de fechas
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de pedidos en el rango de fechas
     */
    List<Pedido> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    /**
     * Busca pedidos por proveedor
     * @param proveedor el proveedor del pedido
     * @return lista de pedidos del proveedor especificado
     */
    List<Pedido> findByProveedor(Proveedor proveedor);
    
    /**
     * Busca pedidos por ID de proveedor
     * @param proveedorId el ID del proveedor
     * @return lista de pedidos del proveedor especificado
     */
    List<Pedido> findByProveedorId(Long proveedorId);
    
    /**
     * Busca pedidos por usuario
     * @param usuario el usuario que realizó el pedido
     * @return lista de pedidos del usuario especificado
     */
    List<Pedido> findByUsuario(Usuario usuario);
    
    /**
     * Busca pedidos por ID de usuario
     * @param usuarioId el ID del usuario
     * @return lista de pedidos del usuario especificado
     */
    List<Pedido> findByUsuarioId(Long usuarioId);
} 