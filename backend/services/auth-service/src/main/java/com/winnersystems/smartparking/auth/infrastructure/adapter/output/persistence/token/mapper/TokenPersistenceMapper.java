package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.mapper;

import com.winnersystems.smartparking.auth.domain.model.PasswordResetToken;
import com.winnersystems.smartparking.auth.domain.model.RefreshToken;
import com.winnersystems.smartparking.auth.domain.model.VerificationToken;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.PasswordResetTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.RefreshTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.VerificationTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class TokenPersistenceMapper {

   // ========== REFRESH TOKEN ==========

   public RefreshTokenEntity toEntity(RefreshToken token) {
      if (token == null) return null;

      RefreshTokenEntity entity = new RefreshTokenEntity();
      entity.setId(token.getId());
      entity.setToken(token.getToken());  // ✅ UUID plano
      entity.setUserId(token.getUserId());
      entity.setIssuedAt(token.getIssuedAt());
      entity.setExpiresAt(token.getExpiresAt());
      entity.setRevoked(token.getRevoked());
      entity.setRevokedAt(token.getRevokedAt());
      entity.setDeviceInfo(token.getDeviceInfo());
      entity.setIpAddress(token.getIpAddress());
      entity.setCreatedAt(token.getCreatedAt());
      entity.setCreatedBy(token.getCreatedBy());
      entity.setUpdatedAt(token.getUpdatedAt());
      entity.setUpdatedBy(token.getUpdatedBy());

      return entity;
   }

   public RefreshToken toDomain(RefreshTokenEntity entity) {
      if (entity == null) return null;

      RefreshToken token = new RefreshToken();
      token.setId(entity.getId());
      token.setToken(entity.getToken());  // ✅ UUID plano
      token.setUserId(entity.getUserId());
      token.setIssuedAt(entity.getIssuedAt());
      token.setExpiresAt(entity.getExpiresAt());

      // ✅ Restaurar estado usando método de dominio
      if (entity.isRevoked()) {
         token.revoke();
      }

      token.setDeviceInfo(entity.getDeviceInfo());
      token.setIpAddress(entity.getIpAddress());
      token.setCreatedAt(entity.getCreatedAt());
      token.setCreatedBy(entity.getCreatedBy());
      token.setUpdatedAt(entity.getUpdatedAt());
      token.setUpdatedBy(entity.getUpdatedBy());

      return token;
   }

   // ========== VERIFICATION TOKEN ==========

   public VerificationTokenEntity toEntity(VerificationToken token) {
      if (token == null) return null;

      VerificationTokenEntity entity = new VerificationTokenEntity();
      entity.setId(token.getId());
      entity.setToken(token.getToken());  // ✅ UUID plano
      entity.setUserId(token.getUserId());
      entity.setExpiresAt(token.getExpiresAt());
      entity.setVerifiedAt(token.getVerifiedAt());
      entity.setIpAddress(token.getIpAddress());
      entity.setCreatedAt(token.getCreatedAt());
      entity.setCreatedBy(token.getCreatedBy());
      entity.setUpdatedAt(token.getUpdatedAt());
      entity.setUpdatedBy(token.getUpdatedBy());

      return entity;
   }

   public VerificationToken toDomain(VerificationTokenEntity entity) {
      if (entity == null) return null;

      VerificationToken token = new VerificationToken();
      token.setId(entity.getId());
      token.setToken(entity.getToken());  // ✅ UUID plano
      token.setUserId(entity.getUserId());
      token.setExpiresAt(entity.getExpiresAt());
      token.setVerifiedAt(entity.getVerifiedAt());
      token.setIpAddress(entity.getIpAddress());
      token.setCreatedAt(entity.getCreatedAt());
      token.setCreatedBy(entity.getCreatedBy());
      token.setUpdatedAt(entity.getUpdatedAt());
      token.setUpdatedBy(entity.getUpdatedBy());

      return token;
   }

   // ========== PASSWORD RESET TOKEN ==========

   public PasswordResetTokenEntity toEntity(PasswordResetToken token) {
      if (token == null) return null;

      PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
      entity.setId(token.getId());
      entity.setToken(token.getToken());  // ✅ UUID plano
      entity.setUserId(token.getUserId());
      entity.setExpiresAt(token.getExpiresAt());
      entity.setUsed(token.getUsed());
      entity.setUsedAt(token.getUsedAt());
      entity.setIpAddress(token.getIpAddress());
      entity.setUserAgent(token.getUserAgent());
      entity.setCreatedAt(token.getCreatedAt());
      entity.setCreatedBy(token.getCreatedBy());
      entity.setUpdatedAt(token.getUpdatedAt());
      entity.setUpdatedBy(token.getUpdatedBy());

      return entity;
   }

   public PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
      if (entity == null) return null;

      PasswordResetToken token = new PasswordResetToken();
      token.setId(entity.getId());
      token.setToken(entity.getToken());  // ✅ UUID plano
      token.setUserId(entity.getUserId());
      token.setExpiresAt(entity.getExpiresAt());

      // ✅ NO llamar markAsUsed() aquí - solo restaurar estado
      // markAsUsed() tiene validaciones que fallarían con tokens expirados
      if (entity.isUsed()) {
         // Setear campos internos directamente en este caso
         token.setUsedAt(entity.getUsedAt());
      }

      token.setIpAddress(entity.getIpAddress());
      token.setUserAgent(entity.getUserAgent());
      token.setCreatedAt(entity.getCreatedAt());
      token.setCreatedBy(entity.getCreatedBy());
      token.setUpdatedAt(entity.getUpdatedAt());
      token.setUpdatedBy(entity.getUpdatedBy());

      return token;
   }
}