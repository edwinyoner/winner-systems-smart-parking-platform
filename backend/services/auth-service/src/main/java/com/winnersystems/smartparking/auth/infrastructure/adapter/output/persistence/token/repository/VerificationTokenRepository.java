package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio JPA para VerificationToken.
 *
 * @author Edwin Yoner - Winner Systems
 * @version 1.0
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {

   /**
    * Busca verification token por UUID.
    * Búsqueda O(1) con índice único.
    *
    * @param token valor UUID del token
    * @return Optional con el token si existe
    */
   Optional<VerificationTokenEntity> findByToken(String token);

   /**
    * Cuenta verification tokens creados desde cierta fecha.
    * Usado para rate limiting (máx 3 por hora).
    *
    * @param userId ID del usuario
    * @param since fecha desde la cual contar
    * @return cantidad de tokens creados después de 'since'
    */
   long countByUserIdAndCreatedAtAfter(Long userId, LocalDateTime since);

   /**
    * Elimina todos los verification tokens de un usuario.
    * Usado al reenviar verificación (limpiar tokens previos).
    *
    * @param userId ID del usuario
    */
   void deleteByUserId(Long userId);
}