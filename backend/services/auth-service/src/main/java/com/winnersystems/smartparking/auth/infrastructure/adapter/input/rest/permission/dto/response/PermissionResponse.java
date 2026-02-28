package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response simplificada para permisos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

   private Long id;
   private String name;
   private String description;
   private boolean status;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
}