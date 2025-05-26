package com.example.BackendProject.dto;

import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaquinariaDTO {

    private Long id;

    @NotEmpty(message = "El nombre es obligatorio")
    private String nombre;

    @NotEmpty(message = "El estado es obligatorio")
    private String estado;

    @NotEmpty(message = "La descripción es obligatoria")
    private String descripcion;
}