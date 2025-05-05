package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    List<Plano> findByProductoId(Long productoId);
    List<Plano> findByPreProductoId(Long preProductoId);
    void deleteByProductoId(Long productoId);
    void deleteByPreProductoId(Long preProductoId);
}