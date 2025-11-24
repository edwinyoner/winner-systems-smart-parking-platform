package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.VerificationTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository para VerificationToken (token de verificación de email)
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {

   /**
    * Busca verification token por el token string
    */
   Optional<VerificationTokenEntity> findByToken(String token);

   /**
    * Busca verification token por usuario (el último)
    */
   Optional<VerificationTokenEntity> findFirstByUserOrderByCreatedAtDesc(UserEntity user);

   /**
    * Elimina tokens expirados (limpieza periódica)
    */
   @Modifying
   @Query("DELETE FROM VerificationTokenEntity vt WHERE vt.expiresAt < :now")
   void deleteExpiredTokens(@Param("now") LocalDateTime now);

   /**
    * Elimina tokens usados antiguos (más de 30 días)
    */
   @Modifying
   @Query("DELETE FROM VerificationTokenEntity vt WHERE vt.used = true AND vt.usedAt < :threshold")
   void deleteOldUsedTokens(@Param("threshold") LocalDateTime threshold);
}