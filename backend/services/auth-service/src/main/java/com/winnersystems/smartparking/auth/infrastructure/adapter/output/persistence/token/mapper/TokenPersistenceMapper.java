package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.mapper;

import com.winnersystems.smartparking.auth.domain.model.PasswordResetToken;
import com.winnersystems.smartparking.auth.domain.model.RefreshToken;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.domain.model.VerificationToken;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.PasswordResetTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.RefreshTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.VerificationTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Tokens (domain) y TokenEntities (infrastructure).
 * Maneja los 3 tipos de tokens: RefreshToken, VerificationToken, PasswordResetToken.
 */
@Component
public class TokenPersistenceMapper {

   private final UserPersistenceMapper userMapper;

   public TokenPersistenceMapper(UserPersistenceMapper userMapper) {
      this.userMapper = userMapper;
   }

   // ========== REFRESH TOKEN ==========

   public RefreshTokenEntity toEntity(RefreshToken token) {
      if (token == null) return null;

      RefreshTokenEntity entity = new RefreshTokenEntity();
      entity.setId(token.getId());
      entity.setToken(token.getToken());
      entity.setExpiresAt(token.getExpiresAt());
      entity.setRevoked(token.isRevoked());
      entity.setCreatedAt(token.getCreatedAt());

      if (token.getUser() != null) {
         entity.setUser(userMapper.toEntity(token.getUser()));
      }

      return entity;
   }

   public RefreshToken toDomain(RefreshTokenEntity entity) {
      if (entity == null) return null;

      RefreshToken token = new RefreshToken();
      token.setId(entity.getId());
      token.setToken(entity.getToken());
      token.setExpiresAt(entity.getExpiresAt());
      token.setRevoked(entity.isRevoked());
      token.setCreatedAt(entity.getCreatedAt());

      if (entity.getUser() != null) {
         token.setUser(userMapper.toDomain(entity.getUser()));
      }

      return token;
   }

   // ========== VERIFICATION TOKEN ==========

   public VerificationTokenEntity toEntity(VerificationToken token) {
      if (token == null) return null;

      VerificationTokenEntity entity = new VerificationTokenEntity();
      entity.setId(token.getId());
      entity.setToken(token.getToken());
      entity.setTokenType(token.getTokenType());
      entity.setExpiresAt(token.getExpiresAt());
      entity.setUsed(token.isUsed());
      entity.setUsedAt(token.getUsedAt());
      entity.setCreatedAt(token.getCreatedAt());

      if (token.getUser() != null) {
         entity.setUser(userMapper.toEntity(token.getUser()));
      }

      return entity;
   }

   public VerificationToken toDomain(VerificationTokenEntity entity) {
      if (entity == null) return null;

      VerificationToken token = new VerificationToken();
      token.setId(entity.getId());
      token.setToken(entity.getToken());
      token.setTokenType(entity.getTokenType());
      token.setExpiresAt(entity.getExpiresAt());
      token.setUsed(entity.isUsed());
      token.setUsedAt(entity.getUsedAt());
      token.setCreatedAt(entity.getCreatedAt());

      if (entity.getUser() != null) {
         token.setUser(userMapper.toDomain(entity.getUser()));
      }

      return token;
   }

   // ========== PASSWORD RESET TOKEN ==========

   public PasswordResetTokenEntity toEntity(PasswordResetToken token) {
      if (token == null) return null;

      PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
      entity.setId(token.getId());
      entity.setToken(token.getToken());
      entity.setTokenType(token.getTokenType());
      entity.setExpiresAt(token.getExpiresAt());
      entity.setUsed(token.isUsed());
      entity.setUsedAt(token.getUsedAt());
      entity.setIpAddress(token.getIpAddress());
      entity.setUserAgent(token.getUserAgent());
      entity.setCreatedAt(token.getCreatedAt());

      if (token.getUser() != null) {
         entity.setUser(userMapper.toEntity(token.getUser()));
      }

      return entity;
   }

   public PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
      if (entity == null) return null;

      PasswordResetToken token = new PasswordResetToken();
      token.setId(entity.getId());
      token.setToken(entity.getToken());
      token.setTokenType(entity.getTokenType());
      token.setExpiresAt(entity.getExpiresAt());
      token.setUsed(entity.isUsed());
      token.setUsedAt(entity.getUsedAt());
      token.setIpAddress(entity.getIpAddress());
      token.setUserAgent(entity.getUserAgent());
      token.setCreatedAt(entity.getCreatedAt());

      if (entity.getUser() != null) {
         token.setUser(userMapper.toDomain(entity.getUser()));
      }

      return token;
   }
}