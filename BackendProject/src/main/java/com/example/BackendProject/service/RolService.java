package com.example.BackendProject.service;

import com.example.BackendProject.entity.Rol;
import com.example.BackendProject.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {
	
	@Autowired
	private RolRepository rolRepository;
	
	public List<Rol> listarRoles(){
		List<Rol> roles = rolRepository.findAll();
		return roles;
	}
	
	public Rol guardarRol(String nombre, List<String> nombresPermisos) {
		Rol rol = new Rol();
		rol.setNombre(nombre);
		return rolRepository.save(rol);
	}
	
	public Rol modificarRol(Long id, String nombre, List<String> nombresPermisos) {
		Rol rol = obtenerRol(id);
	    if (nombre != null && !nombre.isEmpty()) {
	        rol.setNombre(nombre);
	    }
		return rolRepository.save(rol);
	}
	
	public Rol obtenerRol(Long id){
		Optional<Rol> rol = rolRepository.findById(id);
		if (rol.isPresent()) {
			return rol.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el rol con el id" + id);
		}
	}
	public Rol obtenerRolnnombre(String nombre){
		Optional<Rol> rol = rolRepository.findByNombre(nombre);
		if (rol.isPresent()) {
			return rol.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el rol con el id" + nombre);
		}
	}
	public Rol actualizarRol(Long id, Rol rolDto) {
		Rol rol = obtenerRol(id);
		rol.setNombre(rolDto.getNombre());
		return rolRepository.save(rol);
	}
	
}
