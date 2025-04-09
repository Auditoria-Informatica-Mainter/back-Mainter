package com.example.BackendProject.controller;

import com.example.BackendProject.dto.UsuarioDTO;
import com.example.BackendProject.response.AuthResponse;
import com.example.BackendProject.response.LoginRequest;
import com.example.BackendProject.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistroController {
	
	@Autowired
	private UsuarioService userService;

	@PostMapping(value = "login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(userService.login(request));
	}
	
	@PostMapping(value = "register")
	public ResponseEntity<AuthResponse> register(@RequestBody UsuarioDTO userDto) {
		return ResponseEntity.ok(userService.createUser(userDto));
	}
	
	@PostMapping(value = "registerAdmin")
	public ResponseEntity<AuthResponse> registerAdmin(@RequestBody UsuarioDTO userDto) {
		return ResponseEntity.ok(userService.createUserAdmin(userDto));
	}
	
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.loader(authentication));
    }

}
