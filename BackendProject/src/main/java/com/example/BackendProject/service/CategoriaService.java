package com.example.BackendProject.service;

import com.example.BackendProject.dto.CategoriaDTO;
import com.example.BackendProject.dto.SubCategoriaDTO;
import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.entity.Rol;
import com.example.BackendProject.entity.SubCategoria;
import com.example.BackendProject.repository.CategoriaRepository;
import com.example.BackendProject.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubCategoriaRepository SubCategoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria guardarCategoria(CategoriaDTO dto) {
        SubCategoria subCategoria = obtenerSubCategoria(dto.getSubCategoriaId());
        Categoria categoria = new Categoria(dto.getNombre(), dto.getDescripcion(), subCategoria);
        return categoriaRepository.save(categoria);
    }

    public Categoria modificarCategoria(Long id, CategoriaDTO dto) {
        Categoria categoria = obtenerCategoria(id);
        if (dto.getNombre() != null && !dto.getNombre().isEmpty()) {
            categoria.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null && !dto.getDescripcion().isEmpty()) {
            categoria.setDescripcion(dto.getDescripcion());
        }
        if (dto.getSubCategoriaId() != null) {
            SubCategoria SubCategoria = obtenerSubCategoria(dto.getSubCategoriaId());
            categoria.setSubcategoria(SubCategoria);
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria obtenerCategoria(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            return categoria.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la categoria con el id " + id);
        }
    }

    public SubCategoria obtenerSubCategoria(Long id) {
        Optional<SubCategoria> SubCategoria = SubCategoriaRepository.findById(id);
        if (SubCategoria.isPresent()) {
            return SubCategoria.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la SubCategoria con el id " + id);
        }
    }

    public Categoria obtenerRolnnombre(String nombre){
		Optional<Categoria> categoria = categoriaRepository.findByNombre(nombre);
		if (categoria.isPresent()) {
			return categoria.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el rol con el id" + nombre);
		}
	}
}
