package com.example.BackendProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SegurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						// Permitir explícitamente Swagger UI y otros endpoints necesarios
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
						// Permitir health check para deployments
						.requestMatchers("/actuator/health", "/actuator/info").permitAll()
						.requestMatchers("/auth/**").permitAll()
						.anyRequest().authenticated()
				)
				.sessionManagement(sess -> sess
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		
		// Permitir orígenes específicos desde variable de entorno
		String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
		if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
			for (String origin : allowedOrigins.split(",")) {
				config.addAllowedOrigin(origin.trim());
			}
		} else {
			// Fallback para desarrollo
			config.addAllowedOrigin("http://localhost:4200");
			config.addAllowedOrigin("https://localhost:4200");
		}
		
		// Configurar métodos HTTP permitidos
		config.addAllowedMethod("*");
		
		// Permitir todos los headers
		config.addAllowedHeader("*");
		
		// Exponer headers necesarios
		config.addExposedHeader("Authorization");
		
		// Permitir credenciales
		config.setAllowCredentials(true);
		
		// Tiempo de caché para respuestas pre-flight
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
