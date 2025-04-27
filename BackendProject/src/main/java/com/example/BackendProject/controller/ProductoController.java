package com.example.BackendProject.controller;

import com.example.BackendProject.dto.ProductoDTO;
import com.example.BackendProject.entity.Material;
import com.example.BackendProject.entity.Producto;
import com.example.BackendProject.entity.ProductoMaterial;
import com.example.BackendProject.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de productos.
 * Proporciona endpoints para la administración completa del ciclo de vida de productos,
 * incluyendo su creación, modificación, consulta y eliminación.
 * También gestiona la relación con materiales, control de stock y proceso de producción.
 * 
 * Características principales:
 * - CRUD completo de productos
 * - Gestión de materiales asociados (BOM)
 * - Control de stock y verificación de disponibilidad
 * - Registro de producción
 * - Manejo de imágenes de productos
 */
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    /**
     * Crea un nuevo producto
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Crear nuevo producto",
            description = "Crea un nuevo producto en el sistema con su lista de materiales",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Categoría o material no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto nuevoProducto = productoService.crearProducto(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene todos los productos
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los productos",
            description = "Retorna una lista con todos los productos registrados",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de productos",
                            content = @Content(schema = @Schema(implementation = Producto.class))
                    )
            }
    )
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }
    
    /**
     * Obtiene un producto por su ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener producto por ID",
            description = "Retorna un producto específico según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto encontrado"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
            }
    )
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(producto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Actualiza un producto existente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza la información de un producto existente incluyendo sus materiales",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto actualizado"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Producto, categoría o material no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, productoDTO);
            return ResponseEntity.ok(productoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Actualiza la imagen de un producto
     */
    @PutMapping("/{id}/imagen")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Actualizar imagen de producto",
            description = "Actualiza solo la URL de la imagen de un producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen actualizada"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Producto> actualizarImagen(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String imagenUrl = payload.get("imagen");
            if (imagenUrl == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Producto productoActualizado = productoService.actualizarImagen(id, imagenUrl);
            return ResponseEntity.ok(productoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Actualiza el stock de un producto
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ALMACEN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Actualizar stock de producto",
            description = "Aumenta o disminuye el stock de un producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock actualizado"),
                    @ApiResponse(responseCode = "400", description = "Operación inválida (resultaría en stock negativo)"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            Producto productoActualizado = productoService.actualizarStock(id, cantidad);
            return ResponseEntity.ok(productoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Elimina un producto
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto del sistema",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Producto eliminado"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtiene productos con stock bajo
     */
    @GetMapping("/bajo-stock")
    @Operation(
            summary = "Obtener productos con stock bajo",
            description = "Retorna una lista de productos con stock igual o inferior al mínimo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de productos con stock bajo",
                            content = @Content(schema = @Schema(implementation = Producto.class))
                    )
            }
    )
    public ResponseEntity<List<Producto>> obtenerProductosConStockBajo() {
        List<Producto> productosStockBajo = productoService.obtenerProductosConStockBajo();
        return ResponseEntity.ok(productosStockBajo);
    }
    
    /**
     * Obtiene los materiales de un producto
     */
    @GetMapping("/{id}/materiales")
    @Operation(
            summary = "Obtener materiales de un producto",
            description = "Retorna la lista de materiales necesarios para fabricar un producto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de materiales del producto",
                            content = @Content(schema = @Schema(implementation = ProductoMaterial.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
            }
    )
    public ResponseEntity<List<ProductoMaterial>> obtenerMaterialesDeProducto(@PathVariable Long id) {
        try {
            List<ProductoMaterial> materiales = productoService.obtenerMaterialesDeProducto(id);
            return ResponseEntity.ok(materiales);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verifica disponibilidad de materiales
     */
    @GetMapping("/{id}/verificar-disponibilidad")
    @Operation(
            summary = "Verificar disponibilidad de materiales",
            description = "Verifica si hay suficiente stock de materiales para producir una cantidad del producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resultado de la verificación"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
            }
    )
    public ResponseEntity<?> verificarDisponibilidadMateriales(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            boolean disponible = productoService.verificarDisponibilidadMateriales(id, cantidad);
            
            Map<String, Object> response = new HashMap<>();
            response.put("disponible", disponible);
            
            if (!disponible) {
                List<Material> faltantes = productoService.obtenerMaterialesFaltantes(id, cantidad);
                response.put("materialesFaltantes", faltantes);
            }
            
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Registra producción (consume materiales y aumenta stock del producto)
     */
    @PostMapping("/{id}/producir")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCCION')")
    @Operation(
            summary = "Registrar producción",
            description = "Registra la producción de una cantidad del producto, consumiendo materiales y actualizando stock",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producción registrada"),
                    @ApiResponse(responseCode = "400", description = "No hay suficiente stock de materiales"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    public ResponseEntity<?> registrarProduccion(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            // Verificar disponibilidad de materiales
            boolean disponible = productoService.verificarDisponibilidadMateriales(id, cantidad);
            
            if (!disponible) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "No hay suficiente stock de materiales para la producción");
                error.put("materialesFaltantes", productoService.obtenerMaterialesFaltantes(id, cantidad));
                return ResponseEntity.badRequest().body(error);
            }
            
            // Consumir materiales y actualizar stock
            productoService.consumirMaterialesParaProduccion(id, cantidad);
            
            // Obtener el producto actualizado
            Producto productoActualizado = productoService.obtenerProductoPorId(id);
            
            return ResponseEntity.ok(productoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Busca productos por término
     */
    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar productos",
            description = "Retorna productos que contienen el término de búsqueda en su nombre o descripción",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de productos encontrados",
                            content = @Content(schema = @Schema(implementation = Producto.class))
                    )
            }
    )
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam String q) {
        List<Producto> productosEncontrados = productoService.buscarPorTermino(q);
        return ResponseEntity.ok(productosEncontrados);
    }
} 