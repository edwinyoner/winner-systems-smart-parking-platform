package com.winnersystems.smartparking.parking.infrastructure.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Collection;

/**
 * Filtro de autenticación JWT.
 * Intercepta cada request, valida el JWT token y extrae el userId para auditoría.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final JwtService jwtService;

   @Override
   protected void doFilterInternal(
         @NonNull HttpServletRequest request,
         @NonNull HttpServletResponse response,
         @NonNull FilterChain filterChain
   ) throws ServletException, IOException {

      // 1. Extraer token del header Authorization
      String token = extractTokenFromRequest(request);

      // 2. Si hay token y es válido, autenticar
      if (token != null && jwtService.validateToken(token)) {
         try {
            // 3. Extraer claims del JWT
            Claims claims = jwtService.extractAllClaims(token);

            Long userId = claims.get("userId", Long.class);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            log.debug("🔐 JWT válido: userId={}, email={}, role={}", userId, email, role);

            // 4. Crear authorities (roles y permisos)
            Collection<GrantedAuthority> authorities =
                  Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            // 5. Crear CustomUserDetails con el userId
            CustomUserDetails userDetails = new CustomUserDetails(
                  userId,
                  email,
                  role,
                  authorities
            );

            // 6. Establecer autenticación en el contexto de Spring Security
            UsernamePasswordAuthenticationToken authentication =
                  new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                  );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("Usuario autenticado: userId={}", userId);

         } catch (Exception e) {
            log.error("Error al procesar JWT: {}", e.getMessage());
         }
      }

      // 7. Continuar con la cadena de filtros
      filterChain.doFilter(request, response);
   }

   /**
    * Extrae el token JWT del header Authorization.
    * Formato esperado: "Authorization: Bearer <token>"
    */
   private String extractTokenFromRequest(HttpServletRequest request) {
      String authHeader = request.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
         return authHeader.substring(7); // Remover "Bearer "
      }

      return null;
   }
}