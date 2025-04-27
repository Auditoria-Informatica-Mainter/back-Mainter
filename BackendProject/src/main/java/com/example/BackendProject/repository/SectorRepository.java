package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Almacen;
import com.example.BackendProject.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones con la entidad Sector
 */
@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    /**
     * Busca un sector por nombre
     * @param nombre el nombre del sector a buscar
     * @return el sector si existe
     */
    Optional<Sector> findByNombre(String nombre);
    
    /**
     * Busca sectores por tipo
     * @param tipo el tipo de sector a buscar
     * @return lista de sectores del tipo especificado
     */
    List<Sector> findByTipo(String tipo);
    
    /**
     * Busca sectores por almacén
     * @param almacen el almacén al que pertenecen los sectores
     * @return lista de sectores del almacén especificado
     */
    List<Sector> findByAlmacen(Almacen almacen);
    
    /**
     * Busca sectores por almacén ID
     * @param almacenId el ID del almacén
     * @return lista de sectores del almacén especificado
     */
    List<Sector> findByAlmacenId(Long almacenId);
    
    /**
     * Busca sectores con capacidad disponible (stock < capacidad_maxima)
     * @return lista de sectores con capacidad disponible
     */
    @Query("SELECT s FROM Sector s WHERE s.stock < s.capacidad_maxima")
    List<Sector> findByStockLessThanCapacidadMaxima();
} 