package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.mapper;

import com.winnersystems.smartparking.auth.application.dto.command.CreateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.command.UpdateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.port.output.RolePersistencePort;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.CreateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.UpdateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper que convierte entre REST DTOs y Application DTOs para User.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserRestMapper {

   private final RolePersistencePort rolePersistencePort;

   /**
    * CreateUserRequest → CreateUserCommand.
    * Convierte nombres de roles (String) a roleIds (Long).
    */
   public CreateUserCommand toCommand(
         CreateUserRequest request,
         HttpServletRequest httpRequest,
         Long createdBy) {

      // Convertir nombres de roles a IDs
      Set<Long> roleIds = request.roles().stream()
            .map(roleName -> rolePersistencePort.findByName(roleName)
                  .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleName)))
            .map(Role::getId)
            .collect(Collectors.toSet());

      return new CreateUserCommand(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.password(),
            request.phoneNumber(),
            roleIds,                              // ✅ Set<Long>
            request.captchaToken(),
            extractIpAddress(httpRequest),        // ✅ ipAddress
            createdBy                              // ✅ createdBy
      );
   }

   /**
    * UpdateUserRequest → UpdateUserCommand.
    * Convierte nombres de roles (String) a roleIds (Long).
    */
   public UpdateUserCommand toCommand(
         Long userId,
         UpdateUserRequest request,
         Long updatedBy) {

      // Convertir nombres de roles a IDs
      Set<Long> roleIds = request.roles().stream()
            .map(roleName -> rolePersistencePort.findByName(roleName)
                  .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleName)))
            .map(Role::getId)
            .collect(Collectors.toSet());

      return new UpdateUserCommand(
            userId,
            request.firstName(),
            request.lastName(),
            request.phoneNumber(),
            null,                       // ✅ profilePicture (por ahora null)
            roleIds,                    // ✅ Set<Long>
            updatedBy                   // ✅ updatedBy
      );
   }

   /**
    * UserDto → UserResponse.
    */
   public UserResponse toResponse(UserDto dto) {
      return new UserResponse(
            dto.id(),
            dto.firstName(),
            dto.lastName(),
            dto.firstName() + " " + dto.lastName(),
            dto.email(),
            dto.phoneNumber(),
            dto.profilePicture(),
            dto.status(),
            dto.emailVerified(),
            dto.roles() != null ? dto.roles().stream()
                  .map(roleName -> new UserResponse.RoleInfo(
                        roleName,
                        toDisplayName(roleName),
                        toDescription(roleName)
                  ))
                  .collect(Collectors.toSet()) : null,
            dto.createdAt(),
            dto.updatedAt()              // ✅ SIN lastLoginAt
      );
   }

   // ========== HELPERS ==========

   private String extractIpAddress(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
         return ip.split(",")[0].trim();
      }
      ip = request.getHeader("X-Real-IP");
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
         return ip;
      }
      ip = request.getRemoteAddr();
      if ("0:0:0:0:0:0:0:1".equals(ip)) {
         ip = "127.0.0.1";
      }
      return ip;
   }

   private String toDisplayName(String roleName) {
      return switch (roleName) {
         case "ADMIN" -> "Administrador";
         case "AUTORIDAD" -> "Autoridad Municipal";
         case "OPERADOR" -> "Operador";
         default -> roleName;
      };
   }

   private String toDescription(String roleName) {
      return switch (roleName) {
         case "ADMIN" -> "Acceso completo al sistema Smart Parking";
         case "AUTORIDAD" -> "Gestión de zonas, tarifas, reportes y configuración municipal";
         case "OPERADOR" -> "Operación diaria del sistema de estacionamiento";
         default -> "Rol personalizado";
      };
   }
}