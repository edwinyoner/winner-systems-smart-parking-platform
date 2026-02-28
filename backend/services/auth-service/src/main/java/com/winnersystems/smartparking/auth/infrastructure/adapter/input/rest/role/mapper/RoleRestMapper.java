package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role.mapper;

import com.winnersystems.smartparking.auth.application.dto.query.RoleDto;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role.dto.response.RoleResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper para conversiones de Role entre capas REST.
 */
@Component
public class RoleRestMapper {

   /**
    * Convierte RoleDto (Application) a RoleResponse (REST)
    */
   public RoleResponse toResponse(RoleDto dto) {
      RoleResponse response = new RoleResponse();
      response.setId(dto.id());
      response.setName(dto.name());
      response.setDescription(dto.description());
      response.setStatus(dto.status());
      response.setCreatedAt(dto.createdAt());
      response.setUpdatedAt(dto.updatedAt());

      // Mapear permisos si existen
      if (dto.permissions() != null) {
         response.setPermissions(
               dto.permissions().stream()
                     .map(p -> new RoleResponse.PermissionInfo(
                           p.id(),
                           p.name(),
                           p.description()
                     ))
                     .collect(Collectors.toSet())
         );
      }

      return response;
   }
}