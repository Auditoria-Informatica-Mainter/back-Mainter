package com.example.BackendProject.response;

public class LoginRequest {

	private String email;
	private String password;

	// Constructor sin argumentos (necesario para deserialización JSON)
	public LoginRequest() {
	}

	// Constructor con argumentos (opcional, útil para pruebas o instancias rápidas)
	public LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	// Getters y setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
