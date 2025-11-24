package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.PasswordResetTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository para PasswordResetToken (token para restablecer contraseña)
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

   /**
    * Busca password reset token por el token string
    */
   Optional<PasswordResetTokenEntity> findByToken(String token);

   /**
    * Busca password reset token por usuario (el último)
    */
   Optional<PasswordResetTokenEntity> findFirstByUserOrderByCreatedAtDesc(UserEntity user);

   /**
    * Elimina tokens expirados (limpieza periódica)
    */
   @Modifying
   @Query("DELETE FROM PasswordResetTokenEntity prt WHERE prt.expiresAt < :now")
   void deleteExpiredTokens(@Param("now") LocalDateTime now);

   /**
    * Elimina tokens usados antiguos (más de 7 días)
    */
   @Modifying
   @Query("DELETE FROM PasswordResetTokenEntity prt WHERE prt.used = true AND prt.usedAt < :threshold")
   void deleteOldUsedTokens(@Param("threshold") LocalDateTime threshold);

   /**
    * Cuenta cuántos tokens se crearon recientemente para un usuario (últimos 5 minutos)
    * Útil para prevenir spam de solicitudes
    */
   @Query("SELECT COUNT(prt) FROM PasswordResetTokenEntity prt WHERE prt.user = :user AND prt.createdAt > :threshold")
   long countRecentByUser(@Param("user") UserEntity user, @Param("threshold") LocalDateTime threshold);
}