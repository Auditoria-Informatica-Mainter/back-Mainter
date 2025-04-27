package com.example.BackendProject.service;

import com.example.BackendProject.dto.SectorDTO;
import com.example.BackendProject.entity.Almacen;
import com.example.BackendProject.entity.Sector;
import com.example.BackendProject.repository.AlmacenRepository;
import com.example.BackendProject.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de sectores
 */
@Service
public class SectorService {
    
    private final SectorRepository sectorRepository;
    private final AlmacenRepository almacenRepository;
    
    @Autowired
    public SectorService(SectorRepository sectorRepository, AlmacenRepository almacenRepository) {
        this.sectorRepository = sectorRepository;
        this.almacenRepository = almacenRepository;
    }
    
    /**
     * Obtiene todos los sectores
     * @return lista de sectores
     */
    public List<Sector> listarSectores() {
        return sectorRepository.findAll();
    }
    
    /**
     * Obtiene un sector por su ID
     * @param id el ID del sector
     * @return el sector encontrado
     * @throws ResponseStatusException si no se encuentra el sector
     */
    public Sector obtenerSector(Long id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sector no encontrado con ID: " + id));
    }
    
    /**
     * Crea un nuevo sector
     * @param sectorDTO datos del nuevo sector
     * @return el sector creado
     * @throws ResponseStatusException si no se encuentra el almacén
     */
    public Sector crearSector(SectorDTO sectorDTO) {
        Almacen almacen = almacenRepository.findById(sectorDTO.getAlmacenId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Almacén no encontrado con ID: " + sectorDTO.getAlmacenId()));
        
        Sector sector = new Sector(
                sectorDTO.getNombre(),
                sectorDTO.getStock(),
                sectorDTO.getCapacidad_maxima(),
                sectorDTO.getTipo(),
                sectorDTO.getDescripcion(),
                almacen
        );
        
        return sectorRepository.save(sector);
    }
    
    /**
     * Actualiza un sector existente
     * @param id el ID del sector a actualizar
     * @param sectorDTO los nuevos datos del sector
     * @return el sector actualizado
     * @throws ResponseStatusException si no se encuentra el sector o el almacén
     */
    public Sector actualizarSector(Long id, SectorDTO sectorDTO) {
        Sector sector = obtenerSector(id);
        
        if (sectorDTO.getAlmacenId() != null) {
            Almacen almacen = almacenRepository.findById(sectorDTO.getAlmacenId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Almacén no encontrado con ID: " + sectorDTO.getAlmacenId()));
            sector.setAlmacen(almacen);
        }
        
        sector.setNombre(sectorDTO.getNombre());
        sector.setStock(sectorDTO.getStock());
        sector.setCapacidad_maxima(sectorDTO.getCapacidad_maxima());
        sector.setTipo(sectorDTO.getTipo());
        sector.setDescripcion(sectorDTO.getDescripcion());
        
        return sectorRepository.save(sector);
    }
    
    /**
     * Elimina un sector
     * @param id el ID del sector a eliminar
     * @throws ResponseStatusException si no se encuentra el sector
     */
    public void eliminarSector(Long id) {
        if (!sectorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Sector no encontrado con ID: " + id);
        }
        
        sectorRepository.deleteById(id);
    }
    
    /**
     * Busca sectores por almacén
     * @param almacenId el ID del almacén
     * @return lista de sectores del almacén
     */
    public List<Sector> buscarPorAlmacen(Long almacenId) {
        return sectorRepository.findByAlmacenId(almacenId);
    }
    
    /**
     * Busca sectores por tipo
     * @param tipo el tipo de sector
     * @return lista de sectores del tipo especificado
     */
    public List<Sector> buscarPorTipo(String tipo) {
        return sectorRepository.findByTipo(tipo);
    }
} 