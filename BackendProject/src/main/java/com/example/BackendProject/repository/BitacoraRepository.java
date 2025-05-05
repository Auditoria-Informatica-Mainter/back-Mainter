package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Bitacora;
import com.example.BackendProject.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
    List<Bitacora> findByUsuario(Usuario usuario);
    List<Bitacora> findByAccion(String accion);
    List<Bitacora> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Bitacora> findByDireccionIp(String direccionIp);
}