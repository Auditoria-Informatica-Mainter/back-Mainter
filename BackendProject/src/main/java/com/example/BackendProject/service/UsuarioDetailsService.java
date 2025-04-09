package com.example.BackendProject.service;

import com.example.BackendProject.entity.Rol;
import com.example.BackendProject.entity.Usuario;
import com.example.BackendProject.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UsuarioDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario o password inválidos"));

		// Convertimos el único rol a una autoridad
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().getNombre());

		return new User(usuario.getEmail(), usuario.getPassword(), java.util.Collections.singletonList(authority));
	}



    public Usuario getUser(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
	
}
