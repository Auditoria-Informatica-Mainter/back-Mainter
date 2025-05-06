package com.example.BackendProject.controller;

import com.example.BackendProject.dto.UsuarioDTO;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.response.AuthResponse;
import com.example.BackendProject.response.LoginRequest;
import com.example.BackendProject.service.BitacoraService;
import com.example.BackendProject.service.UsuarioService;
import com.example.BackendProject.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class RegistroController {
	
	@Autowired
	private UsuarioService userService;
	
	@Autowired
	private BitacoraService bitacoraService;

	@Autowired
	private IpUtil ipUtil;

	@PostMapping(value = "login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
		String clientIp = ipUtil.getClientIp(servletRequest);
		try {
			AuthResponse response = userService.login(request);
			bitacoraService.logLoginAttempt(request.getEmail(), true, clientIp);
			// Registrar en la base de datos de bitácora
			Usuario usuario = userService.getUser(request.getEmail());
			bitacoraService.registrarLogin(usuario, clientIp);
			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			bitacoraService.logLoginAttempt(request.getEmail(), false, clientIp);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.UNAUTHORIZED.value())
							.message("Credenciales inválidas")
							.build());
		} catch (UsernameNotFoundException e) {
			bitacoraService.logLoginAttempt(request.getEmail(), false, clientIp);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.NOT_FOUND.value())
							.message(e.getMessage())
							.build());
		} catch (Exception e) {
			bitacoraService.logLoginAttempt(request.getEmail(), false, clientIp);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder()
							.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
							.message("Error al iniciar sesión: " + e.getMessage())
							.build());
		}
	}
	
	@PostMapping(value = "register")
	public ResponseEntity<?> register(@RequestBody UsuarioDTO userDto, HttpServletRequest request) {
		String clientIp = ipUtil.getClientIp(request);
		try {
			if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(ApiResponse.builder()
								.statusCode(HttpStatus.BAD_REQUEST.value())
								.message("La contraseña es obligatoria para el registro")
								.build());
			}
			
			AuthResponse response = userService.createUser(userDto);
			bitacoraService.logRegistration(userDto.getEmail(), "USUARIO", clientIp);
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
	public ResponseEntity<?> registerAdmin(@RequestBody UsuarioDTO userDto, HttpServletRequest request) {
		String clientIp = ipUtil.getClientIp(request);
		try {
			if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(ApiResponse.builder()
								.statusCode(HttpStatus.BAD_REQUEST.value())
								.message("La contraseña es obligatoria para el registro")
								.build());
			}
			
			AuthResponse response = userService.createUserAdmin(userDto);
			bitacoraService.logRegistration(userDto.getEmail(), "ADMIN", clientIp);
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

	@PostMapping(value = "registerClient")
	public ResponseEntity<?> registerClient(@RequestBody UsuarioDTO userDto) {
		try {
			// Validar que el password no sea nulo o vacío en el registro
			if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(ApiResponse.builder()
								.statusCode(HttpStatus.BAD_REQUEST.value())
								.message("La contraseña es obligatoria para el registro")
								.build());
			}
			
			AuthResponse response = userService.createUserClient(userDto);
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
							.message("Error al registrar cliente: " + e.getMessage())
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
