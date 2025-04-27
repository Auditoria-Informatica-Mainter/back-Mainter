package com.example.BackendProject.service;

import com.example.BackendProject.dto.AlmacenDTO;
import com.example.BackendProject.entity.Almacen;
import com.example.BackendProject.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de almacenes
 */
@Service
public class AlmacenService {
    
    private final AlmacenRepository almacenRepository;
    
    @Autowired
    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }
    
    /**
     * Obtiene todos los almacenes
     * @return lista de almacenes
     */
    public List<Almacen> listarAlmacenes() {
        return almacenRepository.findAll();
    }
    
    /**
     * Obtiene un almacén por su ID
     * @param id el ID del almacén
     * @return el almacén encontrado
     * @throws ResponseStatusException si no se encuentra el almacén
     */
    public Almacen obtenerAlmacen(Long id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Almacén no encontrado con ID: " + id));
    }
    
    /**
     * Crea un nuevo almacén
     * @param almacenDTO datos del nuevo almacén
     * @return el almacén creado
     */
    public Almacen crearAlmacen(AlmacenDTO almacenDTO) {
        Almacen almacen = new Almacen(almacenDTO.getNombre(), almacenDTO.getCapacidad());
        return almacenRepository.save(almacen);
    }
    
    /**
     * Actualiza un almacén existente
     * @param id el ID del almacén a actualizar
     * @param almacenDTO los nuevos datos del almacén
     * @return el almacén actualizado
     * @throws ResponseStatusException si no se encuentra el almacén
     */
    public Almacen actualizarAlmacen(Long id, AlmacenDTO almacenDTO) {
        Almacen almacen = obtenerAlmacen(id);
        
        almacen.setNombre(almacenDTO.getNombre());
        almacen.setCapacidad(almacenDTO.getCapacidad());
        
        return almacenRepository.save(almacen);
    }
    
    /**
     * Elimina un almacén
     * @param id el ID del almacén a eliminar
     * @throws ResponseStatusException si no se encuentra el almacén
     */
    public void eliminarAlmacen(Long id) {
        if (!almacenRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Almacén no encontrado con ID: " + id);
        }
        
        almacenRepository.deleteById(id);
    }
    
    /**
     * Busca un almacén por nombre
     * @param nombre el nombre del almacén
     * @return el almacén encontrado
     * @throws ResponseStatusException si no se encuentra el almacén
     */
    public Almacen buscarPorNombre(String nombre) {
        return almacenRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Almacén no encontrado con nombre: " + nombre));
    }
    
    /**
     * Busca almacenes con capacidad mayor a la especificada
     * @param capacidad la capacidad mínima
     * @return lista de almacenes que cumplen la condición
     */
    public List<Almacen> buscarPorCapacidadMayorQue(Double capacidad) {
        return almacenRepository.findByCapacidadGreaterThan(capacidad);
    }
} 