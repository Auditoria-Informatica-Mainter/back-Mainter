package com.example.BackendProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaquinariaCarpinteroDTO {
    private Long id;
    private String estado;
    private Long maquinariaId;
    private Long carpinteroId;
    
    // Constructor para crear nueva asignación
    public MaquinariaCarpinteroDTO(String estado, Long maquinariaId, Long carpinteroId) {
        this.estado = estado;
        this.maquinariaId = maquinariaId;
        this.carpinteroId = carpinteroId;
    }
} 