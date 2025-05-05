package com.example.BackendProject.service;

import com.example.BackendProject.dto.ReporteDTO;
import com.example.BackendProject.entity.*;
import com.example.BackendProject.repository.ReporteRepository;
import com.example.BackendProject.repository.CategoriaRepository;
import com.example.BackendProject.repository.SubCategoriaRepository;
import com.example.BackendProject.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private MaterialService materialService;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private SubCategoriaRepository subCategoriaRepository;
    
    @Autowired
    private MaterialRepository materialRepository;

    public List<Reporte> findAllReportes() { 
        return reporteRepository.findAll(); 
    }

    public Reporte findReporteById(Long id) { 
        return reporteRepository.findById(id).orElse(null); 
    }

    /**
     * Genera un reporte de stock para un producto específico
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param productoId ID del producto para el reporte
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return Reporte generado con información de stock
     */
    @Transactional
    public Reporte stockByProductId(Long userId, Long productoId, Date fechaInicio, Date fechaFin) {
        // Obtener el usuario que solicita el reporte
        Usuario usuario = usuarioService.obtenerUserPorId(userId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        
        // Obtener el producto
        Producto producto = productoService.obtenerProductoPorId(productoId);
        
        // Crear el reporte con la información requerida
        Reporte reporte = new Reporte();
        reporte.setSolictado_por(usuario);
        reporte.setProducto(producto);
        reporte.setFecha_Inicio(fechaInicio);
        reporte.setFecha_Fin(fechaFin);
        
        // Puedes agregar descripción adicional con información de stock
        StringBuilder descripcion = new StringBuilder();
        descripcion.append("Reporte de stock del producto: ").append(producto.getNombre()).append("\n");
        descripcion.append("Stock actual: ").append(producto.getStock()).append("\n");
        descripcion.append("Stock mínimo: ").append(producto.getStock_minimo()).append("\n");
        
        // Verificar si el stock está por debajo del mínimo
        boolean stockBajoMinimo = producto.getStock() <= producto.getStock_minimo();
        if (stockBajoMinimo) {
            descripcion.append("ALERTA: Stock por debajo del mínimo requerido!\n");
        }
        
        

        // Guardar la descripción en el reporte
        reporte.setDescripcion(descripcion.toString());
        
        // Guardar el reporte en la base de datos
        return reporteRepository.save(reporte);
    }
    
    /**
     * Genera un reporte de stock para todos los productos
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return Reporte generado con información de stock de todos los productos
     */
    @Transactional
    public ReporteDTO stockAllProducts(Long userId, Date fechaInicio, Date fechaFin) {
        // Obtener el usuario que solicita el reporte
        Usuario usuario = usuarioService.obtenerUserPorId(userId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        
        // Obtener todos los productos
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        
        // Crear el reporte con la información requerida
        Reporte reporte = new Reporte();
        reporte.setSolictado_por(usuario);
        reporte.setFecha_Inicio(fechaInicio);
        reporte.setFecha_Fin(fechaFin);
        
        // Generar descripción detallada
        StringBuilder descripcion = new StringBuilder();
        descripcion.append("REPORTE DE STOCK DE TODOS LOS PRODUCTOS\n");
        descripcion.append("Fecha de generación: ").append(new Date()).append("\n\n");
        
        // Contador de productos bajo stock
        int productosBajoStock = 0;
        
        // Iterar sobre todos los productos
        for (Producto producto : productos) {
            boolean isBajoStock = producto.getStock() <= producto.getStock_minimo();
            
            descripcion.append("Producto: ").append(producto.getNombre()).append("\n");
            descripcion.append("  - Stock actual: ").append(producto.getStock());
            
            if (isBajoStock) {
                descripcion.append(" [BAJO STOCK]");
                productosBajoStock++;
            }
            
            descripcion.append("\n  - Stock mínimo: ").append(producto.getStock_minimo()).append("\n");
            
            // Agregar materiales requeridos para este producto
            List<ProductoMaterial> materiales = productoService.obtenerMaterialesDeProducto(producto.getId());
            if (!materiales.isEmpty()) {
                descripcion.append("  - Materiales requeridos:\n");
                for (ProductoMaterial pm : materiales) {
                    descripcion.append("    * ").append(pm.getMaterial().getNombre())
                            .append(": ").append(pm.getCantidad()).append(" unidades\n");
                }
            }
            
            descripcion.append("\n");
        }
        
      
        
        // Guardar el reporte en la base de datos
        reporte = reporteRepository.save(reporte);
        
        // Convertir a DTO y configurar campos específicos
        ReporteDTO dto = convertToDto(reporte);
        dto.setTotalProductosBajoStock(productosBajoStock);
        
        return dto;
    }
    
    /**
     * Genera un reporte de stock para un material específico por categoría y subcategoría
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param materialId ID del material para el reporte
     * @param categoriaId ID de la categoría (opcional)
     * @param subcategoriaId ID de la subcategoría (opcional)
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return ReporteDTO con información de stock del material
     */
    @Transactional
    public ReporteDTO stockByMaterialCategoriaSubcategoria(Long userId, Long materialId, 
                                                        Long categoriaId, Long subcategoriaId,
                                                        Date fechaInicio, Date fechaFin) {
        // Obtener el usuario que solicita el reporte
        Usuario usuario = usuarioService.obtenerUserPorId(userId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        
        // Obtener el material
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material no encontrado con ID: " + materialId));
        
        // Verificar categoría y subcategoría si son proporcionados
        Categoria categoria = null;
        SubCategoria subcategoria = null;
        
        if (categoriaId != null) {
            categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriaId));
        }
        
        if (subcategoriaId != null) {
            subcategoria = subCategoriaRepository.findById(subcategoriaId)
                    .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada con ID: " + subcategoriaId));
        }
        
        // Validar que el material pertenece a la categoría/subcategoría proporcionada
        if (categoria != null && !material.getCategoria().getId().equals(categoriaId)) {
            throw new RuntimeException("El material no pertenece a la categoría especificada");
        }
        
        if (subcategoria != null && (categoria == null || !categoria.getSubCategoria().getId().equals(subcategoriaId))) {
            throw new RuntimeException("La categoría del material no pertenece a la subcategoría especificada");
        }
        
        // Crear el reporte con la información requerida
        Reporte reporte = new Reporte();
        reporte.setSolictado_por(usuario);
        reporte.setMaterial(material);
        reporte.setFecha_Inicio(fechaInicio);
        reporte.setFecha_Fin(fechaFin);
        
        // Generar descripción detallada
        StringBuilder descripcion = new StringBuilder();
        descripcion.append("REPORTE DE STOCK DE MATERIAL\n\n");
        descripcion.append("Material: ").append(material.getNombre()).append("\n");
        descripcion.append("Categoría: ").append(material.getCategoria().getNombre()).append("\n");
        
        if (material.getCategoria().getSubCategoria() != null) {
            descripcion.append("Subcategoría: ").append(material.getCategoria().getSubCategoria().getNombre()).append("\n");
        }
        
        descripcion.append("\nStock actual: ").append(material.getStockActual()).append("\n");
        descripcion.append("Stock mínimo: ").append(material.getStockMinimo()).append("\n");
        
        // Verificar si el stock está por debajo del mínimo
        if (material.getStockActual() <= material.getStockMinimo()) {
            descripcion.append("ALERTA: Stock por debajo del mínimo requerido!\n");
        }
        
        // Lista de productos que utilizan este material
        List<ProductoMaterial> usosProductos = materialService.obtenerProductosQueLlevanElMaterial(materialId);
        if (!usosProductos.isEmpty()) {
            descripcion.append("\nProductos que utilizan este material:\n");
            for (ProductoMaterial pm : usosProductos) {
                descripcion.append("- ").append(pm.getProducto().getNombre())
                        .append(" (").append(pm.getCantidad()).append(" unidades por producto)\n");
            }
        }
        
        // Guardar la descripción en el reporte
        reporte.setDescripcion(descripcion.toString());
        
        // Guardar el reporte en la base de datos
        reporte = reporteRepository.save(reporte);
        
        // Convertir a DTO
        ReporteDTO dto = convertToDto(reporte);
        
        return dto;
    }
    
    /**
     * Genera un reporte de stock para todos los materiales de una subcategoría
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param subcategoriaId ID de la subcategoría
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return ReporteDTO con información de stock de los materiales
     */
    @Transactional
    public ReporteDTO stockMaterialesBySubcategoria(Long userId, Long subcategoriaId, Date fechaInicio, Date fechaFin) {
        // Obtener el usuario que solicita el reporte
        Usuario usuario = usuarioService.obtenerUserPorId(userId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        
        // Obtener la subcategoría
        SubCategoria subcategoria = subCategoriaRepository.findById(subcategoriaId)
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada con ID: " + subcategoriaId));
        
        // Obtener todas las categorías que pertenecen a esta subcategoría
        List<Categoria> categorias = categoriaRepository.findBySubCategoria(subcategoria);
        
        // Crear el reporte con la información requerida
        Reporte reporte = new Reporte();
        reporte.setSolictado_por(usuario);
        reporte.setFecha_Inicio(fechaInicio);
        reporte.setFecha_Fin(fechaFin);
        
        // Generar descripción detallada
        StringBuilder descripcion = new StringBuilder();
        descripcion.append("REPORTE DE STOCK DE MATERIALES POR SUBCATEGORÍA\n\n");
        descripcion.append("Subcategoría: ").append(subcategoria.getNombre()).append("\n\n");
        
        int materialesBajoStock = 0;
        int totalMateriales = 0;
        
        for (Categoria categoria : categorias) {
            descripcion.append("Categoría: ").append(categoria.getNombre()).append("\n");
            
            // Obtener materiales de esta categoría
            List<Material> materiales = materialRepository.findByCategoriaId(categoria.getId());
            totalMateriales += materiales.size();
            
            if (materiales.isEmpty()) {
                descripcion.append("  No hay materiales en esta categoría\n\n");
                continue;
            }
            
            for (Material material : materiales) {
                boolean isBajoStock = material.getStockActual() <= material.getStockMinimo();
                
                descripcion.append("  - ").append(material.getNombre()).append("\n");
                descripcion.append("    Stock actual: ").append(material.getStockActual());
                
                if (isBajoStock) {
                    descripcion.append(" [BAJO STOCK]");
                    materialesBajoStock++;
                }
                
                descripcion.append("\n    Stock mínimo: ").append(material.getStockMinimo()).append("\n");
            }
            
            descripcion.append("\n");
        }
        
        // Agregar resumen
        descripcion.append("RESUMEN:\n");
        descripcion.append("Total de materiales: ").append(totalMateriales).append("\n");
        descripcion.append("Materiales con stock bajo: ").append(materialesBajoStock).append("\n");
        
        // Guardar la descripción en el reporte
        reporte.setDescripcion(descripcion.toString());
        
        // Guardar el reporte en la base de datos
        reporte = reporteRepository.save(reporte);
        
        // Convertir a DTO
        ReporteDTO dto = convertToDto(reporte);
        dto.setTotalProductosBajoStock(materialesBajoStock); // Reutilizamos este campo para materiales
        
        return dto;
    }
    
    /**
     * Genera un reporte de stock para todos los materiales de todas las subcategorías
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return ReporteDTO con información de stock de los materiales
     */
    @Transactional
    public ReporteDTO stockAllSubcategorias(Long userId, Date fechaInicio, Date fechaFin) {
        // Obtener el usuario que solicita el reporte
        Usuario usuario = usuarioService.obtenerUserPorId(userId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        
        // Obtener todas las subcategorías
        List<SubCategoria> subcategorias = subCategoriaRepository.findAll();
        
        // Crear el reporte con la información requerida
        Reporte reporte = new Reporte();
        reporte.setSolictado_por(usuario);
        reporte.setFecha_Inicio(fechaInicio);
        reporte.setFecha_Fin(fechaFin);
        
        // Generar descripción detallada
        StringBuilder descripcion = new StringBuilder();
        descripcion.append("REPORTE DE STOCK POR SUBCATEGORÍAS\n\n");
        descripcion.append("Fecha de generación: ").append(new Date()).append("\n\n");
        
        int materialesBajoStock = 0;
        int totalMateriales = 0;
        double valorTotalInventario = 0;
        
        for (SubCategoria subcategoria : subcategorias) {
            descripcion.append("SUBCATEGORÍA: ").append(subcategoria.getNombre()).append("\n");
            
            // Obtener categorías de esta subcategoría
            List<Categoria> categorias = categoriaRepository.findBySubCategoria(subcategoria);
            
            if (categorias.isEmpty()) {
                descripcion.append("  No hay categorías en esta subcategoría\n\n");
                continue;
            }
            
            int materialesSubcategoria = 0;
            
            for (Categoria categoria : categorias) {
                descripcion.append("  Categoría: ").append(categoria.getNombre()).append("\n");
                
                // Obtener materiales de esta categoría
                List<Material> materiales = materialRepository.findByCategoriaId(categoria.getId());
                materialesSubcategoria += materiales.size();
                totalMateriales += materiales.size();
                
                if (materiales.isEmpty()) {
                    descripcion.append("    No hay materiales en esta categoría\n");
                    continue;
                }
                
                for (Material material : materiales) {
                    boolean isBajoStock = material.getStockActual() <= material.getStockMinimo();
                    
                    descripcion.append("    - ").append(material.getNombre()).append("\n");
                    descripcion.append("      Stock actual: ").append(material.getStockActual());
                    
                    if (isBajoStock) {
                        descripcion.append(" [BAJO STOCK]");
                        materialesBajoStock++;
                    }
                    
                    descripcion.append("\n      Stock mínimo: ").append(material.getStockMinimo()).append("\n");
                    
                    // Calcular valor del inventario de este material si tiene precio
                    if (material.getPrecio() != null) {
                        double valorMaterial = material.getPrecio() * material.getStockActual();
                        valorTotalInventario += valorMaterial;
                    }
                }
            }
            
            descripcion.append("  Total materiales en subcategoría: ").append(materialesSubcategoria).append("\n\n");
        }
        
        // Agregar resumen
        descripcion.append("RESUMEN GENERAL:\n");
        descripcion.append("Total de subcategorías: ").append(subcategorias.size()).append("\n");
        descripcion.append("Total de materiales: ").append(totalMateriales).append("\n");
        descripcion.append("Materiales con stock bajo: ").append(materialesBajoStock).append("\n");
        descripcion.append("Valor total estimado del inventario: ").append(String.format("%.2f", valorTotalInventario)).append("\n");
        
        // Guardar la descripción en el reporte
        reporte.setDescripcion(descripcion.toString());
        
        // Guardar el reporte en la base de datos
        reporte = reporteRepository.save(reporte);
        
        // Convertir a DTO
        ReporteDTO dto = convertToDto(reporte);
        dto.setTotalProductosBajoStock(materialesBajoStock);
        dto.setValorTotalInventario(valorTotalInventario);
        
        return dto;
    }
    
    /**
     * Convierte un Reporte a un ReporteDTO
     * 
     * @param reporte Reporte a convertir
     * @return ReporteDTO con la información formateada para el cliente
     */
    public ReporteDTO convertToDto(Reporte reporte) {
        ReporteDTO dto = new ReporteDTO();
        
        dto.setId(reporte.getId());
        dto.setFechaInicio(reporte.getFecha_Inicio());
        dto.setFechaFin(reporte.getFecha_Fin());
        dto.setDescripcion(reporte.getDescripcion());
        
        // Información del usuario
        if (reporte.getSolictado_por() != null) {
            dto.setUsuarioId(reporte.getSolictado_por().getId());
            dto.setNombreUsuario(reporte.getSolictado_por().getNombre());
            dto.setApellidoUsuario(reporte.getSolictado_por().getApellido());
        }
        
        // Información del producto
        if (reporte.getProducto() != null) {
            Producto producto = reporte.getProducto();
            dto.setProductoId(producto.getId());
            dto.setNombreProducto(producto.getNombre());
            dto.setStockActual(producto.getStock());
            dto.setStockMinimo(producto.getStock_minimo());
            dto.setStockBajoMinimo(producto.getStock() <= producto.getStock_minimo());
            
            // Para reportes de stock, intentamos extraer información adicional de la descripción
            if (reporte.getDescripcion() != null) {
                String desc = reporte.getDescripcion();
                
                // Verificamos si hay información sobre materiales suficientes
                dto.setMaterialesSuficientes(!desc.contains("No hay suficientes materiales"));
                
                // Extraer la producción posible
                try {
                    String produccionText = desc.substring(desc.lastIndexOf("Producción posible con materiales actuales:"));
                    String[] parts = produccionText.split("unidades");
                    String numberPart = parts[0].replaceAll("[^0-9]", "").trim();
                    if (!numberPart.isEmpty()) {
                        dto.setProduccionPosible(Integer.parseInt(numberPart));
                    }
                } catch (Exception e) {
                    // En caso de error, no establecemos este valor
                }
            }
        }
        
        // Información del material
        if (reporte.getMaterial() != null) {
            dto.setMaterialId(reporte.getMaterial().getId());
            dto.setNombreMaterial(reporte.getMaterial().getNombre());
        }
        
        // Información de la compra
        if (reporte.getCompra() != null) {
            dto.setCompraId(reporte.getCompra().getId());
            dto.setEstadoCompra(reporte.getCompra().getEstado());
        }
        
        return dto;
    }
    
    /**
     * Obtiene un reporte por su ID en formato DTO
     * 
     * @param id ID del reporte
     * @return ReporteDTO con la información del reporte
     */
    public ReporteDTO getReporteDTOById(Long id) {
        Reporte reporte = findReporteById(id);
        if (reporte == null) {
            return null;
        }
        return convertToDto(reporte);
    }
    
    /**
     * Obtiene todos los reportes en formato DTO
     * 
     * @return Lista de ReporteDTO
     */
    public List<ReporteDTO> getAllReportesDTO() {
        List<Reporte> reportes = findAllReportes();
        return reportes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Genera un reporte de stock para un producto específico y devuelve el DTO
     * 
     * @param userId ID del usuario que solicita el reporte
     * @param productoId ID del producto para el reporte
     * @param fechaInicio Fecha de inicio para el período del reporte
     * @param fechaFin Fecha de fin para el período del reporte
     * @return ReporteDTO con la información de stock
     */
    @Transactional
    public ReporteDTO generateStockReportDTO(Long userId, Long productoId, Date fechaInicio, Date fechaFin) {
        Reporte reporte = stockByProductId(userId, productoId, fechaInicio, fechaFin);
        return convertToDto(reporte);
    }
}
