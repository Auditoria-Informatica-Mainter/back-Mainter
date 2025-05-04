package com.example.BackendProject.service;

import com.example.BackendProject.dto.MaterialResumidoDTO;
import com.example.BackendProject.dto.ProveedorDTO;
import com.example.BackendProject.dto.ProveedorConMaterialesDTO;
import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.entity.ProveedorMaterial;
import com.example.BackendProject.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorService {
    
    private final ProveedorRepository proveedorRepository;
    
    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }
    
    /**
     * Guarda un proveedor en la base de datos
     * @param proveedorDTO El DTO con los datos del proveedor a guardar
     * @return El proveedor guardado con su ID asignado
     */
    public ProveedorDTO guardarProveedor(ProveedorDTO proveedorDTO) {
        Proveedor proveedor = convertirAEntidad(proveedorDTO);
        proveedor = proveedorRepository.save(proveedor);
        return convertirADTO(proveedor);
    }
    
    /**
     * Obtiene todos los proveedores
     * @return Lista de todos los proveedores en formato DTO
     */
    public List<ProveedorDTO> obtenerTodosLosProveedores() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return proveedores.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un proveedor por su ID
     * @param id ID del proveedor a buscar
     * @return Optional con el proveedor en formato DTO si existe
     */
    public Optional<ProveedorDTO> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    /**
     * Obtiene un proveedor con sus materiales por ID
     * @param id ID del proveedor a buscar
     * @return El proveedor con sus materiales
     * @throws ResponseStatusException si no se encuentra el proveedor
     */
    public ProveedorConMaterialesDTO obtenerProveedorConMateriales(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Proveedor no encontrado con ID: " + id));
        
        return convertirADTOConMateriales(proveedor);
    }
    
    /**
     * Obtiene un proveedor por su nombre
     * @param nombre Nombre del proveedor a buscar
     * @return Optional con el proveedor en formato DTO si existe
     */
    public Optional<ProveedorDTO> obtenerProveedorPorNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre)
                .map(this::convertirADTO);
    }
    
    /**
     * Busca proveedores cuyo nombre contiene el texto especificado
     * @param texto Texto a buscar en el nombre
     * @return Lista de proveedores en formato DTO que coinciden con la búsqueda
     */
    public List<ProveedorDTO> buscarProveedoresPorNombre(String texto) {
        List<Proveedor> proveedores = proveedorRepository.findByNombreContainingIgnoreCase(texto);
        return proveedores.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene los proveedores activos o inactivos
     * @param activo Estado del proveedor (activo o inactivo)
     * @return Lista de proveedores en formato DTO según su estado
     */
    public List<ProveedorDTO> obtenerProveedoresPorEstado(Boolean activo) {
        List<Proveedor> proveedores = proveedorRepository.findByActivo(activo);
        return proveedores.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza un proveedor existente
     * @param id ID del proveedor a actualizar
     * @param proveedorDTO Datos actualizados del proveedor
     * @return Optional con el proveedor actualizado o vacío si no existe
     */
    public Optional<ProveedorDTO> actualizarProveedor(Long id, ProveedorDTO proveedorDTO) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setNombre(proveedorDTO.getNombre());
                    proveedor.setRuc(proveedorDTO.getRuc());
                    proveedor.setDireccion(proveedorDTO.getDireccion());
                    proveedor.setTelefono(proveedorDTO.getTelefono());
                    proveedor.setEmail(proveedorDTO.getEmail());
                    proveedor.setPersonaContacto(proveedorDTO.getPersonaContacto());
                    proveedor.setActivo(proveedorDTO.getActivo());
                    return convertirADTO(proveedorRepository.save(proveedor));
                });
    }
    
    /**
     * Cambia el estado de un proveedor (activo/inactivo)
     * @param id ID del proveedor
     * @param activo Nuevo estado
     * @return Optional con el proveedor actualizado o vacío si no existe
     */
    public Optional<ProveedorDTO> cambiarEstadoProveedor(Long id, Boolean activo) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setActivo(activo);
                    return convertirADTO(proveedorRepository.save(proveedor));
                });
    }
    
    /**
     * Elimina un proveedor por su ID
     * @param id ID del proveedor a eliminar
     */
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
    
    /**
     * Convierte una entidad Proveedor a DTO
     * @param proveedor La entidad Proveedor
     * @return El DTO correspondiente
     */
    private ProveedorDTO convertirADTO(Proveedor proveedor) {
        return new ProveedorDTO(
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getRuc(),
                proveedor.getDireccion(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                proveedor.getPersonaContacto(),
                proveedor.getActivo()
        );
    }
    
    /**
     * Convierte un DTO a entidad Proveedor
     * @param proveedorDTO El DTO a convertir
     * @return La entidad Proveedor correspondiente
     */
    private Proveedor convertirAEntidad(ProveedorDTO proveedorDTO) {
        Proveedor proveedor = new Proveedor();
        // Solo establecer el ID si no es nulo (para actualizaciones)
        if(proveedorDTO.getId() != null) {
            proveedor.setId(proveedorDTO.getId());
        }
        proveedor.setNombre(proveedorDTO.getNombre());
        proveedor.setRuc(proveedorDTO.getRuc());
        proveedor.setDireccion(proveedorDTO.getDireccion());
        proveedor.setTelefono(proveedorDTO.getTelefono());
        proveedor.setEmail(proveedorDTO.getEmail());
        proveedor.setPersonaContacto(proveedorDTO.getPersonaContacto());
        proveedor.setActivo(proveedorDTO.getActivo() != null ? proveedorDTO.getActivo() : true);
        return proveedor;
    }
    
    /**
     * Convierte una entidad Proveedor a DTO con materiales
     * @param proveedor La entidad Proveedor
     * @return El DTO con materiales correspondiente
     */
    private ProveedorConMaterialesDTO convertirADTOConMateriales(Proveedor proveedor) {
        ProveedorConMaterialesDTO dto = new ProveedorConMaterialesDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setRuc(proveedor.getRuc());
        dto.setDireccion(proveedor.getDireccion());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEmail(proveedor.getEmail());
        dto.setPersonaContacto(proveedor.getPersonaContacto());
        dto.setActivo(proveedor.getActivo());
        
        // Convertir los materiales del proveedor
        List<MaterialResumidoDTO> materialesDTO = new ArrayList<>();
        for (ProveedorMaterial pm : proveedor.getMateriales()) {
            MaterialResumidoDTO materialDTO = new MaterialResumidoDTO();
            materialDTO.setId(pm.getMaterial().getId());
            materialDTO.setNombre(pm.getMaterial().getNombre());
            materialDTO.setUnidadMedida(pm.getMaterial().getUnidadMedida());
            materialDTO.setPrecio(pm.getMaterial().getPrecio());
            materialDTO.setStockActual(pm.getMaterial().getStockActual());
            materialDTO.setPrecioProveedor(pm.getPrecio());
            materialDTO.setCantidadMinima(pm.getCantidadMinima());
            materialesDTO.add(materialDTO);
        }
        dto.setMateriales(materialesDTO);
        
        return dto;
    }
} 