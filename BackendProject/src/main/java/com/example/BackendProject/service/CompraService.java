package com.example.BackendProject.service;

import com.example.BackendProject.config.LoggableAction;
import com.example.BackendProject.dto.CompraDTO;
import com.example.BackendProject.entity.Compra;
import com.example.BackendProject.entity.Proveedor;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.CompraRepository;
import com.example.BackendProject.repository.ProveedorRepository;
import com.example.BackendProject.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestión de compras
 */
@Service
public class CompraService {
    
    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    
    @Autowired
    public CompraService(
            CompraRepository compraRepository,
            ProveedorRepository proveedorRepository,
            UsuarioRepository usuarioRepository) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * Obtiene todas las compras
     * @return lista de compras
     */
    public List<Compra> listarCompras() {
        return compraRepository.findAll();
    }
    
    /**
     * Obtiene una compra por su ID
     * @param id el ID de la compra
     * @return la compra encontrada
     * @throws ResponseStatusException si no se encuentra la compra
     */
    public Compra obtenerCompra(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Compra no encontrada con ID: " + id));
    }
    
    /**
     * Crea una nueva compra
     * @param dto datos de la nueva compra
     * @return la compra creada
     * @throws ResponseStatusException si no se encuentra el proveedor o el usuario
     */
    @LoggableAction
    public Compra crearCompra(CompraDTO dto) {
        Proveedor proveedor = null;
        Usuario usuario = null;
        
        if (dto.getProveedorId() != null) {
            proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Proveedor no encontrado con ID: " + dto.getProveedorId()));
        }
        
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuario no encontrado con ID: " + dto.getUsuarioId()));
        }
        
        // Si no se proporciona fecha, usar la fecha actual
        Date fecha = dto.getFecha() != null ? dto.getFecha() : new Date();
        
        // Valores iniciales para importe_total e importe_descuento si no se proporcionan
        Double importeTotal = dto.getImporte_total() != null ? dto.getImporte_total() : 0.0;
        Double importeDescuento = dto.getImporte_descuento() != null ? dto.getImporte_descuento() : 0.0;
        
        Compra compra = new Compra(
                dto.getEstado() != null ? dto.getEstado() : "PENDIENTE",
                fecha,
                importeTotal,
                importeDescuento,
                proveedor,
                usuario
                
        );
        
        return compraRepository.save(compra);
    }
    
    /**
     * Actualiza una compra existente
     * @param id el ID de la compra a actualizar
     * @param dto los nuevos datos de la compra
     * @return la compra actualizada
     * @throws ResponseStatusException si no se encuentra la compra, el proveedor o el usuario
     */
    @LoggableAction
    public Compra actualizarCompra(Long id, CompraDTO dto) {
        Compra compra = obtenerCompra(id);
        
        if (dto.getEstado() != null) {
            compra.setEstado(dto.getEstado());
        }
        
        if (dto.getFecha() != null) {
            compra.setFecha(dto.getFecha());
        }
        
        if (dto.getImporte_total() != null) {
            compra.setImporte_total(dto.getImporte_total());
        }
        
        if (dto.getImporte_descuento() != null) {
            compra.setImporte_descuento(dto.getImporte_descuento());
        }
        
        if (dto.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Proveedor no encontrado con ID: " + dto.getProveedorId()));
            compra.setProveedor(proveedor);
        }
        
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuario no encontrado con ID: " + dto.getUsuarioId()));
            compra.setUsuario(usuario);
        }
        
        return compraRepository.save(compra);
    }
    
    /**
     * Elimina una compra
     * @param id el ID de la compra a eliminar
     * @throws ResponseStatusException si no se encuentra la compra
     */
    @LoggableAction
    public void eliminarCompra(Long id) {
        if (!compraRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Compra no encontrada con ID: " + id);
        }
        
        compraRepository.deleteById(id);
    }
    
    /**
     * Busca compras por estado
     * @param estado el estado a buscar
     * @return lista de compras con el estado especificado
     */
    public List<Compra> buscarPorEstado(String estado) {
        return compraRepository.findByEstado(estado);
    }
    
    /**
     * Busca compras por proveedor
     * @param proveedorId el ID del proveedor
     * @return lista de compras del proveedor
     */
    public List<Compra> buscarPorProveedor(Long proveedorId) {
        return compraRepository.findByProveedorId(proveedorId);
    }

    /**
 * Busca compras realizadas en un rango de fechas
 * @param fechaInicio la fecha de inicio del rango
 * @param fechaFin la fecha de fin del rango
 * @return lista de compras realizadas en ese rango
 */
public List<Compra> buscarComprasPorRangoFechas(Date fechaInicio, Date fechaFin) {
    return compraRepository.findByFechaBetween(fechaInicio, fechaFin);
}

public List<Compra> buscarPorProveedorYRangoFechas(Long proveedorId, Date fechaInicio, Date fechaFin) {
    return compraRepository.findByProveedorIdAndFechaBetween(proveedorId, fechaInicio, fechaFin);
}

/**
 * Busca compras que incluyen un material específico en un rango de fechas
 * @param materialId el ID del material
 * @param fechaInicio la fecha de inicio del rango
 * @param fechaFin la fecha de fin del rango
 * @return lista de compras que incluyen el material en el rango de fechas
 */
public List<Compra> buscarComprasPorMaterialYRangoFechas(Long materialId, Date fechaInicio, Date fechaFin) {
    // Primero obtenemos todas las compras en el rango de fechas
    List<Compra> comprasEnRango = compraRepository.findByFechaBetween(fechaInicio, fechaFin);
    
    // Filtramos las compras que incluyen el material especificado
    return comprasEnRango.stream()
            .filter(compra -> compra.getDetalles().stream()
                    .anyMatch(detalle -> detalle.getMaterial().getId().equals(materialId)))
            .collect(java.util.stream.Collectors.toList());
}

/**
 * Busca todas las compras en un rango de fechas con información de materiales
 * @param fechaInicio la fecha de inicio del rango
 * @param fechaFin la fecha de fin del rango
 * @return lista de todas las compras en el rango de fechas
 */
public List<Compra> buscarComprasTodosMaterialesEnRangoFechas(Date fechaInicio, Date fechaFin) {
    // Simplemente retornamos todas las compras en el rango de fechas
    // Los detalles de cada compra ya incluyen la información de los materiales
    return compraRepository.findByFechaBetween(fechaInicio, fechaFin);
}

} 