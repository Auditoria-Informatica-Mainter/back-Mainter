package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la entidad Almacen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlmacenDTO {
    private Long id;
    private String nombre;
    private Double capacidad;
} 