package com.example.BackendProject.service;

import com.example.BackendProject.dto.CategoriaDTO;
import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.entity.SubCategoria;
import com.example.BackendProject.repository.CategoriaRepository;
import com.example.BackendProject.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de categorías.
 * Proporciona métodos para administrar las categorías de productos y materiales.
 */
@Service
public class CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    private final SubCategoriaRepository subCategoriaRepository;
    
    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, SubCategoriaRepository subCategoriaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.subCategoriaRepository = subCategoriaRepository;
    }
    
    /**
     * Obtiene todas las categorías
     * @return lista de categorías
     */
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
    
    /**
     * Obtiene una categoría por su ID
     * @param id el ID de la categoría
     * @return la categoría encontrada
     * @throws ResponseStatusException si no se encuentra la categoría
     */
    public Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Categoría no encontrada con ID: " + id));
    }
    
    /**
     * Obtiene una categoría por su nombre
     * @param nombre el nombre de la categoría
     * @return la categoría encontrada
     * @throws ResponseStatusException si no se encuentra la categoría
     */
    public Categoria obtenerCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Categoría no encontrada con nombre: " + nombre));
    }
    
    /**
     * Crea una nueva categoría
     * @param categoriaDTO datos de la nueva categoría
     * @return la categoría creada
     */
    public Categoria guardarCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria;
        
        if (categoriaDTO.getSubCategoriaId() != null) {
            SubCategoria subCategoria = subCategoriaRepository.findById(categoriaDTO.getSubCategoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                            "Subcategoría no encontrada con ID: " + categoriaDTO.getSubCategoriaId()));
            
            categoria = new Categoria(categoriaDTO.getNombre(), categoriaDTO.getDescripcion(), subCategoria);
        } else {
            categoria = new Categoria(categoriaDTO.getNombre(), categoriaDTO.getDescripcion());
        }
        
        categoria.setActivo(true);
        return categoriaRepository.save(categoria);
    }
    
    /**
     * Actualiza una categoría existente
     * @param id el ID de la categoría a actualizar
     * @param categoriaDTO los nuevos datos de la categoría
     * @return la categoría actualizada
     * @throws ResponseStatusException si no se encuentra la categoría
     */
    public Categoria modificarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = obtenerCategoria(id);
        
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        
        if (categoriaDTO.getSubCategoriaId() != null) {
            SubCategoria subCategoria = subCategoriaRepository.findById(categoriaDTO.getSubCategoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                            "Subcategoría no encontrada con ID: " + categoriaDTO.getSubCategoriaId()));
            
            categoria.setSubCategoria(subCategoria);
        }
        
        return categoriaRepository.save(categoria);
    }
    
    /**
     * Obtiene las categorías por subcategoría
     * @param subCategoriaId el ID de la subcategoría
     * @return lista de categorías de la subcategoría
     * @throws ResponseStatusException si no se encuentra la subcategoría
     */
    public List<Categoria> obtenerCategoriasPorSubCategoria(Long subCategoriaId) {
        SubCategoria subCategoria = subCategoriaRepository.findById(subCategoriaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Subcategoría no encontrada con ID: " + subCategoriaId));
        
        return categoriaRepository.findBySubCategoria(subCategoria);
    }
}
