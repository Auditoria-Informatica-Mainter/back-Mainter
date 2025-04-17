package com.example.BackendProject.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidad que representa un usuario del sistema")
public class Usuario implements UserDetails{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Identificador único del usuario")
    private Long id;

    @Schema(description = "Nombre del usuario")
    private String nombre;

    @Schema(description = "Apellido del usuario")
    private String apellido;

    @Email(message = "El email debe ser válido")
    @Schema(description = "Email del usuario (único)", example = "usuario@example.com")
    private String email;
    
    @Schema(description = "Número de teléfono del usuario")
    private String telefono;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @Schema(description = "Estado del usuario (activo/inactivo)")
    private boolean estado;
    
    @Schema(description = "Disponibilidad del usuario")
    private boolean disponibilidad;
    
    @JsonIgnore
    private boolean cuentaNoExpirada;
    
    @JsonIgnore
    private boolean cuentaNoBloqueada;
    
    @JsonIgnore
    private boolean credencialesNoExpiradas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    @Schema(description = "Rol asignado al usuario")
    private Rol rol;


    public Usuario(String nombre, String apellido, String email, String password, String telefono, boolean estado, boolean disponible, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.estado = true;
        this.disponibilidad = true;
        this.rol = rol;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna solo el nombre del rol como autoridad, sin usar permisos
        return Collections.singletonList(new SimpleGrantedAuthority(rol.getNombre()));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return cuentaNoExpirada;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return cuentaNoBloqueada;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return credencialesNoExpiradas;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return estado;
    }
}
