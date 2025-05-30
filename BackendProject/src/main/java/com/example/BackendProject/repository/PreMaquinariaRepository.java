package com.example.BackendProject.repository;

import com.example.BackendProject.entity.PreMaquinaria;
import com.example.BackendProject.entity.Maquinaria;
import com.example.BackendProject.entity.Pre_producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreMaquinariaRepository extends JpaRepository<PreMaquinaria, Long> {
    
    // Buscar pre-maquinarias por pre-producto
    List<PreMaquinaria> findByPreProducto(Pre_producto preProducto);
    List<PreMaquinaria> findByPreProductoId(Long preProductoId);
    
    // Buscar pre-maquinarias por maquinaria
    List<PreMaquinaria> findByMaquinaria(Maquinaria maquinaria);
    List<PreMaquinaria> findByMaquinariaId(Long maquinariaId);
    
    // Verificar si una maquinaria específica está planificada para un producto
    Optional<PreMaquinaria> findByPreProductoAndMaquinaria(Pre_producto preProducto, Maquinaria maquinaria);
    Optional<PreMaquinaria> findByPreProductoIdAndMaquinariaId(Long preProductoId, Long maquinariaId);
    
    // Obtener todas las maquinarias requeridas para un producto
    @Query("SELECT pm FROM PreMaquinaria pm WHERE pm.preProducto.id = :preProductoId ORDER BY pm.id")
    List<PreMaquinaria> findMaquinariasRequeridas(@Param("preProductoId") Long preProductoId);
    
    // Buscar productos que requieren una maquinaria específica
    @Query("SELECT pm FROM PreMaquinaria pm WHERE pm.maquinaria.id = :maquinariaId ORDER BY pm.preProducto.nombre")
    List<PreMaquinaria> findProductosQueRequierenMaquinaria(@Param("maquinariaId") Long maquinariaId);
    
    // Calcular tiempo total estimado para un producto
    @Query("SELECT SUM(CAST(pm.tiempoEstimado AS integer)) FROM PreMaquinaria pm WHERE pm.preProducto.id = :preProductoId")
    Optional<Integer> calcularTiempoTotalEstimado(@Param("preProductoId") Long preProductoId);
    
    // Obtener maquinarias más utilizadas en planificaciones
    @Query("SELECT pm.maquinaria, COUNT(pm.maquinaria) as uso_count FROM PreMaquinaria pm GROUP BY pm.maquinaria ORDER BY uso_count DESC")
    List<Object[]> findMaquinariasMasUtilizadas();
    
    // Buscar pre-maquinarias por descripción
    List<PreMaquinaria> findByDescripcionContainingIgnoreCase(String descripcion);
    
    // Verificar si existe planificación para combinación específica
    boolean existsByPreProductoIdAndMaquinariaId(Long preProductoId, Long maquinariaId);
} 