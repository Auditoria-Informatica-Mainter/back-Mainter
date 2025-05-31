package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Orden_PreProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Orden_PreProductoRepository extends JpaRepository<Orden_PreProducto, Long> {
    // Métodos personalizados si se requieren
} 