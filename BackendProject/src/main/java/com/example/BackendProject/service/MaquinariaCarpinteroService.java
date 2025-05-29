package com.example.BackendProject.service;

import com.example.BackendProject.dto.MaquinariaCarpinteroDTO;
import com.example.BackendProject.entity.MaquinariaCarpintero;
import com.example.BackendProject.entity.Maquinaria;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.MaquinariaCarpinteroRepository;
import com.example.BackendProject.repository.MaquinariaRepository;
import com.example.BackendProject.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaquinariaCarpinteroService {

    @Autowired
    private MaquinariaCarpinteroRepository maquinariaCarpinteroRepository;
    
    @Autowired
    private MaquinariaRepository maquinariaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Asignar carpintero a maquinaria
    public MaquinariaCarpintero asignarCarpinteroAMaquinaria(Long maquinariaId, Long carpinteroId, String estado) {
        // Verificar que la maquinaria existe
        Maquinaria maquinaria = maquinariaRepository.findById(maquinariaId)
            .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada con ID: " + maquinariaId));
        
        // Verificar que el carpintero existe
        Usuario carpintero = usuarioRepository.findById(carpinteroId)
            .orElseThrow(() -> new RuntimeException("Carpintero no encontrado con ID: " + carpinteroId));
        
        // Verificar si ya existe una asignación activa para esta maquinaria
        Optional<MaquinariaCarpintero> asignacionExistente = 
            maquinariaCarpinteroRepository.findAsignacionActivaPorMaquinaria(maquinariaId);
        
        if (asignacionExistente.isPresent() && !"disponible".equals(estado)) {
            throw new RuntimeException("La maquinaria ya está asignada a otro carpintero");
        }
        
        // Crear nueva asignación
        MaquinariaCarpintero nuevaAsignacion = new MaquinariaCarpintero();
        nuevaAsignacion.setMaquinaria(maquinaria);
        nuevaAsignacion.setCarpintero(carpintero);
        nuevaAsignacion.setEstado(estado);
        
        return maquinariaCarpinteroRepository.save(nuevaAsignacion);
    }

    // Cambiar estado de asignación
    public MaquinariaCarpintero cambiarEstadoAsignacion(Long asignacionId, String nuevoEstado) {
        MaquinariaCarpintero asignacion = maquinariaCarpinteroRepository.findById(asignacionId)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + asignacionId));
        
        asignacion.setEstado(nuevoEstado);
        return maquinariaCarpinteroRepository.save(asignacion);
    }

    // Liberar maquinaria (cambiar estado a disponible)
    public MaquinariaCarpintero liberarMaquinaria(Long maquinariaId) {
        Optional<MaquinariaCarpintero> asignacionActiva = 
            maquinariaCarpinteroRepository.findAsignacionActivaPorMaquinaria(maquinariaId);
        
        if (asignacionActiva.isPresent()) {
            MaquinariaCarpintero asignacion = asignacionActiva.get();
            asignacion.setEstado("disponible");
            return maquinariaCarpinteroRepository.save(asignacion);
        }
        
        throw new RuntimeException("No hay asignación activa para esta maquinaria");
    }

    // Obtener todas las asignaciones
    public List<MaquinariaCarpintero> getAllAsignaciones() {
        return maquinariaCarpinteroRepository.findAll();
    }

    // Obtener asignaciones por maquinaria
    public List<MaquinariaCarpintero> getAsignacionesPorMaquinaria(Long maquinariaId) {
        return maquinariaCarpinteroRepository.findByMaquinariaId(maquinariaId);
    }

    // Obtener asignaciones por carpintero
    public List<MaquinariaCarpintero> getAsignacionesPorCarpintero(Long carpinteroId) {
        return maquinariaCarpinteroRepository.findByCarpinteroId(carpinteroId);
    }

    // Obtener maquinarias en uso por un carpintero
    public List<MaquinariaCarpintero> getMaquinariasEnUsoPorCarpintero(Long carpinteroId) {
        return maquinariaCarpinteroRepository.findMaquinariasEnUsoPorCarpintero(carpinteroId);
    }

    // Obtener asignaciones por estado
    public List<MaquinariaCarpintero> getAsignacionesPorEstado(String estado) {
        return maquinariaCarpinteroRepository.findByEstado(estado);
    }

    // Verificar disponibilidad de maquinaria
    public boolean isMaquinariaDisponible(Long maquinariaId) {
        Optional<MaquinariaCarpintero> asignacionActiva = 
            maquinariaCarpinteroRepository.findAsignacionActivaPorMaquinaria(maquinariaId);
        return asignacionActiva.isEmpty();
    }

    // Eliminar asignación
    public void eliminarAsignacion(Long asignacionId) {
        if (!maquinariaCarpinteroRepository.existsById(asignacionId)) {
            throw new RuntimeException("Asignación no encontrada con ID: " + asignacionId);
        }
        maquinariaCarpinteroRepository.deleteById(asignacionId);
    }

    // Actualizar asignación completa
    public MaquinariaCarpintero actualizarAsignacion(Long asignacionId, MaquinariaCarpinteroDTO dto) {
        MaquinariaCarpintero asignacion = maquinariaCarpinteroRepository.findById(asignacionId)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + asignacionId));
        
        // Actualizar maquinaria si cambió
        if (dto.getMaquinariaId() != null && !dto.getMaquinariaId().equals(asignacion.getMaquinaria().getId())) {
            Maquinaria nuevaMaquinaria = maquinariaRepository.findById(dto.getMaquinariaId())
                .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada con ID: " + dto.getMaquinariaId()));
            asignacion.setMaquinaria(nuevaMaquinaria);
        }
        
        // Actualizar carpintero si cambió
        if (dto.getCarpinteroId() != null && !dto.getCarpinteroId().equals(asignacion.getCarpintero().getId())) {
            Usuario nuevoCarpintero = usuarioRepository.findById(dto.getCarpinteroId())
                .orElseThrow(() -> new RuntimeException("Carpintero no encontrado con ID: " + dto.getCarpinteroId()));
            asignacion.setCarpintero(nuevoCarpintero);
        }
        
        // Actualizar estado
        if (dto.getEstado() != null) {
            asignacion.setEstado(dto.getEstado());
        }
        
        return maquinariaCarpinteroRepository.save(asignacion);
    }
} 