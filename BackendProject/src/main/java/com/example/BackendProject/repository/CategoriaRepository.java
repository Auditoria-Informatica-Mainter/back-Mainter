package com.example.BackendProject.repository;

import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.entity.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones con la entidad Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    /**
     * Busca una categoría por nombre.
     * @param nombre el nombre de la categoría
     * @return la categoría si existe
     */
    Optional<Categoria> findByNombre(String nombre);
    
    /**
     * Busca categorías que contengan el texto especificado en su nombre
     */
    List<Categoria> findByNombreContainingIgnoreCase(String texto);
    
    /**
     * Busca categorías por estado activo
     */
    List<Categoria> findByActivo(Boolean activo);
    
    /**
     * Busca categorías por subcategoría.
     * @param subCategoria la subcategoría a buscar
     * @return lista de categorías de la subcategoría
     */
    List<Categoria> findBySubCategoria(SubCategoria subCategoria);
}