package com.example.BackendProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BackendProject.entity.Metodo_pago;

public interface MetodoPagoRepository extends JpaRepository<Metodo_pago, Long>{
    Optional<Metodo_pago> findByNombre(String nombre);
}
