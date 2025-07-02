package com.example.BackendProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${ALLOWED_ORIGINS:http://localhost:4200,https://front-mainter.vercel.app}")
	private String allowedOrigins;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						// Permitir health check para deployments (CRÍTICO para Koyeb)
						.requestMatchers("/actuator/health", "/actuator/info").permitAll()
						// Permitir explícitamente Swagger UI y otros endpoints necesarios
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
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
		
		// Leer orígenes permitidos de variable de entorno
		String[] origins = allowedOrigins.split(",");
		for (String origin : origins) {
			config.addAllowedOrigin(origin.trim());
		}
		
		// Configurar métodos HTTP permitidos
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("OPTIONS");
		
		// Permitir headers específicos
		config.addAllowedHeader("Content-Type");
		config.addAllowedHeader("Authorization");
		config.addAllowedHeader("X-Requested-With");
		config.addAllowedHeader("Accept");
		
		// Exponer headers necesarios
		config.addExposedHeader("Authorization");
		
		// Permitir credenciales solo para orígenes específicos
		config.setAllowCredentials(true);
		
		// Tiempo de caché para respuestas pre-flight
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
