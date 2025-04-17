
package com.example.BackendProject.repository;

import com.example.BackendProject.entity.SubCategoria;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long> {

    Optional<SubCategoria> findByNombre(String nombre);

}
