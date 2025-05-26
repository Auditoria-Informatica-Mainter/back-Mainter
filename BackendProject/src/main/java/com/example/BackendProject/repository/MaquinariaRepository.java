package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Maquinaria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long> {
    List<Maquinaria> findByNombreContainingIgnoreCase(String nombre);
    List<Maquinaria> findByDescripcionContainingIgnoreCase(String descripcion);
    List<Maquinaria> findByEstado(String estado);
}