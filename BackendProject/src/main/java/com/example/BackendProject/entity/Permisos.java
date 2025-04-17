package com.example.BackendProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permiso")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidad que representa un permiso en el sistema")
public class Permisos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Identificador único del permiso", example = "1")
    private Long id;

    @Schema(description = "Nombre del permiso", example = "CREAR_USUARIO")
    private String nombre;

    public Permisos(String nombre) {
        super();
        this.nombre = nombre;
    }

}
