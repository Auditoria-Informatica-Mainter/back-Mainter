package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones con la entidad Almacen
 */
@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    /**
     * Busca un almacen por nombre
     * @param nombre el nombre del almacen a buscar
     * @return el almacen si existe
     */
    Optional<Almacen> findByNombre(String nombre);
    
    /**
     * Busca almacenes con capacidad mayor a la especificada
     * @param capacidad la capacidad mínima a buscar
     * @return lista de almacenes que cumplen la condición
     */
    List<Almacen> findByCapacidadGreaterThan(Double capacidad);
} 