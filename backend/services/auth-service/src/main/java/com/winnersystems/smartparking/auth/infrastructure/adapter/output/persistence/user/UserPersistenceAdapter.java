package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user;

import com.winnersystems.smartparking.auth.application.dto.query.UserSearchCriteria;
import com.winnersystems.smartparking.auth.application.port.output.UserPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.mapper.UserPersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para User.
 *
 * <p>Implementa UserPersistencePort usando JPA/Hibernate.</p>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

   private final UserRepository userRepository;
   private final UserPersistenceMapper mapper;

   @Override
   public User save(User user) {
      UserEntity entity = mapper.toEntity(user);
      UserEntity savedEntity = userRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   public Optional<User> findById(Long id) {
      return userRepository.findById(id)
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain);
   }

   @Override
   public Optional<User> findByEmail(String email) {
      return userRepository.findByEmail(email)
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain);
   }

   @Override
   public List<User> findByCriteria(UserSearchCriteria criteria, int page, int size) {
      // Crear especificación dinámica
      Specification<UserEntity> spec = buildSpecification(criteria);

      // Crear paginación con ordenamiento
      Sort sort = Sort.by(Sort.Direction.fromString(criteria.sortDirection()), criteria.sortBy());
      PageRequest pageRequest = PageRequest.of(page, size, sort);

      // Ejecutar consulta
      Page<UserEntity> pageResult = userRepository.findAll(spec, pageRequest);

      // Convertir a dominio
      return pageResult.getContent().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public long countByCriteria(UserSearchCriteria criteria) {
      Specification<UserEntity> spec = buildSpecification(criteria);
      return userRepository.count(spec);
   }

   // ========== HELPER: BUILD SPECIFICATION ==========

   /**
    * Construye especificación JPA dinámica basada en criterios de búsqueda.
    */
   private Specification<UserEntity> buildSpecification(UserSearchCriteria criteria) {
      return (root, query, cb) -> {
         var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

         // Filtrar por searchTerm (buscar en firstName, lastName, email)
         if (criteria.searchTerm() != null && !criteria.searchTerm().isEmpty()) {
            String searchPattern = "%" + criteria.searchTerm().toLowerCase() + "%";
            predicates.add(cb.or(
                  cb.like(cb.lower(root.get("firstName")), searchPattern),
                  cb.like(cb.lower(root.get("lastName")), searchPattern),
                  cb.like(cb.lower(root.get("email")), searchPattern)
            ));
         }

         // Filtrar por status
         if (criteria.status() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.status()));
         }

         // Filtrar por emailVerified
         if (criteria.emailVerified() != null) {
            predicates.add(cb.equal(root.get("emailVerified"), criteria.emailVerified()));
         }

         // Filtrar por rol
         if (criteria.roleName() != null && !criteria.roleName().isEmpty()) {
            predicates.add(cb.isMember(
                  criteria.roleName(),
                  root.join("roles").get("name")
            ));
         }

         // Incluir/excluir eliminados
         if (criteria.includeDeleted() == null || !criteria.includeDeleted()) {
            predicates.add(cb.isNull(root.get("deletedAt")));
         }

         return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
      };
   }
}