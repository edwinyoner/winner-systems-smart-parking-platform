package com.winnersystems.smartparking.parking.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de Spring Security para parking-service.
 * Configura autenticación basada en JWT tokens.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

   private final JwtAuthenticationFilter jwtAuthenticationFilter;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
            // Deshabilitar CSRF (no necesario para APIs REST stateless)
            .csrf(AbstractHttpConfigurer::disable)

            // Configurar autorización de requests
            .authorizeHttpRequests(auth -> auth
                  // ========== ENDPOINTS PÚBLICOS (TEMPORALMENTE) ==========
                  // ⚠️ SOLO PARA DESARROLLO - En producción quitar estos
                  .requestMatchers(
                        "/actuator/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/error",
                        // ⚠️ TEMPORALMENTE PERMITIR TODOS LOS ENDPOINTS
                        "/shifts/**",
                        "/rates/**",
                        "/parkings/**",
                        "/zones/**",
                        "/spaces/**",
                        "/document-types/**",
                        "/payment-types/**",
                        "/transactions/**"
                  ).permitAll()

                  // Todos los demás endpoints requieren autenticación
                  .anyRequest().authenticated()
            )

            // Sin estado (stateless) - No crear sesiones
            .sessionManagement(session ->
                  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Agregar filtro JWT antes del filtro de autenticación estándar
            .addFilterBefore(
                  jwtAuthenticationFilter,
                  UsernamePasswordAuthenticationFilter.class
            );

      return http.build();
   }
}