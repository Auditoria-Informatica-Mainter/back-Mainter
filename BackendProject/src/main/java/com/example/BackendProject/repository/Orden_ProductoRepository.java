package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Orden_Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Orden_ProductoRepository extends JpaRepository<Orden_Producto, Long> {
    // Métodos personalizados si se requieren
} 