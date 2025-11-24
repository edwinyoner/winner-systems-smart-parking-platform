package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user;

import com.winnersystems.smartparking.auth.application.port.output.UserPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.mapper.UserPersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para User.
 *
 * ESTE ES EL ADAPTADOR que implementa el PORT (UserPersistencePort).
 *
 * Flujo:
 * 1. AuthService (Application) llama a UserPersistencePort.save(user)
 * 2. UserPersistenceAdapter (Infrastructure) recibe la llamada
 * 3. Convierte User (domain) → UserEntity (JPA)
 * 4. Guarda en BD usando UserRepository (Spring Data)
 * 5. Convierte UserEntity → User (domain)
 * 6. Retorna User a AuthService
 *
 * Application NO conoce JPA, MySQL, ni nada técnico.
 * Infrastructure SI conoce los detalles técnicos.
 */
@Component
public class UserPersistenceAdapter implements UserPersistencePort {

   private final UserRepository userRepository;
   private final UserPersistenceMapper mapper;

   // Inyección de dependencias por constructor
   public UserPersistenceAdapter(
         UserRepository userRepository,
         UserPersistenceMapper mapper) {
      this.userRepository = userRepository;
      this.mapper = mapper;
   }

   @Override
   public User save(User user) {
      // 1. Convertir Domain → Entity
      UserEntity entity = mapper.toEntity(user);

      // 2. Guardar en BD (Spring Data JPA)
      UserEntity savedEntity = userRepository.save(entity);

      // 3. Convertir Entity → Domain
      return mapper.toDomain(savedEntity);
   }

   @Override
   public Optional<User> findById(Long id) {
      return userRepository.findById(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<User> findByEmail(String email) {
      return userRepository.findByEmail(email)
            .map(mapper::toDomain);
   }

   @Override
   public boolean existsByEmail(String email) {
      return userRepository.existsByEmail(email);
   }

   @Override
   public List<User> findAll() {
      return userRepository.findAllNotDeleted().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public List<User> findAll(int page, int size) {
      PageRequest pageRequest = PageRequest.of(page, size);
      return userRepository.findAll(pageRequest).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public List<User> findByRole(String roleName) {
      return userRepository.findByRoleType(roleName).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public List<User> findActiveUsers() {
      return userRepository.findActiveUsers().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public void deleteById(Long id) {
      // Soft delete se maneja en el servicio
      // Aquí sería hard delete si se llama
      userRepository.deleteById(id);
   }

   @Override
   public long count() {
      return userRepository.count();
   }

   @Override
   public long countActive() {
      return userRepository.countActiveUsers();
   }
}