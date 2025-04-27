package com.example.BackendProject.service;

import com.example.BackendProject.dto.SubCategoriaDTO;
import com.example.BackendProject.entity.SubCategoria;
import com.example.BackendProject.repository.CategoriaRepository;
import com.example.BackendProject.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de subcategorías
 */
@Service
public class SubCategoriaService {
    
    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    
    @Autowired
    public SubCategoriaService(SubCategoriaRepository subCategoriaRepository, 
                             CategoriaRepository categoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
        this.categoriaRepository = categoriaRepository;
    }
    
    /**
     * Obtener todas las subcategorías
     * @return lista de subcategorías
     */
    public List<SubCategoria> listarSubCategorias() {
        return subCategoriaRepository.findAll();
    }
    
    /**
     * Obtener una subcategoría por su ID
     * @param id el ID de la subcategoría
     * @return la subcategoría encontrada
     * @throws ResponseStatusException si no se encuentra la subcategoría
     */
    public SubCategoria obtenerSubCategoria(Long id) {
        return subCategoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Subcategoría no encontrada con ID: " + id));
    }
    
    /**
     * Crear una nueva subcategoría
     * @param subCategoriaDTO datos de la nueva subcategoría
     * @return la subcategoría creada
     */
    public SubCategoria crearSubCategoria(SubCategoriaDTO subCategoriaDTO) {
        SubCategoria subCategoria = new SubCategoria(
                subCategoriaDTO.getNombre(),
                subCategoriaDTO.getDescripcion()
        );
        return subCategoriaRepository.save(subCategoria);
    }
    
    /**
     * Actualizar una subcategoría existente
     * @param id el ID de la subcategoría a actualizar
     * @param subCategoriaDTO los nuevos datos de la subcategoría
     * @return la subcategoría actualizada
     * @throws ResponseStatusException si no se encuentra la subcategoría
     */
    public SubCategoria actualizarSubCategoria(Long id, SubCategoriaDTO subCategoriaDTO) {
        SubCategoria subCategoria = obtenerSubCategoria(id);
        
        subCategoria.setNombre(subCategoriaDTO.getNombre());
        subCategoria.setDescripcion(subCategoriaDTO.getDescripcion());
        
        return subCategoriaRepository.save(subCategoria);
    }
    
    /**
     * Eliminar una subcategoría
     * @param id el ID de la subcategoría a eliminar
     * @throws ResponseStatusException si no se encuentra la subcategoría
     */
    public void eliminarSubCategoria(Long id) {
        if (!subCategoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Subcategoría no encontrada con ID: " + id);
        }
        
        // Verificar si hay categorías asociadas
        SubCategoria subCategoria = obtenerSubCategoria(id);
        if (subCategoria.getCategorias() != null && !subCategoria.getCategorias().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "No se puede eliminar la subcategoría porque tiene categorías asociadas");
        }
        
        subCategoriaRepository.deleteById(id);
    }
    
    /**
     * Obtener las categorías asociadas a una subcategoría
     * @param id el ID de la subcategoría
     * @return lista de categorías asociadas
     * @throws ResponseStatusException si no se encuentra la subcategoría
     */
    public List<Object> obtenerCategoriasPorSubCategoria(Long id) {
        SubCategoria subCategoria = obtenerSubCategoria(id);
        
        return subCategoria.getCategorias().stream()
                .map(categoria -> {
                    return categoria;
                })
                .collect(Collectors.toList());
    }
}
