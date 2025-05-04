package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    /**
     * Busca un proveedor por nombre
     */
    Optional<Proveedor> findByNombre(String nombre);
    
    /**
     * Busca proveedores que contengan el texto especificado en su nombre
     */
    List<Proveedor> findByNombreContainingIgnoreCase(String texto);
    
    /**
     * Busca proveedores por estado activo
     */
    List<Proveedor> findByActivo(Boolean activo);
} 