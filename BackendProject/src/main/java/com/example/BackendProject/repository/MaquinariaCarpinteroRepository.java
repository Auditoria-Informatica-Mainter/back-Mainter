package com.example.BackendProject.repository;

import com.example.BackendProject.entity.MaquinariaCarpintero;
import com.example.BackendProject.entity.Maquinaria;
import com.example.BackendProject.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaquinariaCarpinteroRepository extends JpaRepository<MaquinariaCarpintero, Long> {
    
    // Buscar asignaciones por maquinaria
    List<MaquinariaCarpintero> findByMaquinaria(Maquinaria maquinaria);
    List<MaquinariaCarpintero> findByMaquinariaId(Long maquinariaId);
    
    // Buscar asignaciones por carpintero
    List<MaquinariaCarpintero> findByCarpintero(Usuario carpintero);
    List<MaquinariaCarpintero> findByCarpinteroId(Long carpinteroId);
    
    // Buscar por estado
    List<MaquinariaCarpintero> findByEstado(String estado);
    
    // Verificar si una maquinaria está asignada a un carpintero específico
    Optional<MaquinariaCarpintero> findByMaquinariaAndCarpintero(Maquinaria maquinaria, Usuario carpintero);
    Optional<MaquinariaCarpintero> findByMaquinariaIdAndCarpinteroId(Long maquinariaId, Long carpinteroId);
    
    // Buscar maquinarias disponibles (sin asignación activa)
    @Query("SELECT mc FROM MaquinariaCarpintero mc WHERE mc.maquinaria.id = :maquinariaId AND mc.estado = 'disponible'")
    List<MaquinariaCarpintero> findMaquinariaDisponible(@Param("maquinariaId") Long maquinariaId);
    
    // Buscar maquinarias en uso por carpintero
    @Query("SELECT mc FROM MaquinariaCarpintero mc WHERE mc.carpintero.id = :carpinteroId AND mc.estado = 'en_uso'")
    List<MaquinariaCarpintero> findMaquinariasEnUsoPorCarpintero(@Param("carpinteroId") Long carpinteroId);
    
    // Verificar si existe asignación activa para una maquinaria
    @Query("SELECT mc FROM MaquinariaCarpintero mc WHERE mc.maquinaria.id = :maquinariaId AND mc.estado IN ('en_uso', 'reservada')")
    Optional<MaquinariaCarpintero> findAsignacionActivaPorMaquinaria(@Param("maquinariaId") Long maquinariaId);
} 