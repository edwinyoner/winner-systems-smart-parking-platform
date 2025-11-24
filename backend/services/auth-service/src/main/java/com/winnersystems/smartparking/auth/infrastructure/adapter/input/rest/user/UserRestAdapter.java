package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user;

import com.winnersystems.smartparking.auth.application.dto.command.CreateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.command.UpdateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.port.input.user.*;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.MessageResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.CreateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request.UpdateUserRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.response.UserResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.mapper.UserRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller para gestión de usuarios (CRUD).
 *
 * ENDPOINTS:
 * POST   /api/users           - Crear usuario (Admin)
 * GET    /api/users           - Listar todos los usuarios (Admin)
 * GET    /api/users/{id}      - Obtener usuario por ID (Admin/Own)
 * PUT    /api/users/{id}      - Actualizar usuario (Admin/Own)
 * DELETE /api/users/{id}      - Eliminar usuario (Admin)
 * GET    /api/users/active    - Listar usuarios activos (Admin)
 */
@RestController
@RequestMapping("/api/users")
public class UserRestAdapter {

   private final CreateUserUseCase createUserUseCase;
   private final UpdateUserUseCase updateUserUseCase;
   private final GetUserUseCase getUserUseCase;
   private final ListUsersUseCase listUsersUseCase;
   private final DeleteUserUseCase deleteUserUseCase;
   private final UserRestMapper mapper;

   public UserRestAdapter(
         CreateUserUseCase createUserUseCase,
         UpdateUserUseCase updateUserUseCase,
         GetUserUseCase getUserUseCase,
         ListUsersUseCase listUsersUseCase,
         DeleteUserUseCase deleteUserUseCase,
         UserRestMapper mapper) {
      this.createUserUseCase = createUserUseCase;
      this.updateUserUseCase = updateUserUseCase;
      this.getUserUseCase = getUserUseCase;
      this.listUsersUseCase = listUsersUseCase;
      this.deleteUserUseCase = deleteUserUseCase;
      this.mapper = mapper;
   }

   /**
    * POST /api/users
    * Crear un nuevo usuario
    * Requiere rol ADMIN
    */
   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
      CreateUserCommand command = mapper.toCommand(request);
      UserDto userDto = createUserUseCase.execute(command);
      UserResponse response = mapper.toResponse(userDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   /**
    * GET /api/users
    * Listar todos los usuarios
    * Requiere rol ADMIN o AUTORIDAD
    */
   @GetMapping
   @PreAuthorize("hasAnyRole('ADMIN', 'AUTORIDAD')")
   public ResponseEntity<List<UserResponse>> listUsers() {
      List<UserDto> users = listUsersUseCase.execute();
      List<UserResponse> response = users.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/users/active
    * Listar solo usuarios activos
    * Requiere rol ADMIN o AUTORIDAD
    */
   @GetMapping("/active")
   @PreAuthorize("hasAnyRole('ADMIN', 'AUTORIDAD')")
   public ResponseEntity<List<UserResponse>> listActiveUsers() {
      List<UserDto> users = listUsersUseCase.executeActiveUsers();
      List<UserResponse> response = users.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/users/{id}
    * Obtener un usuario por ID
    * Requiere rol ADMIN o ser el mismo usuario
    */
   @GetMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
   public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
      UserDto userDto = getUserUseCase.getById(id);
      UserResponse response = mapper.toResponse(userDto);
      return ResponseEntity.ok(response);
   }

   /**
    * PUT /api/users/{id}
    * Actualizar un usuario
    * Requiere rol ADMIN o ser el mismo usuario
    */
   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
   public ResponseEntity<UserResponse> updateUser(
         @PathVariable Long id,
         @Valid @RequestBody UpdateUserRequest request) {

      UpdateUserCommand command = mapper.toCommand(id, request);
      UserDto userDto = updateUserUseCase.execute(command);
      UserResponse response = mapper.toResponse(userDto);
      return ResponseEntity.ok(response);
   }

   /**
    * DELETE /api/users/{id}
    * Eliminar un usuario (soft delete)
    * Requiere rol ADMIN
    */
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
      deleteUserUseCase.deleteById(id);
      return ResponseEntity.ok(new MessageResponse("Usuario eliminado exitosamente"));
   }
}