package com.example.BackendProject.response;

import com.example.BackendProject.entity.Rol;

public class AuthResponse {

	private String token;
	private String email;
	private String nombre;
	private String apellido;
	private String telefono;
	private Long id;
	private Rol role;

	public AuthResponse() {
	}

	public AuthResponse(String token, String email, String nombre, String apellido,String telefono, Long id, Rol role) {
		this.token = token;
		this.email = email;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.id = id;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono() {
		return telefono;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Rol getRole() {
		return role;
	}

	public void setRole(Rol role) {
		this.role = role;
	}

	// Opcional: builder si usas Lombok o lo quieres implementar manualmente
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final AuthResponse authResponse = new AuthResponse();

		public Builder token(String token) {
			authResponse.setToken(token);
			return this;
		}

		public Builder email(String email) {
			authResponse.setEmail(email);
			return this;
		}

		public Builder nombre(String nombre) {
			authResponse.setNombre(nombre);
			return this;
		}

		public Builder apellido(String apellido) {
			authResponse.setApellido(apellido);
			return this;
		}

		public Builder telefono(String telefono) {
			authResponse.setTelefono(telefono);
			return this;
		}

		public Builder id(Long id) {
			authResponse.setId(id);
			return this;
		}

		public Builder role(Rol role) {
			authResponse.setRole(role);
			return this;
		}

		public AuthResponse build() {
			return authResponse;
		}
	}
}
