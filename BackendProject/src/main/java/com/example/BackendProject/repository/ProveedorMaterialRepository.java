package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.entity.ProveedorMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorMaterialRepository extends JpaRepository<ProveedorMaterial, Long> {
    
    /**
     * Busca relaciones de proveedor-material por proveedor
     */
    List<ProveedorMaterial> findByProveedor(Proveedor proveedor);
    
    /**
     * Busca relaciones de proveedor-material por ID de proveedor
     */
    List<ProveedorMaterial> findByProveedorId(Long proveedorId);
    
    /**
     * Busca relaciones de proveedor-material por material
     */
    List<ProveedorMaterial> findByMaterial(Material material);
    
    /**
     * Busca relaciones de proveedor-material por ID de material
     */
    List<ProveedorMaterial> findByMaterialId(Long materialId);
    
    /**
     * Busca relaciones de proveedor-material por proveedor y material
     */
    Optional<ProveedorMaterial> findByProveedorAndMaterial(Proveedor proveedor, Material material);
    
    /**
     * Busca relaciones de proveedor-material por ID de proveedor y ID de material
     */
    Optional<ProveedorMaterial> findByProveedorIdAndMaterialId(Long proveedorId, Long materialId);
    
    /**
     * Encuentra el proveedor material con el menor precio para un material específico
     */
    @Query("SELECT pm FROM ProveedorMaterial pm WHERE pm.material = ?1 ORDER BY pm.precio ASC")
    List<ProveedorMaterial> findByMaterialOrderByPrecioAsc(Material material);
} 