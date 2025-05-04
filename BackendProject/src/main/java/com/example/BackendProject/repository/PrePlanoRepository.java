package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Pre_plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrePlanoRepository extends JpaRepository<Pre_plano, Long> {
    List<Pre_plano> findByPreProductoId(Long preProductoId);
    List<Pre_plano> findByMaterialId(Long materialId);
    void deleteByPreProductoId(Long preProductoId);
}