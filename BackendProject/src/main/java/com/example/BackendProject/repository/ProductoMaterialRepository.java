package com.example.BackendProject.repository;

import com.example.BackendProject.entity.ProductoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de acceso a datos de la relación ProductoMaterial
 */
@Repository
public interface ProductoMaterialRepository extends JpaRepository<ProductoMaterial, Long> {
    
    /**
     * Busca relaciones por producto
     */
    List<ProductoMaterial> findByProductoId(Long productoId);
    
    /**
     * Busca relaciones por material
     */
    List<ProductoMaterial> findByMaterialId(Long materialId);
    
    /**
     * Elimina todas las relaciones de un producto
     */
    void deleteByProductoId(Long productoId);
    
    /**
     * Verifica si existe una relación entre un producto y un material
     */
    boolean existsByProductoIdAndMaterialId(Long productoId, Long materialId);
} 