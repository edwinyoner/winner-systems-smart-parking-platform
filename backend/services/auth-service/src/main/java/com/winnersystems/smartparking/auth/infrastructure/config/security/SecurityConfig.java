package com.winnersystems.smartparking.auth.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de Spring Security.
 *
 * Define:
 * - Qué endpoints están protegidos y cuáles no
 * - Cómo se autentica (JWT)
 * - Encriptación de contraseñas (BCrypt)
 * - CORS y CSRF
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

   private final JwtAuthenticationFilter jwtAuthFilter;

   public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
      this.jwtAuthFilter = jwtAuthFilter;
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
            // Deshabilitar CSRF (no necesario en API REST con JWT)
            .csrf(csrf -> csrf.disable())

            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                  // Endpoints públicos (sin autenticación)
                  .requestMatchers(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/refresh",
                        "/api/auth/forgot-password",
                        "/api/auth/reset-password",
                        "/api/auth/verify-email"
                  ).permitAll()

                  // Documentación Swagger (si usas)
                  .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                  ).permitAll()

                  // Todos los demás endpoints requieren autenticación
                  .anyRequest().authenticated()
            )

            // Política de sesiones: STATELESS (sin sesiones, solo JWT)
            .sessionManagement(session -> session
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Agregar filtro JWT antes del filtro de autenticación estándar
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
   }

   /**
    * Bean para encriptar contraseñas con BCrypt
    */
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}