package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de acceso a datos de Producto
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Busca un producto por su nombre
     */
    Optional<Producto> findByNombre(String nombre);
    
    /**
     * Busca productos con stock menor o igual que un valor dado
     */
    List<Producto> findByStockLessThanEqual(Integer cantidad);
    
    /**
     * Busca productos con stock menor o igual que su stock mínimo
     */
    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stock_minimo")
    List<Producto> findProductosWithLowStock();
    
    /**
     * Busca productos por categoría
     */
    List<Producto> findByCategoriaId(Long categoriaId);
    
    /**
     * Busca productos que contengan el término de búsqueda en el nombre o descripción
     */
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Producto> searchByNombreOrDescripcion(@Param("searchTerm") String searchTerm);
} 