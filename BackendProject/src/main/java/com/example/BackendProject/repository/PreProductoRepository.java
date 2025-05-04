package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Pre_producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PreProductoRepository extends JpaRepository<Pre_producto, Long> {
    Optional<Pre_producto> findByNombre(String nombre);
}