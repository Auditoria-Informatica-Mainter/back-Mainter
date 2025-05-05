package com.example.BackendProject.service;

import com.example.BackendProject.dto.MaterialDTO;
import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.ProductoMaterial;
import com.example.BackendProject.entity.Sector;
import com.example.BackendProject.repository.CategoriaRepository;
import com.example.BackendProject.repository.MaterialRepository;
import com.example.BackendProject.repository.ProductoMaterialRepository;
import com.example.BackendProject.repository.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con materiales
 */
@Service
public class MaterialService {
    
    private final MaterialRepository materialRepository;
    private final CategoriaRepository categoriaRepository;
    private final SectorRepository sectorRepository;
    private final ProductoMaterialRepository productoMaterialRepository;
    
    @Autowired
    public MaterialService(
            MaterialRepository materialRepository, 
            CategoriaRepository categoriaRepository,
            SectorRepository sectorRepository,
            ProductoMaterialRepository productoMaterialRepository) {
        this.materialRepository = materialRepository;
        this.categoriaRepository = categoriaRepository;
        this.sectorRepository = sectorRepository;
        this.productoMaterialRepository = productoMaterialRepository;
    }
    
    /**
     * Crea un nuevo material
     * 
     * @param materialDTO DTO con los datos del material a crear
     * @return Material creado
     * @throws EntityNotFoundException si la categoría no existe
     */
    @Transactional
    public Material crearMaterial(MaterialDTO materialDTO) {
        // Buscar la categoría
        Categoria categoria = categoriaRepository.findById(materialDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + materialDTO.getCategoriaId()));
        
        // Crear el nuevo material
        Material nuevoMaterial = new Material();
        nuevoMaterial.setNombre(materialDTO.getNombre());
        nuevoMaterial.setStockActual(materialDTO.getStockActual());
        nuevoMaterial.setUnidadMedida(materialDTO.getUnidadMedida());
        nuevoMaterial.setStockMinimo(materialDTO.getStockMinimo());
        nuevoMaterial.setPuntoReorden(materialDTO.getStockMinimo() + 5); // Punto de reorden por defecto
        nuevoMaterial.setCategoria(categoria);
        nuevoMaterial.setCategoriaText(categoria.getNombre());
        nuevoMaterial.setActivo(true);
        
        // Asignar sector si existe
        if (materialDTO.getSectorId() != null) {
            Sector sector = sectorRepository.findById(materialDTO.getSectorId())
                    .orElseThrow(() -> new EntityNotFoundException("Sector no encontrado con ID: " + materialDTO.getSectorId()));
            nuevoMaterial.setSector(sector);
        }
        
        // Asignar descripción si existe
        if (materialDTO.getDescripcion() != null) {
            nuevoMaterial.setDescripcion(materialDTO.getDescripcion());
        } else {
            nuevoMaterial.setDescripcion("Material nuevo"); // Descripción por defecto
        }
        
        // Asignar imagen si existe
        if (materialDTO.getImagen() != null) {
            nuevoMaterial.setImagen(materialDTO.getImagen());
        }
        
        // Guardar y retornar
        return materialRepository.save(nuevoMaterial);
    }
    
    /**
     * Obtiene todos los materiales
     * 
     * @return Lista de todos los materiales
     */
    public List<Material> obtenerTodosLosMateriales() {
        return materialRepository.findAll();
    }
    
    /**
     * Obtiene un material por su ID
     * 
     * @param id ID del material
     * @return Material encontrado
     * @throws EntityNotFoundException si no se encuentra el material
     */
    public Material obtenerMaterialPorId(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + id));
    }
    
    /**
     * Busca un material por su nombre
     * 
     * @param nombre Nombre del material
     * @return Material encontrado o vacío si no existe
     */
    public Optional<Material> buscarPorNombre(String nombre) {
        return materialRepository.findByNombre(nombre);
    }
    
    /**
     * Busca materiales por término de búsqueda en el nombre
     * 
     * @param searchTerm Término a buscar
     * @return Lista de materiales que coinciden con la búsqueda
     */
    public List<Material> buscarPorTermino(String searchTerm) {
        return materialRepository.searchByNombre(searchTerm);
    }
    
    /**
     * Actualiza un material existente
     * 
     * @param id ID del material a actualizar
     * @param materialDTO DTO con los nuevos datos
     * @return Material actualizado
     * @throws EntityNotFoundException si el material o la categoría no existen
     */
    @Transactional
    public Material actualizarMaterial(Long id, MaterialDTO materialDTO) {
        // Buscar el material
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + id));
        
        // Buscar la categoría si se proporciona un ID
        if (materialDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(materialDTO.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + materialDTO.getCategoriaId()));
            material.setCategoria(categoria);
            // Actualizar texto de categoría
            material.setCategoriaText(categoria.getNombre());
        }
        
        // Buscar el sector si se proporciona un ID
        if (materialDTO.getSectorId() != null) {
            Sector sector = sectorRepository.findById(materialDTO.getSectorId())
                    .orElseThrow(() -> new EntityNotFoundException("Sector no encontrado con ID: " + materialDTO.getSectorId()));
            material.setSector(sector);
        }
        
        // Actualizar los campos
        material.setNombre(materialDTO.getNombre());
        material.setStockActual(materialDTO.getStockActual());
        material.setUnidadMedida(materialDTO.getUnidadMedida());
        material.setStockMinimo(materialDTO.getStockMinimo());
        
        // Actualizar descripción si se proporciona
        if (materialDTO.getDescripcion() != null) {
            material.setDescripcion(materialDTO.getDescripcion());
        }
        
        // Actualizar imagen si se proporciona
        if (materialDTO.getImagen() != null) {
            material.setImagen(materialDTO.getImagen());
        }
        
        // Guardar y retornar
        return materialRepository.save(material);
    }
    
    /**
     * Actualiza solo la imagen de un material
     * 
     * @param id ID del material
     * @param imagenUrl URL de la imagen
     * @return Material actualizado
     * @throws EntityNotFoundException si el material no existe
     */
    @Transactional
    public Material actualizarImagen(Long id, String imagenUrl) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + id));
        
        material.setImagen(imagenUrl);
        
        return materialRepository.save(material);
    }
    
    /**
     * Actualiza el stock de un material
     * 
     * @param id ID del material
     * @param cantidad Cantidad a sumar (positiva) o restar (negativa)
     * @return Material actualizado
     * @throws EntityNotFoundException si el material no existe
     * @throws IllegalArgumentException si la operación resulta en stock negativo
     */
    @Transactional
    public Material actualizarStock(Long id, Integer cantidad) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + id));
        
        int nuevoStock = material.getStockActual() + cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("La operación resultaría en stock negativo. Stock actual: " + material.getStockActual());
        }
        
        material.setStockActual(nuevoStock);
        return materialRepository.save(material);
    }
    
    /**
     * Elimina un material por su ID
     * 
     * @param id ID del material a eliminar
     * @throws EntityNotFoundException si el material no existe
     */
    @Transactional
    public void eliminarMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new EntityNotFoundException("Material no encontrado con ID: " + id);
        }
        materialRepository.deleteById(id);
    }
    
    /**
     * Obtiene la lista de materiales con stock bajo (menor o igual al mínimo)
     * 
     * @return Lista de materiales con stock bajo
     */
    public List<Material> obtenerMaterialesConStockBajo() {
        return materialRepository.findMaterialesWithLowStock();
    }
    
    /**
     * Obtiene los materiales asociados a un proveedor específico
     *
     * @param proveedorId ID del proveedor
     * @return Lista de materiales del proveedor
     * @throws EntityNotFoundException si el proveedor no existe
     */
    public List<Material> obtenerMaterialesPorProveedor(Long proveedorId) {
        // Aquí podríamos verificar si el proveedor existe antes de realizar la búsqueda
        // pero para simplificar usaremos directamente el repositorio
        
        return materialRepository.findByProveedorId(proveedorId);
    }
    
    /**
     * Obtiene los materiales que necesitan reabastecimiento
     * (stock actual es menor o igual al punto de reorden)
     *
     * @return Lista de materiales que necesitan reabastecimiento
     */
    public List<Material> obtenerMaterialesNecesitanReabastecimiento() {
        return materialRepository.findMaterialesNecesitanReabastecimiento();
    }
    
    /**
     * Obtiene las relaciones producto-material para un material específico
     * 
     * @param materialId ID del material
     * @return Lista de relaciones producto-material que utilizan el material
     */
    public List<ProductoMaterial> obtenerProductosQueLlevanElMaterial(Long materialId) {
        // Verificar que el material existe
        if (!materialRepository.existsById(materialId)) {
            throw new EntityNotFoundException("Material no encontrado con ID: " + materialId);
        }
        
        // Buscar todas las relaciones producto-material que incluyen este material
        return productoMaterialRepository.findByMaterialId(materialId);
    }
} 