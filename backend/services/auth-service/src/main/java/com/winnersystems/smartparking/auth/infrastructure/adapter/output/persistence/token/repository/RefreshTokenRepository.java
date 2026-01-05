package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

   /**
    * Busca refresh token por UUID.
    * Búsqueda O(1) con índice único.
    */
   Optional<RefreshTokenEntity> findByToken(String token);

   /**
    * Revoca TODOS los refresh tokens de un usuario.
    */
   @Modifying
   @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true, rt.revokedAt = CURRENT_TIMESTAMP WHERE rt.userId = :userId")
   void revokeAllByUserId(@Param("userId") Long userId);
}