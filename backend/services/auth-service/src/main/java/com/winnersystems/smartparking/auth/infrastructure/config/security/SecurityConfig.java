package com.winnersystems.smartparking.auth.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de Spring Security para auth-service.
 *
 * <p><b>IMPORTANTE - Arquitectura Microservicios:</b></p>
 * <ul>
 *   <li>CORS se maneja en API Gateway, NO aquí</li>
 *   <li>Paths son relativos (sin /api/auth) por RewritePath del Gateway</li>
 *   <li>Este servicio NO es accesible directamente desde el navegador</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

   private final JwtAuthenticationFilter jwtAuthFilter;
   private final UserDetailsService userDetailsService;

   public SecurityConfig(
         JwtAuthenticationFilter jwtAuthFilter,
         UserDetailsService userDetailsService) {
      this.jwtAuthFilter = jwtAuthFilter;
      this.userDetailsService = userDetailsService;
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
            // Deshabilitar CSRF (API REST stateless)
            .csrf(csrf -> csrf.disable())

            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                  // Endpoints públicos (paths DESPUÉS del rewrite del Gateway)
                  .requestMatchers(
                        "/login",              // POST /api/auth/login → /login
                        "/refresh",            // POST /api/auth/refresh → /refresh
                        "/forgot-password",    // POST /api/auth/forgot-password → /forgot-password
                        "/reset-password",     // POST /api/auth/reset-password → /reset-password
                        "/verify-email",        // GET  /api/auth/verify-email → /verify-email
                        "/resend-verification"
                  ).permitAll()

                  .requestMatchers("/change-password").authenticated()
                  // Actuator (health check para Eureka)
                  .requestMatchers("/actuator/health").permitAll()

                  // Todos los demás endpoints requieren autenticación
                  .anyRequest().authenticated()
            )

            // Política de sesiones: STATELESS
            .sessionManagement(session -> session
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Provider de autenticación
            .authenticationProvider(authenticationProvider())

            // Filtro JWT
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
   }

   @Bean
   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setUserDetailsService(userDetailsService);
      provider.setPasswordEncoder(passwordEncoder());
      return provider;
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
   }
}