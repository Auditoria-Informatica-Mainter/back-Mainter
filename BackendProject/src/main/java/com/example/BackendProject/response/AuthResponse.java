package com.example.BackendProject.response;

import com.example.BackendProject.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	
	String token;
	String email;
	Rol role;
	String nombre;
	String apellido;
	Long id;
	
}
