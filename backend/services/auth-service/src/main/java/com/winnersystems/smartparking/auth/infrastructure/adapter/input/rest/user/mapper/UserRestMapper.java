package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.mapper;

import com.winnersystems.smartparking.auth.application.dto.command.CreateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.command.UpdateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.CreateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.UpdateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper que convierte entre REST DTOs y Application DTOs para User.
 */
@Component
public class UserRestMapper {

   /**
    * CreateUserRequest → CreateUserCommand
    */
   public CreateUserCommand toCommand(CreateUserRequest request) {
      return new CreateUserCommand(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.password(),
            request.phoneNumber(),
            request.roles(),
            request.captchaToken()
      );
   }

   /**
    * UpdateUserRequest → UpdateUserCommand
    */
   public UpdateUserCommand toCommand(Long userId, UpdateUserRequest request) {
      return new UpdateUserCommand(
            userId,
            request.firstName(),
            request.lastName(),
            request.phoneNumber(),
            request.status(),
            request.roles()
      );
   }

   /**
    * UserDto → UserResponse
    */
   public UserResponse toResponse(UserDto dto) {
      return new UserResponse(
            dto.id(),
            dto.firstName(),
            dto.lastName(),
            dto.fullName(),
            dto.email(),
            dto.phoneNumber(),
            dto.profilePicture(),
            dto.status().name(),
            dto.emailVerified(),
            dto.roles() != null ? dto.roles().stream()
                  .map(role -> new UserResponse.RoleInfo(
                        role.id(),
                        role.roleType().name(),
                        role.displayName(),
                        role.description()
                  ))
                  .collect(Collectors.toSet()) : null,
            dto.lastLoginAt(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}