package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.RefreshTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository para RefreshToken
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

   /**
    * Busca refresh token por el token string
    */
   Optional<RefreshTokenEntity> findByToken(String token);

   /**
    * Busca refresh token válido por usuario
    */
   @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.user = :user " +
         "AND rt.revoked = false AND rt.expiresAt > :now")
   Optional<RefreshTokenEntity> findValidByUser(
         @Param("user") UserEntity user,
         @Param("now") LocalDateTime now
   );

   /**
    * Revoca todos los tokens de un usuario
    */
   @Modifying
   @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true WHERE rt.user = :user")
   void revokeAllByUser(@Param("user") UserEntity user);

   /**
    * Elimina tokens expirados (limpieza periódica)
    */
   @Modifying
   @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiresAt < :now")
   void deleteExpiredTokens(@Param("now") LocalDateTime now);
}