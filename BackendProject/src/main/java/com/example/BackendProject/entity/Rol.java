package com.example.BackendProject.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rol")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidad que representa un rol del sistema")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Identificador único del rol")
    private Long id;

    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String nombre;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "rol_permiso",
            joinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"),
            inverseJoinColumns =  @JoinColumn(name = "permiso_id", referencedColumnName = "id")
    )
    @Schema(description = "Permisos asignados al rol")
    private Set<Permisos> permisos;

    public Rol(String nombre) {
        super();
        this.nombre = nombre;
    }

}
