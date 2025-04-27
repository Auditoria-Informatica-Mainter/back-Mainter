package com.example.BackendProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la entidad SubCategoria
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
