package com.example.BackendProject.controller;

import com.example.BackendProject.dto.UsuarioDTO;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.response.AuthResponse;
import com.example.BackendProject.response.LoginRequest;
import com.example.BackendProject.service.UsuarioService;
import com.example.BackendProject.util.HttpStatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistroController {
	
	@Autowired
	private UsuarioService userService;

	@PostMapping(value = "login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			return ResponseEntity.ok(userService.login(request));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.UNAUTHORIZED.value())
							.message("Credenciales inválidas")
							.build());
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.NOT_FOUND.value())
							.message(e.getMessage())
							.build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
							.message("Error al iniciar sesión: " + e.getMessage())
							.build());
		}
	}
	
	@PostMapping(value = "register")
	public ResponseEntity<?> register(@RequestBody UsuarioDTO userDto) {
		try {
			// Validar que el password no sea nulo o vacío en el registro
			if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(ApiResponse.builder()
								.statusCode(HttpStatus.BAD_REQUEST.value())
								.message("La contraseña es obligatoria para el registro")
								.build());
			}
			
			AuthResponse response = userService.createUser(userDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.BAD_REQUEST.value())
							.message(e.getMessage())
							.build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
							.message("Error al registrar usuario: " + e.getMessage())
							.build());
		}
	}
	
	@PostMapping(value = "registerAdmin")
	public ResponseEntity<?> registerAdmin(@RequestBody UsuarioDTO userDto) {
		try {
			// Validar que el password no sea nulo o vacío en el registro
			if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(ApiResponse.builder()
								.statusCode(HttpStatus.BAD_REQUEST.value())
								.message("La contraseña es obligatoria para el registro")
								.build());
			}
			
			AuthResponse response = userService.createUserAdmin(userDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.BAD_REQUEST.value())
							.message(e.getMessage())
							.build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
							.message("Error al registrar administrador: " + e.getMessage())
							.build());
		}
	}
	
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
			return ResponseEntity.ok(userService.loader(authentication));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
							.message("Error al obtener usuario actual: " + e.getMessage())
							.build());
		}
    }
}
