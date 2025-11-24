package com.winnersystems.smartparking.auth.infrastructure.config.security;

import com.winnersystems.smartparking.auth.application.port.output.JwtPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta TODAS las peticiones HTTP.
 *
 * Flujo:
 * 1. Extrae el token JWT del header "Authorization"
 * 2. Valida el token
 * 3. Extrae el email del usuario
 * 4. Carga los detalles del usuario
 * 5. Crea autenticación en SecurityContext
 * 6. Continúa con la petición
 *
 * Si el token es inválido → Error 401 Unauthorized
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final JwtPort jwtService;
   private final UserDetailsService userDetailsService;

   public JwtAuthenticationFilter(
         JwtPort jwtService,
         UserDetailsService userDetailsService) {
      this.jwtService = jwtService;
      this.userDetailsService = userDetailsService;
   }

   @Override
   protected void doFilterInternal(
         HttpServletRequest request,
         HttpServletResponse response,
         FilterChain filterChain) throws ServletException, IOException {

      // 1. Extraer header Authorization
      final String authHeader = request.getHeader("Authorization");

      // Si no hay header o no empieza con "Bearer ", continuar sin autenticar
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
         filterChain.doFilter(request, response);
         return;
      }

      try {
         // 2. Extraer token (quitar "Bearer ")
         final String jwt = authHeader.substring(7);

         // 3. Extraer email del token
         final String userEmail = jwtService.extractEmail(jwt);

         // 4. Si hay email y NO hay autenticación previa
         if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Cargar detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // 6. Validar token
            if (jwtService.validateToken(jwt)) {

               // 7. Crear autenticación
               UsernamePasswordAuthenticationToken authToken =
                     new UsernamePasswordAuthenticationToken(
                           userDetails,
                           null,
                           userDetails.getAuthorities()
                     );

               authToken.setDetails(
                     new WebAuthenticationDetailsSource().buildDetails(request)
               );

               // 8. Establecer autenticación en el contexto de Spring Security
               SecurityContextHolder.getContext().setAuthentication(authToken);
            }
         }

      } catch (Exception e) {
         // Si hay error al procesar el token, no autenticar
         // El endpoint protegido retornará 401
      }

      // 9. Continuar con el siguiente filtro
      filterChain.doFilter(request, response);
   }
}