package com.example.BackendProject.service;

import com.example.BackendProject.dto.PreMaquinariaDTO;
import com.example.BackendProject.entity.PreMaquinaria;
import com.example.BackendProject.entity.Maquinaria;
import com.example.BackendProject.entity.Pre_producto;
import com.example.BackendProject.repository.PreMaquinariaRepository;
import com.example.BackendProject.repository.MaquinariaRepository;
import com.example.BackendProject.repository.PreProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class PreMaquinariaService {

    @Autowired
    private PreMaquinariaRepository preMaquinariaRepository;
    
    @Autowired
    private MaquinariaRepository maquinariaRepository;
    
    @Autowired
    private PreProductoRepository preProductoRepository;

    // Crear nueva planificación de maquinaria
    public PreMaquinaria crearPreMaquinaria(PreMaquinariaDTO dto) {
        // Verificar que la maquinaria existe
        Maquinaria maquinaria = maquinariaRepository.findById(dto.getMaquinariaId())
            .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada con ID: " + dto.getMaquinariaId()));
        
        // Verificar que el pre-producto existe
        Pre_producto preProducto = preProductoRepository.findById(dto.getPreProductoId())
            .orElseThrow(() -> new RuntimeException("Pre-producto no encontrado con ID: " + dto.getPreProductoId()));
        
        // Verificar si ya existe esta planificación
        if (preMaquinariaRepository.existsByPreProductoIdAndMaquinariaId(dto.getPreProductoId(), dto.getMaquinariaId())) {
            throw new RuntimeException("Ya existe una planificación para esta maquinaria en este producto");
        }
        
        // Crear nueva pre-maquinaria
        PreMaquinaria preMaquinaria = new PreMaquinaria();
        preMaquinaria.setCantidad(dto.getCantidad());
        preMaquinaria.setDescripcion(dto.getDescripcion());
        preMaquinaria.setTiempoEstimado(dto.getTiempoEstimado());
        preMaquinaria.setMaquinaria(maquinaria);
        preMaquinaria.setPreProducto(preProducto);
        
        return preMaquinariaRepository.save(preMaquinaria);
    }

    // Obtener todas las planificaciones
    public List<PreMaquinaria> getAllPreMaquinarias() {
        return preMaquinariaRepository.findAll();
    }

    // Obtener planificaciones por pre-producto
    public List<PreMaquinaria> getPreMaquinariasPorProducto(Long preProductoId) {
        return preMaquinariaRepository.findByPreProductoId(preProductoId);
    }

    // Obtener planificaciones por maquinaria
    public List<PreMaquinaria> getPreMaquinariasPorMaquinaria(Long maquinariaId) {
        return preMaquinariaRepository.findByMaquinariaId(maquinariaId);
    }

    // Obtener maquinarias requeridas para un producto
    public List<PreMaquinaria> getMaquinariasRequeridas(Long preProductoId) {
        return preMaquinariaRepository.findMaquinariasRequeridas(preProductoId);
    }

    // Obtener productos que requieren una maquinaria específica
    public List<PreMaquinaria> getProductosQueRequierenMaquinaria(Long maquinariaId) {
        return preMaquinariaRepository.findProductosQueRequierenMaquinaria(maquinariaId);
    }

    // Calcular tiempo total estimado para un producto
    public Integer calcularTiempoTotalEstimado(Long preProductoId) {
        return preMaquinariaRepository.calcularTiempoTotalEstimado(preProductoId).orElse(0);
    }

    // Obtener resumen de planificación para un producto
    public Map<String, Object> getResumenPlanificacion(Long preProductoId) {
        List<PreMaquinaria> planificaciones = getPreMaquinariasPorProducto(preProductoId);
        Integer tiempoTotal = calcularTiempoTotalEstimado(preProductoId);
        
        Pre_producto preProducto = preProductoRepository.findById(preProductoId)
            .orElseThrow(() -> new RuntimeException("Pre-producto no encontrado con ID: " + preProductoId));
        
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("preProducto", preProducto);
        resumen.put("planificaciones", planificaciones);
        resumen.put("totalMaquinarias", planificaciones.size());
        resumen.put("tiempoTotalEstimado", tiempoTotal);
        
        return resumen;
    }

    // Actualizar planificación
    public PreMaquinaria actualizarPreMaquinaria(Long id, PreMaquinariaDTO dto) {
        PreMaquinaria preMaquinaria = preMaquinariaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pre-maquinaria no encontrada con ID: " + id));
        
        // Actualizar maquinaria si cambió
        if (dto.getMaquinariaId() != null && !dto.getMaquinariaId().equals(preMaquinaria.getMaquinaria().getId())) {
            Maquinaria nuevaMaquinaria = maquinariaRepository.findById(dto.getMaquinariaId())
                .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada con ID: " + dto.getMaquinariaId()));
            preMaquinaria.setMaquinaria(nuevaMaquinaria);
        }
        
        // Actualizar pre-producto si cambió
        if (dto.getPreProductoId() != null && !dto.getPreProductoId().equals(preMaquinaria.getPreProducto().getId())) {
            Pre_producto nuevoPreProducto = preProductoRepository.findById(dto.getPreProductoId())
                .orElseThrow(() -> new RuntimeException("Pre-producto no encontrado con ID: " + dto.getPreProductoId()));
            preMaquinaria.setPreProducto(nuevoPreProducto);
        }
        
        // Actualizar otros campos
        if (dto.getCantidad() != null) {
            preMaquinaria.setCantidad(dto.getCantidad());
        }
        if (dto.getDescripcion() != null) {
            preMaquinaria.setDescripcion(dto.getDescripcion());
        }
        if (dto.getTiempoEstimado() != null) {
            preMaquinaria.setTiempoEstimado(dto.getTiempoEstimado());
        }
        
        return preMaquinariaRepository.save(preMaquinaria);
    }

    // Eliminar planificación
    public void eliminarPreMaquinaria(Long id) {
        if (!preMaquinariaRepository.existsById(id)) {
            throw new RuntimeException("Pre-maquinaria no encontrada con ID: " + id);
        }
        preMaquinariaRepository.deleteById(id);
    }

    // Buscar por descripción
    public List<PreMaquinaria> buscarPorDescripcion(String descripcion) {
        return preMaquinariaRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }

    // Verificar si existe planificación para una combinación
    public boolean existePlanificacion(Long preProductoId, Long maquinariaId) {
        return preMaquinariaRepository.existsByPreProductoIdAndMaquinariaId(preProductoId, maquinariaId);
    }

    // Duplicar planificación de un producto a otro
    public List<PreMaquinaria> duplicarPlanificacion(Long preProductoOrigenId, Long preProductoDestinoId) {
        Pre_producto preProductoDestino = preProductoRepository.findById(preProductoDestinoId)
            .orElseThrow(() -> new RuntimeException("Pre-producto destino no encontrado con ID: " + preProductoDestinoId));
        
        List<PreMaquinaria> planificacionesOrigen = getPreMaquinariasPorProducto(preProductoOrigenId);
        
        return planificacionesOrigen.stream()
            .map(original -> {
                PreMaquinaria nueva = new PreMaquinaria();
                nueva.setCantidad(original.getCantidad());
                nueva.setDescripcion(original.getDescripcion());
                nueva.setTiempoEstimado(original.getTiempoEstimado());
                nueva.setMaquinaria(original.getMaquinaria());
                nueva.setPreProducto(preProductoDestino);
                return preMaquinariaRepository.save(nueva);
            })
            .toList();
    }

    // Obtener estadísticas de uso de maquinarias
    public Map<String, Object> getEstadisticasUsoMaquinarias() {
        List<Object[]> maquinariasMasUtilizadas = preMaquinariaRepository.findMaquinariasMasUtilizadas();
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("maquinariasMasUtilizadas", maquinariasMasUtilizadas);
        estadisticas.put("totalPlanificaciones", preMaquinariaRepository.count());
        
        return estadisticas;
    }
} 