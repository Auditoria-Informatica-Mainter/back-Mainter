package com.example.BackendProject.service;

import com.example.BackendProject.dto.SubCategoriaDTO;
import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.entity.SubCategoria;
import com.example.BackendProject.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoriaService {

    @Autowired
    private SubCategoriaRepository subcategoriaRepository;

    // Listar todas las subcategorías
    public List<SubCategoria> listarSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    // Guardar una nueva subcategoría
    public SubCategoria guardarSubcategoria(SubCategoriaDTO dto) {
        SubCategoria subcategoria = new SubCategoria(dto.getNombre(), dto.getDescripcion());
        return subcategoriaRepository.save(subcategoria);
    }

    // Modificar una subcategoría existente
    public SubCategoria modificarSubcategoria(Long id, String nombre, String descripcion) {
        SubCategoria subcategoria = obtenerSubcategoria(id);
        if (nombre != null && !nombre.isEmpty()) {
            subcategoria.setNombre(nombre);
        }
        if (descripcion != null && !descripcion.isEmpty()) {
            subcategoria.setDescripcion(descripcion);
        }
        return subcategoriaRepository.save(subcategoria);
    }

    // Obtener una subcategoría por su ID
    public SubCategoria obtenerSubcategoria(Long id) {
        Optional<SubCategoria> subcategoria = subcategoriaRepository.findById(id);
        if (subcategoria.isPresent()) {
            return subcategoria.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la subcategoría con el id " + id);
        }
    }

    public SubCategoria obtenerRolnnombre(String nombre){
        Optional<SubCategoria> SubCategoria = subcategoriaRepository.findByNombre(nombre);
        if (SubCategoria.isPresent()) {
            return SubCategoria.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el rol con el id" + nombre);
        }
	}
}
