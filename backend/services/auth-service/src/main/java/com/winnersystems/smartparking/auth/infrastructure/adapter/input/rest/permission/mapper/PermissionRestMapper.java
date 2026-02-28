package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission.mapper;

import com.winnersystems.smartparking.auth.application.dto.query.PermissionDto;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission.dto.response.PermissionResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversiones de Permission entre capas REST.
 */
@Component
public class PermissionRestMapper {

   /**
    * Convierte PermissionDto (Application) a PermissionResponse (REST)
    */
   public PermissionResponse toResponse(PermissionDto dto) {
      PermissionResponse response = new PermissionResponse();
      response.setId(dto.id());
      response.setName(dto.name());
      response.setDescription(dto.description());
      response.setStatus(dto.status());
      response.setCreatedAt(dto.createdAt());
      response.setUpdatedAt(dto.updatedAt());
      return response;
   }
}