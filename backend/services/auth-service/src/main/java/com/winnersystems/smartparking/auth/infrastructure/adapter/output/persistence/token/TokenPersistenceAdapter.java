package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token;

import com.winnersystems.smartparking.auth.application.port.output.TokenPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.PasswordResetToken;
import com.winnersystems.smartparking.auth.domain.model.RefreshToken;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.domain.model.VerificationToken;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.PasswordResetTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.RefreshTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.VerificationTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.mapper.TokenPersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository.PasswordResetTokenRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository.RefreshTokenRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository.VerificationTokenRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adaptador de persistencia para Tokens.
 * Implementa TokenPersistencePort usando JPA.
 * Maneja RefreshToken, VerificationToken y PasswordResetToken.
 */
@Component
public class TokenPersistenceAdapter implements TokenPersistencePort {

   private final RefreshTokenRepository refreshTokenRepository;
   private final VerificationTokenRepository verificationTokenRepository;
   private final PasswordResetTokenRepository passwordResetTokenRepository;
   private final TokenPersistenceMapper mapper;
   private final UserPersistenceMapper userMapper;

   public TokenPersistenceAdapter(
         RefreshTokenRepository refreshTokenRepository,
         VerificationTokenRepository verificationTokenRepository,
         PasswordResetTokenRepository passwordResetTokenRepository,
         TokenPersistenceMapper mapper,
         UserPersistenceMapper userMapper) {
      this.refreshTokenRepository = refreshTokenRepository;
      this.verificationTokenRepository = verificationTokenRepository;
      this.passwordResetTokenRepository = passwordResetTokenRepository;
      this.mapper = mapper;
      this.userMapper = userMapper;
   }

   // ========== REFRESH TOKEN ==========

   @Override
   public RefreshToken saveRefreshToken(RefreshToken token) {
      RefreshTokenEntity entity = mapper.toEntity(token);
      RefreshTokenEntity savedEntity = refreshTokenRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   public Optional<RefreshToken> findRefreshTokenByToken(String token) {
      return refreshTokenRepository.findByToken(token)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<RefreshToken> findValidRefreshTokenByUser(User user) {
      UserEntity userEntity = userMapper.toEntity(user);
      return refreshTokenRepository.findValidByUser(userEntity, LocalDateTime.now())
            .map(mapper::toDomain);
   }

   @Override
   @Transactional
   public void revokeAllRefreshTokensByUser(User user) {
      UserEntity userEntity = userMapper.toEntity(user);
      refreshTokenRepository.revokeAllByUser(userEntity);
   }

   @Override
   @Transactional
   public void deleteExpiredRefreshTokens() {
      refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
   }

   // ========== VERIFICATION TOKEN ==========

   @Override
   public VerificationToken saveVerificationToken(VerificationToken token) {
      VerificationTokenEntity entity = mapper.toEntity(token);
      VerificationTokenEntity savedEntity = verificationTokenRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   public Optional<VerificationToken> findVerificationTokenByToken(String token) {
      return verificationTokenRepository.findByToken(token)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<VerificationToken> findVerificationTokenByUser(User user) {
      UserEntity userEntity = userMapper.toEntity(user);
      return verificationTokenRepository.findFirstByUserOrderByCreatedAtDesc(userEntity)
            .map(mapper::toDomain);
   }

   @Override
   @Transactional
   public void deleteExpiredVerificationTokens() {
      verificationTokenRepository.deleteExpiredTokens(LocalDateTime.now());
   }

   // ========== PASSWORD RESET TOKEN ==========

   @Override
   public PasswordResetToken savePasswordResetToken(PasswordResetToken token) {
      PasswordResetTokenEntity entity = mapper.toEntity(token);
      PasswordResetTokenEntity savedEntity = passwordResetTokenRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   public Optional<PasswordResetToken> findPasswordResetTokenByToken(String token) {
      return passwordResetTokenRepository.findByToken(token)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<PasswordResetToken> findPasswordResetTokenByUser(User user) {
      UserEntity userEntity = userMapper.toEntity(user);
      return passwordResetTokenRepository.findFirstByUserOrderByCreatedAtDesc(userEntity)
            .map(mapper::toDomain);
   }

   @Override
   @Transactional
   public void deleteExpiredPasswordResetTokens() {
      passwordResetTokenRepository.deleteExpiredTokens(LocalDateTime.now());
   }
}