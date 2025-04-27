package com.example.BackendProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
	
	@NotEmpty(message = "ingrese su numero de ci")
	private String ci;
	
    @NotEmpty(message = "ingrese un nombre")
    private String nombre;
    
    @NotEmpty(message = "ingrese un apellido")
    private String apellido;
    
    private String telefono;
    
    private Long rolid;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    private String password;


}
