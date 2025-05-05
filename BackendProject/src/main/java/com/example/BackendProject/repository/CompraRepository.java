package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Compra;
import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para operaciones con la entidad Compra
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    /**
     * Busca compras por estado
     * @param estado el estado de la compra a buscar
     * @return lista de compras con el estado especificado
     */
    List<Compra> findByEstado(String estado);
    
    /**
     * Busca compras por rango de fechas
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de compras en el rango de fechas
     */
    List<Compra> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    /**
     * Busca compras por proveedor
     * @param proveedor el proveedor de la compra
     * @return lista de compras del proveedor especificado
     */
    List<Compra> findByProveedor(Proveedor proveedor);
    
    /**
     * Busca compras por ID de proveedor
     * @param proveedorId el ID del proveedor
     * @return lista de compras del proveedor especificado
     */
    List<Compra> findByProveedorId(Long proveedorId);
    
    /**
     * Busca compras por usuario
     * @param usuario el usuario que realizó la compra
     * @return lista de compras del usuario especificado
     */
    List<Compra> findByUsuario(Usuario usuario);
    
    /**
     * Busca compras por ID de usuario
     * @param usuarioId el ID del usuario
     * @return lista de compras del usuario especificado
     */
    List<Compra> findByUsuarioId(Long usuarioId);
} 