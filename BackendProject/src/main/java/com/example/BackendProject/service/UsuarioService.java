package com.example.BackendProject.service;

import com.example.BackendProject.config.JwtService;
import com.example.BackendProject.dto.UsuarioDTO;
import com.example.BackendProject.entity.Rol;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.RolRepository;
import com.example.BackendProject.repository.UsuarioRepository;
import com.example.BackendProject.response.AuthResponse;
import com.example.BackendProject.response.LoginRequest;
import com.example.BackendProject.config.LoggableAction;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private RolService rolService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UsuarioDetailsService usuarioDetailsService;

	@Autowired
	private ModelMapper modelMapper;

	public List<UsuarioDTO> listUser() {
		List<Usuario> users = usuarioRepository.findAll();
		return users.stream()
				.map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
				.collect(Collectors.toList());
	}

	public List<Usuario> listUsuario() {
		return usuarioRepository.findAll();
	}

	public Usuario getUser(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}

	public Long getUsuarioById(String email) {
		return getUser(email).getId();
	}

	public Long getUsuariorById(String name) {
		return getUser(name).getId();
	}

	public Usuario obtenerUserPorId(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}

	@LoggableAction
	public AuthResponse createUser(UsuarioDTO userDto) {
		Rol rol = rolService.obtenerRol(userDto.getRolid());
		Usuario usuario = new Usuario(
				userDto.getNombre(),
				userDto.getApellido(),
				passwordEncoder.encode(userDto.getPassword()),
				userDto.getTelefono(),
				userDto.getEmail(),
				true, // estado
				true, // disponible
				rol
		);
		usuarioRepository.save(usuario);
		return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
	}

	public AuthResponse createUserAdmin(UsuarioDTO userDto) {
		Rol rol = rolRepository.findByNombre("ADMIN")
				.orElseGet(() -> rolRepository.save(new Rol("ADMIN")));

		Usuario usuario = new Usuario(
				userDto.getNombre(),
				userDto.getApellido(),
				passwordEncoder.encode(userDto.getPassword()),
				userDto.getTelefono(),
				userDto.getEmail(),
				true,
				true,
				rol
		);
		usuarioRepository.save(usuario);
		return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
	}

	@LoggableAction
	public Usuario registrarUser(UsuarioDTO userDto) {
		Rol rol = rolService.obtenerRol(userDto.getRolid());
		Usuario usuario = new Usuario(
				userDto.getNombre(),
				userDto.getApellido(),
				passwordEncoder.encode(userDto.getPassword()),
				userDto.getTelefono(),
				userDto.getEmail(),
				true,
				true,
				rol
		);
		return usuarioRepository.save(usuario);
	}

	public AuthResponse login(LoginRequest loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(), loginRequest.getPassword()));

		Usuario usuario = getUser(loginRequest.getUsername());
		String token = jwtService.getToken(usuario);

		return AuthResponse.builder()
				.token(token)
				.email(usuario.getEmail())
				.nombre(usuario.getNombre())
				.apellido(usuario.getApellido())
				.id(usuario.getId())
				.role(usuario.getRol())
				.build();
	}

	public AuthResponse loader(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Usuario user = usuarioDetailsService.getUser(userDetails.getUsername());
		String token = jwtService.getToken(userDetails);
		return AuthResponse.builder()
				.token(token)
				.email(user.getEmail())
				.nombre(user.getNombre())
				.apellido(user.getApellido())
				.id(user.getId())
				.role(user.getRol())
				.build();
	}

	@LoggableAction
	public Usuario updateUser(Long id, UsuarioDTO userDto) {
		Usuario user = obtenerUserPorId(id);
		user.setNombre(userDto.getNombre());
		user.setApellido(userDto.getApellido());
		user.setEmail(userDto.getEmail());
		user.setTelefono(userDto.getTelefono());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRol(rolService.obtenerRol(userDto.getRolid()));
		return usuarioRepository.save(user);
	}

	@Transactional
	@LoggableAction
	public void deleteUser(Long id) {
		Usuario user = obtenerUserPorId(id);
		user.setEstado(false);
		user.setDisponibilidad(false);
		usuarioRepository.save(user);
	}

	@Transactional
	@LoggableAction
	public void activeUser(Long id) {
		Usuario user = obtenerUserPorId(id);
		user.setEstado(true);
		user.setDisponibilidad(true);
		usuarioRepository.save(user);
	}

	public List<Usuario> buscarUsuarios(String searchTerm) {
		return usuarioRepository.findByNombreOrApellidoOrEmail(searchTerm);
	}

	public Usuario obtenerUsuarioAuthenticado() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String email = ((UserDetails) principal).getUsername();
			return getUser(email);
		} else {
			throw new IllegalStateException("Usuario no autenticado");
		}
	}
}
