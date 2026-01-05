package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user;

import com.winnersystems.smartparking.auth.application.dto.command.CreateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.command.UpdateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.dto.query.UserSearchCriteria;
import com.winnersystems.smartparking.auth.application.port.input.user.*;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.MessageResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.CreateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.UpdateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.response.UserResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.mapper.UserRestMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * REST Controller para gestión de usuarios (CRUD).
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestAdapter {

   private final CreateUserUseCase createUserUseCase;
   private final UpdateUserUseCase updateUserUseCase;
   private final GetUserUseCase getUserUseCase;
   private final ListUsersUseCase listUsersUseCase;
   private final DeleteUserUseCase deleteUserUseCase;
   private final RestoreUserUseCase restoreUserUseCase;
   private final UserRestMapper mapper;

   /**
    * POST /users - Crear usuario
    */
   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UserResponse> createUser(
         @Valid @RequestBody CreateUserRequest request,
         HttpServletRequest httpRequest,
         Authentication authentication) {

      Long createdBy = Long.parseLong(authentication.getName());
      CreateUserCommand command = mapper.toCommand(request, httpRequest, createdBy);
      UserDto userDto = createUserUseCase.execute(command);
      UserResponse response = mapper.toResponse(userDto);

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   /**
    * GET /users - Listar usuarios con paginación
    */
   @GetMapping
   @PreAuthorize("hasAnyRole('ADMIN', 'AUTORIDAD')")
   public ResponseEntity<PagedResponse<UserResponse>> listUsers(
         @RequestParam(required = false) String search,
         @RequestParam(required = false) Boolean status,
         @RequestParam(required = false) String role,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      // Construir criterios
      UserSearchCriteria criteria = new UserSearchCriteria(
            search,        // searchTerm
            status,        // status
            null,          // emailVerified
            false,         // includeDeleted
            role,          // roleName
            "createdAt",   // sortBy
            "desc"         // sortDirection
      );

      PagedResponse<UserDto> pagedDto = listUsersUseCase.execute(criteria, page, size);

      // Convertir a UserResponse
      PagedResponse<UserResponse> response = new PagedResponse<>(
            pagedDto.content().stream()
                  .map(mapper::toResponse)
                  .collect(Collectors.toList()),
            pagedDto.currentPage(),
            pagedDto.pageSize(),
            pagedDto.totalElements(),
            pagedDto.totalPages(),
            pagedDto.first(),
            pagedDto.last(),
            pagedDto.hasNext(),
            pagedDto.hasPrevious()
      );

      return ResponseEntity.ok(response);
   }

   /**
    * GET /users/active - Solo usuarios activos
    */
   @GetMapping("/active")
   @PreAuthorize("hasAnyRole('ADMIN', 'AUTORIDAD')")
   public ResponseEntity<PagedResponse<UserResponse>> listActiveUsers(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      UserSearchCriteria criteria = new UserSearchCriteria(
            null,
            true,          // solo activos
            null,
            false,
            null,
            "createdAt",
            "desc"
      );

      PagedResponse<UserDto> pagedDto = listUsersUseCase.execute(criteria, page, size);

      PagedResponse<UserResponse> response = new PagedResponse<>(
            pagedDto.content().stream()
                  .map(mapper::toResponse)
                  .collect(Collectors.toList()),
            pagedDto.currentPage(),
            pagedDto.pageSize(),
            pagedDto.totalElements(),
            pagedDto.totalPages(),
            pagedDto.first(),
            pagedDto.last(),
            pagedDto.hasNext(),
            pagedDto.hasPrevious()
      );

      return ResponseEntity.ok(response);
   }

   /**
    * GET /users/{id} - Obtener usuario
    */
   @GetMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
   public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
      UserDto userDto = getUserUseCase.execute(id);
      UserResponse response = mapper.toResponse(userDto);
      return ResponseEntity.ok(response);
   }

   /**
    * PUT /users/{id} - Actualizar usuario
    */
   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
   public ResponseEntity<UserResponse> updateUser(
         @PathVariable Long id,
         @Valid @RequestBody UpdateUserRequest request,
         Authentication authentication) {

      Long updatedBy = Long.parseLong(authentication.getName());
      UpdateUserCommand command = mapper.toCommand(id, request, updatedBy);
      UserDto userDto = updateUserUseCase.execute(command);
      UserResponse response = mapper.toResponse(userDto);

      return ResponseEntity.ok(response);
   }

   /**
    * DELETE /users/{id} - Eliminar usuario (soft delete)
    */
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<MessageResponse> deleteUser(
         @PathVariable Long id,
         Authentication authentication) {

      Long deletedBy = Long.parseLong(authentication.getName());
      deleteUserUseCase.executeDelete(id, deletedBy);

      return ResponseEntity.ok(
            new MessageResponse("Usuario eliminado exitosamente")
      );
   }

   /**
    * POST /users/{id}/restore - Restaurar usuario
    */
   @PostMapping("/{id}/restore")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UserResponse> restoreUser(
         @PathVariable Long id,
         Authentication authentication) {

      Long restoredBy = Long.parseLong(authentication.getName());
      UserDto userDto = restoreUserUseCase.executeRestore(id, restoredBy);
      UserResponse response = mapper.toResponse(userDto);

      return ResponseEntity.ok(response);
   }
}