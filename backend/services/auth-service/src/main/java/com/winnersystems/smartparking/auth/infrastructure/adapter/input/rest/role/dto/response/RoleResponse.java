package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Response simplificada para roles (sin campos de auditoría).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

   private Long id;
   private String name;
   private String description;
   private boolean status;
   private Set<PermissionInfo> permissions;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   /**
    * Info básica de permisos dentro del rol
    */
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class PermissionInfo {
      private Long id;
      private String name;
      private String description;
   }
}