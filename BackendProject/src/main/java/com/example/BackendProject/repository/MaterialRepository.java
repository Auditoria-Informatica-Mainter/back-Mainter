package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de acceso a datos de Material
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    /**
     * Busca un material por su nombre
     */
    Optional<Material> findByNombre(String nombre);
    
    /**
     * Busca materiales que contengan el texto especificado en su nombre
     */
    List<Material> findByNombreContainingIgnoreCase(String texto);
    
    /**
     * Busca materiales por estado activo
     */
    List<Material> findByActivo(Boolean activo);
    
    /**
     * Busca materiales por categoría
     */
    List<Material> findByCategoriaText(String categoria);
    
    /**
     * Busca materiales con stock por debajo del mínimo
     */
    @Query("SELECT m FROM Material m WHERE m.stockActual < m.stockMinimo")
    List<Material> findByStockBajoMinimo();
    
    /**
     * Busca materiales con stock menor o igual que un valor dado
     */
    List<Material> findByStockActualLessThanEqual(Integer cantidad);
    
    /**
     * Busca materiales con stock menor o igual que su stock mínimo
     */
    @Query("SELECT m FROM Material m WHERE m.stockActual <= m.stockMinimo")
    List<Material> findMaterialesWithLowStock();
    
    /**
     * Busca materiales por categoría
     */
    List<Material> findByCategoriaId(Long categoriaId);
    
    /**
     * Busca materiales que contengan el término de búsqueda en el nombre
     */
    @Query("SELECT m FROM Material m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Material> searchByNombre(@Param("searchTerm") String searchTerm);
    
    /**
     * Busca materiales por proveedor
     */
    @Query("SELECT m FROM Material m JOIN m.proveedores pm WHERE pm.proveedor.id = :proveedorId")
    List<Material> findByProveedorId(@Param("proveedorId") Long proveedorId);
    
    /**
     * Busca materiales que necesitan reabastecimiento (stock por debajo del punto de reorden)
     */
    @Query("SELECT m FROM Material m WHERE m.stockActual <= m.puntoReorden AND m.activo = true")
    List<Material> findMaterialesNecesitanReabastecimiento();
} 