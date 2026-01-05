package com.winnersystems.smartparking.auth.infrastructure.config.security;

import com.winnersystems.smartparking.auth.application.port.output.JwtPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j  // ← AGREGADO
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

      System.out.println("========== JWT FILTER DEBUG ==========");
      System.out.println("🌐 Request URI: " + request.getRequestURI());
      System.out.println("🔧 Method: " + request.getMethod());

      // 1. Extraer header Authorization
      final String authHeader = request.getHeader("Authorization");
      System.out.println("🔐 Auth Header: " + authHeader);

      // Si no hay header o no empieza con "Bearer ", continuar sin autenticar
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
         System.out.println("❌ No Bearer token found");
         System.out.println("======================================");
         filterChain.doFilter(request, response);
         return;
      }

      try {
         // 2. Extraer token (quitar "Bearer ")
         final String jwt = authHeader.substring(7);
         System.out.println("🎫 JWT Token: " + jwt.substring(0, Math.min(20, jwt.length())) + "...");

         // 3. Extraer email del token
         final String userEmail = jwtService.extractEmail(jwt);
         System.out.println("📧 Email from token: " + userEmail);

         // 4. Si hay email y NO hay autenticación previa
         if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("👤 Loading UserDetails for: " + userEmail);

            // 5. Cargar detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            System.out.println("✅ UserDetails loaded: " + userDetails.getClass().getName());
            System.out.println("   - Username: " + userDetails.getUsername());
            System.out.println("   - Authorities: " + userDetails.getAuthorities());

            // 6. Validar token
            if (jwtService.validateToken(jwt)) {
               System.out.println("✅ Token is valid");

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
               System.out.println("✅ Authentication set in SecurityContext");
            } else {
               System.out.println("❌ Token is NOT valid");
            }
         } else {
            System.out.println("⚠️ Email is null or authentication already exists");
         }

      } catch (Exception e) {
         System.err.println("❌ Error processing JWT: " + e.getMessage());
         e.printStackTrace();
      }

      System.out.println("======================================");

      // 9. Continuar con el siguiente filtro
      filterChain.doFilter(request, response);
   }
}