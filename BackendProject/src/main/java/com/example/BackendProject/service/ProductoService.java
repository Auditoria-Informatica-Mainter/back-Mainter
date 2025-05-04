package com.example.BackendProject.service;

import com.example.BackendProject.config.LoggableAction;
import com.example.BackendProject.dto.ProductoDTO;
import com.example.BackendProject.dto.ProductoMaterialDTO;
import com.example.BackendProject.entity.Categoria;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.entity.ProductoMaterial;
import com.example.BackendProject.repository.CategoriaRepository;
import com.example.BackendProject.repository.MaterialRepository;
import com.example.BackendProject.repository.ProductoMaterialRepository;
import com.example.BackendProject.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con productos
 */
@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MaterialRepository materialRepository;
    
    @Autowired
    private ProductoMaterialRepository productoMaterialRepository;
    
    /**
     * Crea un nuevo producto con su lista de materiales
     * 
     * @param productoDTO DTO con los datos del producto a crear y sus materiales
     * @return Producto creado
     * @throws EntityNotFoundException si la categoría o algún material no existe
     */
    @LoggableAction
    @Transactional
    public Producto crearProducto(ProductoDTO productoDTO) {
        // Buscar la categoría
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
        
        // Crear el nuevo producto
        Producto nuevoProducto = new Producto(
                productoDTO.getNombre(),
                productoDTO.getDescripcion(),
                productoDTO.getStock(),
                productoDTO.getStock_minimo(),
                productoDTO.getImagen(),
                categoria
        );
        
        // Guardar producto
        Producto productoGuardado = productoRepository.save(nuevoProducto);
        
        // Procesar materiales si se proporcionan
        if (productoDTO.getMateriales() != null && !productoDTO.getMateriales().isEmpty()) {
            for (ProductoMaterialDTO materialDTO : productoDTO.getMateriales()) {
                // Buscar el material
                Material material = materialRepository.findById(materialDTO.getMaterialId())
                        .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + materialDTO.getMaterialId()));
                
                // Crear relación producto-material
                ProductoMaterial productoMaterial = new ProductoMaterial(
                        productoGuardado,
                        material,
                        materialDTO.getCantidad()
                );
                
                // Guardar relación
                productoMaterialRepository.save(productoMaterial);
            }
        }
        
        // Recargar el producto con sus relaciones
        return productoRepository.findById(productoGuardado.getId()).orElse(productoGuardado);
    }
    
    /**
     * Obtiene todos los productos
     * 
     * @return Lista de todos los productos
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
    
    /**
     * Obtiene un producto por su ID
     * 
     * @param id ID del producto
     * @return Producto encontrado
     * @throws EntityNotFoundException si no se encuentra el producto
     */
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }
    
    /**
     * Busca un producto por su nombre
     * 
     * @param nombre Nombre del producto
     * @return Producto encontrado o vacío si no existe
     */
    public Optional<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }
    
    /**
     * Busca productos por término de búsqueda en el nombre o descripción
     * 
     * @param searchTerm Término a buscar
     * @return Lista de productos que coinciden con la búsqueda
     */
    public List<Producto> buscarPorTermino(String searchTerm) {
        return productoRepository.searchByNombreOrDescripcion(searchTerm);
    }
    
    /**
     * Actualiza un producto existente
     * 
     * @param id ID del producto a actualizar
     * @param productoDTO DTO con los nuevos datos
     * @return Producto actualizado
     * @throws EntityNotFoundException si el producto o la categoría no existen
     */
    @LoggableAction
    @Transactional
    public Producto actualizarProducto(Long id, ProductoDTO productoDTO) {
        // Buscar el producto
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        
        // Buscar la categoría si se proporciona un ID
        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        
        // Actualizar los campos
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setStock(productoDTO.getStock());
        producto.setStock_minimo(productoDTO.getStock_minimo());
        
        // Actualizar imagen solo si se proporciona
        if (productoDTO.getImagen() != null) {
            producto.setImagen(productoDTO.getImagen());
        }
        
        // Guardar los cambios del producto
        Producto productoActualizado = productoRepository.save(producto);
        
        // Actualizar los materiales si se proporcionan
        if (productoDTO.getMateriales() != null) {
            // Eliminar relaciones existentes
            productoMaterialRepository.deleteByProductoId(id);
            
            // Crear nuevas relaciones
            for (ProductoMaterialDTO materialDTO : productoDTO.getMateriales()) {
                // Buscar el material
                Material material = materialRepository.findById(materialDTO.getMaterialId())
                        .orElseThrow(() -> new EntityNotFoundException("Material no encontrado con ID: " + materialDTO.getMaterialId()));
                
                // Crear relación producto-material
                ProductoMaterial productoMaterial = new ProductoMaterial(
                        productoActualizado,
                        material,
                        materialDTO.getCantidad()
                );
                
                // Guardar relación
                productoMaterialRepository.save(productoMaterial);
            }
        }
        
        // Recargar el producto con sus relaciones
        return productoRepository.findById(productoActualizado.getId()).orElse(productoActualizado);
    }
    
    /**
     * Actualiza solo la imagen de un producto
     * 
     * @param id ID del producto
     * @param imagenUrl URL de la imagen
     * @return Producto actualizado
     * @throws EntityNotFoundException si el producto no existe
     */
    @LoggableAction
    @Transactional
    public Producto actualizarImagen(Long id, String imagenUrl) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        
        producto.setImagen(imagenUrl);
        return productoRepository.save(producto);
    }
    
    /**
     * Actualiza el stock de un producto
     * 
     * @param id ID del producto
     * @param cantidad Cantidad a sumar (positiva) o restar (negativa)
     * @return Producto actualizado
     * @throws EntityNotFoundException si el producto no existe
     * @throws IllegalArgumentException si la operación resulta en stock negativo
     */
    @LoggableAction
    @Transactional
    public Producto actualizarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("La operación resultaría en stock negativo. Stock actual: " + producto.getStock());
        }
        
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }
    
    /**
     * Elimina un producto por su ID
     * 
     * @param id ID del producto a eliminar
     * @throws EntityNotFoundException si el producto no existe
     */
    @LoggableAction
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
        
        // Primero eliminar las relaciones con materiales
        productoMaterialRepository.deleteByProductoId(id);
        
        // Luego eliminar el producto
        productoRepository.deleteById(id);
    }
    
    /**
     * Obtiene la lista de productos con stock bajo (menor o igual al mínimo)
     * 
     * @return Lista de productos con stock bajo
     */
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findProductosWithLowStock();
    }
    
    /**
     * Obtiene la lista de materiales necesarios para un producto
     * 
     * @param productoId ID del producto
     * @return Lista de relaciones producto-material
     * @throws EntityNotFoundException si el producto no existe
     */
    public List<ProductoMaterial> obtenerMaterialesDeProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + productoId);
        }
        
        return productoMaterialRepository.findByProductoId(productoId);
    }
    
    /**
     * Verifica si hay suficiente stock de materiales para producir una cantidad de un producto
     * 
     * @param productoId ID del producto
     * @param cantidad Cantidad a producir
     * @return true si hay suficiente stock, false si no
     * @throws EntityNotFoundException si el producto no existe
     */
    public boolean verificarDisponibilidadMateriales(Long productoId, Integer cantidad) {
        // Obtener los materiales necesarios para el producto
        List<ProductoMaterial> materialesNecesarios = obtenerMaterialesDeProducto(productoId);
        
        // Verificar cada material
        for (ProductoMaterial pm : materialesNecesarios) {
            // Cantidad necesaria total = cantidad por unidad * unidades a producir
            int cantidadNecesaria = pm.getCantidad() * cantidad;
            
            // Verificar si hay suficiente stock
            if (pm.getMaterial().getStockActual() < cantidadNecesaria) {
                return false;
            }
        }
        
        // Si llegamos aquí, hay suficiente stock de todos los materiales
        return true;
    }
    
    /**
     * Obtiene la lista de materiales faltantes para producir una cantidad de producto
     * 
     * @param productoId ID del producto
     * @param cantidad Cantidad a producir
     * @return Lista de materiales que no tienen suficiente stock
     * @throws EntityNotFoundException si el producto no existe
     */
    public List<Material> obtenerMaterialesFaltantes(Long productoId, Integer cantidad) {
        // Obtener los materiales necesarios para el producto
        List<ProductoMaterial> materialesNecesarios = obtenerMaterialesDeProducto(productoId);
        List<Material> materialesFaltantes = new ArrayList<>();
        
        // Verificar cada material
        for (ProductoMaterial pm : materialesNecesarios) {
            // Cantidad necesaria total = cantidad por unidad * unidades a producir
            int cantidadNecesaria = pm.getCantidad() * cantidad;
            
            // Verificar si hay suficiente stock
            if (pm.getMaterial().getStockActual() < cantidadNecesaria) {
                materialesFaltantes.add(pm.getMaterial());
            }
        }
        
        return materialesFaltantes;
    }
    
    /**
     * Consume los materiales necesarios para producir una cantidad de producto
     * 
     * @param productoId ID del producto
     * @param cantidad Cantidad a producir
     * @throws EntityNotFoundException si el producto no existe
     * @throws IllegalArgumentException si no hay suficiente stock de algún material
     */
    @Transactional
    public void consumirMaterialesParaProduccion(Long productoId, Integer cantidad) {
        // Verificar disponibilidad
        if (!verificarDisponibilidadMateriales(productoId, cantidad)) {
            throw new IllegalArgumentException("No hay suficiente stock de materiales para la producción");
        }
        
        // Obtener los materiales necesarios para el producto
        List<ProductoMaterial> materialesNecesarios = obtenerMaterialesDeProducto(productoId);
        
        // Consumir cada material
        for (ProductoMaterial pm : materialesNecesarios) {
            Material material = pm.getMaterial();
            int cantidadNecesaria = pm.getCantidad() * cantidad;
            
            // Actualizar stock del material
            material.setStockActual(material.getStockActual() - cantidadNecesaria);
            materialRepository.save(material);
        }
        
        // Actualizar stock del producto
        actualizarStock(productoId, cantidad);
    }
}