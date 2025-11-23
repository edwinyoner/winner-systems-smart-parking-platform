package com.winnersystems.smartparking.auth.application.service.user;

import com.winnersystems.smartparking.auth.application.dto.command.CreateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.command.UpdateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.port.input.user.*;
import com.winnersystems.smartparking.auth.application.port.output.*;
import com.winnersystems.smartparking.auth.domain.exception.*;
import com.winnersystems.smartparking.auth.domain.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de usuarios.
 * Implementa TODOS los casos de uso CRUD de usuarios.
 */
public class UserService implements
      CreateUserUseCase,
      UpdateUserUseCase,
      GetUserUseCase,
      ListUsersUseCase,
      DeleteUserUseCase {

   // ========== PUERTOS DE SALIDA ==========
   private final UserPersistencePort userRepository;
   private final RolePersistencePort roleRepository;
   private final TokenPersistencePort tokenRepository;
   private final PasswordEncoderPort passwordEncoder;
   private final EmailPort emailService;
   private final CaptchaPort captchaService;

   public UserService(
         UserPersistencePort userRepository,
         RolePersistencePort roleRepository,
         TokenPersistencePort tokenRepository,
         PasswordEncoderPort passwordEncoder,
         EmailPort emailService,
         CaptchaPort captchaService) {
      this.userRepository = userRepository;
      this.roleRepository = roleRepository;
      this.tokenRepository = tokenRepository;
      this.passwordEncoder = passwordEncoder;
      this.emailService = emailService;
      this.captchaService = captchaService;
   }

   // ========== IMPLEMENTACIÓN: CREATE USER ==========

   @Override
   public UserDto execute(CreateUserCommand command) {
      // 1. Validar comando
      command.validate();

      // 2. Validar CAPTCHA (si aplica)
      if (command.captchaToken() != null) {
         if (!captchaService.validateCaptcha(command.captchaToken(), null)) {
            throw new InvalidCaptchaException();
         }
      }

      // 3. Verificar que el email NO exista
      if (userRepository.existsByEmail(command.email())) {
         throw new EmailAlreadyExistsException(command.email());
      }

      // 4. Encriptar contraseña
      String encodedPassword = passwordEncoder.encode(command.password());

      // 5. Crear entidad User
      User user = new User(
            command.firstName(),
            command.lastName(),
            command.email(),
            encodedPassword
      );
      user.setPhoneNumber(command.phoneNumber());

      // 6. Asignar roles
      command.roles().forEach(roleType -> {
         Role role = roleRepository.findByRoleType(roleType)
               .orElseThrow(() -> new RuntimeException(
                     "Rol no encontrado: " + roleType));
         user.assignRole(role);
      });

      // 7. Guardar en BD
      User savedUser = userRepository.save(user);

      // 8. Generar token de verificación de email
      VerificationToken verificationToken = new VerificationToken(savedUser);
      tokenRepository.saveVerificationToken(verificationToken);

      // 9. Enviar email de bienvenida
      emailService.sendWelcomeEmail(savedUser, verificationToken.getToken());

      // 10. Retornar DTO
      return mapToDto(savedUser);
   }

   // ========== IMPLEMENTACIÓN: UPDATE USER ==========

   @Override
   public UserDto execute(UpdateUserCommand command) {
      // 1. Validar
      command.validate();

      // 2. Buscar usuario
      User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

      // 3. Actualizar datos básicos
      user.updateProfile(
            command.firstName(),
            command.lastName(),
            command.phoneNumber()
      );

      // 4. Actualizar status si cambió
      if (command.status() != user.getStatus()) {
         user.setStatus(command.status());
      }

      // 5. Actualizar roles si cambiaron
      if (command.roles() != null && !command.roles().isEmpty()) {
         // Limpiar roles actuales
         user.getRoles().clear();

         // Agregar nuevos roles
         command.roles().forEach(roleType -> {
            Role role = roleRepository.findByRoleType(roleType)
                  .orElseThrow(() -> new RuntimeException(
                        "Rol no encontrado: " + roleType));
            user.assignRole(role);
         });
      }

      // 6. Guardar
      User updatedUser = userRepository.save(user);

      // 7. Retornar DTO
      return mapToDto(updatedUser);
   }

   // ========== IMPLEMENTACIÓN: GET USER ==========

   @Override
   public UserDto getById(Long userId) {
      User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
      return mapToDto(user);
   }

   @Override
   public UserDto executeByEmail(String email) {
      User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
      return mapToDto(user);
   }

   // ========== IMPLEMENTACIÓN: LIST USERS ==========

   @Override
   public List<UserDto> execute() {
      return userRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
   }

   @Override
   public List<UserDto> execute(int page, int size) {
      return userRepository.findAll(page, size).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
   }

   @Override
   public List<UserDto> executeActiveUsers() {
      return userRepository.findActiveUsers().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
   }

   // ========== IMPLEMENTACIÓN: DELETE USER ==========

   @Override
   public void deleteById(Long userId) {
      // 1. Buscar usuario
      User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

      // 2. Soft delete
      user.markAsDeleted();

      // 3. Revocar tokens
      tokenRepository.revokeAllRefreshTokensByUser(user);

      // 4. Guardar
      userRepository.save(user);
   }

   @Override
   public void executePermanent(Long userId) {
      // Hard delete - eliminar permanentemente
      // USAR CON CUIDADO
      userRepository.deleteById(userId);
   }

   // ========== HELPER: Mapeo a DTO ==========

   private UserDto mapToDto(User user) {
      return new UserDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getProfilePicture(),
            user.getStatus(),
            user.isEmailVerified(),
            null, // roles (lazy loading - se pueden cargar aparte si se necesitan)
            user.getLastLoginAt(),
            user.getCreatedAt(),
            user.getUpdatedAt()
      );
   }
}