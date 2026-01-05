package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

   /**
    * Busca password reset token por UUID.
    * Búsqueda O(1) con índice único.
    */
   Optional<PasswordResetTokenEntity> findByToken(String token);

   /**
    * Cuenta tokens creados por usuario después de una fecha.
    * CRÍTICO para rate limiting (máx 3 por hora).
    */
   long countByUserIdAndCreatedAtAfter(Long userId, LocalDateTime since);

   /**
    * Revoca TODOS los password reset tokens de un usuario.
    */
   @Modifying
   @Query("UPDATE PasswordResetTokenEntity prt SET prt.used = true, prt.usedAt = CURRENT_TIMESTAMP WHERE prt.userId = :userId")
   void revokeAllByUserId(@Param("userId") Long userId);
}