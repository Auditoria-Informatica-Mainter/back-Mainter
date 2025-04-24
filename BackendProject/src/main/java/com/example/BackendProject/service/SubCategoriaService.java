package com.example.BackendProject.service;

import com.example.BackendProject.dto.SubCategoriaDTO;
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

    public List<SubCategoria> listarSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    public SubCategoria guardarSubcategoria(SubCategoriaDTO dto) {
        SubCategoria subcategoria = new SubCategoria(dto.getNombre(), dto.getDescripcion());
        return subcategoriaRepository.save(subcategoria);
    }

    public SubCategoria modificarSubcategoria(Long id, SubCategoriaDTO dto) {
        SubCategoria subcategoria = obtenerSubcategoria(id);
        if (dto.getNombre() != null && !dto.getNombre().isEmpty()) {
            subcategoria.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null && !dto.getDescripcion().isEmpty()) {
            subcategoria.setDescripcion(dto.getDescripcion());
        }
        return subcategoriaRepository.save(subcategoria);
    }

    public SubCategoria obtenerSubcategoria(Long id) {
        Optional<SubCategoria> subcategoria = subcategoriaRepository.findById(id);
        if (subcategoria.isPresent()) {
            return subcategoria.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la subcategoría con el id " + id);
        }
    }

    public SubCategoria obtenerRolnnombre(String nombre) {
        Optional<SubCategoria> subcategoria = subcategoriaRepository.findByNombre(nombre);
        if (subcategoria.isPresent()) {
            return subcategoria.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la subcategoría con el nombre: " + nombre);
        }
    }
}
