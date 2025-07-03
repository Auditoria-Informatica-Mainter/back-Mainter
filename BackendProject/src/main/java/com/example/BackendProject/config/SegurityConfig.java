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
						// VULNERABILIDAD 1: Endpoints expuestos sin autenticación (detectable con Nmap/Nessus)
						.requestMatchers("/actuator/**").permitAll() // Expone todos los actuator endpoints
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
						.requestMatchers("/auth/**").permitAll()
						// VULNERABILIDAD 2: Endpoints de administración sin protección
						.requestMatchers("/admin/**", "/debug/**", "/test/**").permitAll()
						// VULNERABILIDAD 3: Directorio backup expuesto
						.requestMatchers("/backup/**", "/config/**").permitAll()
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
		
		// VULNERABILIDAD 4: CORS permisivo - permite cualquier origen (detectable con herramientas web)
		config.addAllowedOriginPattern("*");
		config.setAllowCredentials(false); // Cambiado para permitir *
		
		// Configurar métodos HTTP permitidos
		config.addAllowedMethod("*"); // Permite todos los métodos HTTP
		
		// VULNERABILIDAD 5: Headers permisivos
		config.addAllowedHeader("*"); // Permite todos los headers
		
		// Exponer headers que pueden contener información sensible
		config.addExposedHeader("Authorization");
		config.addExposedHeader("X-Debug-Info");
		config.addExposedHeader("X-Server-Version");
		
		// Tiempo de caché extendido
		config.setMaxAge(86400L); // 24 horas

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
