package com.example.BackendProject.service;

import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {
    
    private final ProveedorRepository proveedorRepository;
    
    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }
    
    /**
     * Guarda un proveedor en la base de datos
     * @param proveedor El proveedor a guardar
     * @return El proveedor guardado con su ID asignado
     */
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }
    
    /**
     * Obtiene todos los proveedores
     * @return Lista de todos los proveedores
     */
    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }
    
    /**
     * Obtiene un proveedor por su ID
     * @param id ID del proveedor a buscar
     * @return Optional con el proveedor si existe
     */
    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }
    
    /**
     * Obtiene un proveedor por su nombre
     * @param nombre Nombre del proveedor a buscar
     * @return Optional con el proveedor si existe
     */
    public Optional<Proveedor> obtenerProveedorPorNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre);
    }
    
    /**
     * Busca proveedores cuyo nombre contiene el texto especificado
     * @param texto Texto a buscar en el nombre
     * @return Lista de proveedores que coinciden con la búsqueda
     */
    public List<Proveedor> buscarProveedoresPorNombre(String texto) {
        return proveedorRepository.findByNombreContainingIgnoreCase(texto);
    }
    
    /**
     * Obtiene los proveedores activos o inactivos
     * @param activo Estado del proveedor (activo o inactivo)
     * @return Lista de proveedores según su estado
     */
    public List<Proveedor> obtenerProveedoresPorEstado(Boolean activo) {
        return proveedorRepository.findByActivo(activo);
    }
    
    /**
     * Obtiene los proveedores de una ciudad específica
     * @param ciudad Ciudad a filtrar
     * @return Lista de proveedores de la ciudad
     */
    public List<Proveedor> obtenerProveedoresPorCiudad(String ciudad) {
        return proveedorRepository.findByCiudad(ciudad);
    }
    
    /**
     * Obtiene los proveedores de un país específico
     * @param pais País a filtrar
     * @return Lista de proveedores del país
     */
    public List<Proveedor> obtenerProveedoresPorPais(String pais) {
        return proveedorRepository.findByPais(pais);
    }
    
    /**
     * Actualiza un proveedor existente
     * @param id ID del proveedor a actualizar
     * @param proveedorActualizado Datos actualizados del proveedor
     * @return Optional con el proveedor actualizado o vacío si no existe
     */
    public Optional<Proveedor> actualizarProveedor(Long id, Proveedor proveedorActualizado) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setNombre(proveedorActualizado.getNombre());
                    proveedor.setRuc(proveedorActualizado.getRuc());
                    proveedor.setDirecciones(proveedorActualizado.getDirecciones());
                    proveedor.setTelefonos(proveedorActualizado.getTelefonos());
                    proveedor.setEmails(proveedorActualizado.getEmails());
                    proveedor.setPersonaContacto(proveedorActualizado.getPersonaContacto());
                    proveedor.setCiudad(proveedorActualizado.getCiudad());
                    proveedor.setPais(proveedorActualizado.getPais());
                    proveedor.setActivo(proveedorActualizado.getActivo());
                    return proveedorRepository.save(proveedor);
                });
    }
    
    /**
     * Cambia el estado de un proveedor (activo/inactivo)
     * @param id ID del proveedor
     * @param activo Nuevo estado
     * @return Optional con el proveedor actualizado o vacío si no existe
     */
    public Optional<Proveedor> cambiarEstadoProveedor(Long id, Boolean activo) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setActivo(activo);
                    return proveedorRepository.save(proveedor);
                });
    }
    
    /**
     * Elimina un proveedor por su ID
     * @param id ID del proveedor a eliminar
     */
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
} 