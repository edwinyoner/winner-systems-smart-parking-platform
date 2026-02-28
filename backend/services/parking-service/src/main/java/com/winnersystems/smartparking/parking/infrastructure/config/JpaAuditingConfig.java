package com.winnersystems.smartparking.parking.infrastructure.config;

import com.winnersystems.smartparking.parking.infrastructure.config.security.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuración de auditoría JPA.
 * Captura automáticamente el ID del usuario autenticado para campos createdBy/updatedBy.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

   /**
    * Proveedor de auditor que obtiene el ID del usuario actual del SecurityContext.
    * El ID viene del JWT Token decodificado por JwtAuthenticationFilter.
    */
   @Bean
   public AuditorAware<Long> auditorProvider() {
      return () -> {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

         if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
         }

         // Obtener el principal (CustomUserDetails)
         Object principal = authentication.getPrincipal();

         if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            Long userId = userDetails.getId();

            // Log para debugging (opcional)
            // log.debug("Auditoría JPA: userId={}", userId);

            return Optional.of(userId);
         }

         // Si no hay CustomUserDetails, no hay auditoría
         return Optional.empty();
      };
   }
}