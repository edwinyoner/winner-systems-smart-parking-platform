package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token;

import com.winnersystems.smartparking.auth.application.port.output.TokenPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.PasswordResetToken;
import com.winnersystems.smartparking.auth.domain.model.RefreshToken;
import com.winnersystems.smartparking.auth.domain.model.VerificationToken;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.mapper.TokenPersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository.PasswordResetTokenRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository.RefreshTokenRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adaptador de persistencia para Tokens.
 * Implementa TokenPersistencePort usando JPA.
 *
 * @author Edwin Yoner - Winner Systems
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class TokenPersistenceAdapter implements TokenPersistencePort {

   private final RefreshTokenRepository refreshTokenRepository;
   private final VerificationTokenRepository verificationTokenRepository;
   private final PasswordResetTokenRepository passwordResetTokenRepository;
   private final TokenPersistenceMapper mapper;

   // ========== REFRESH TOKEN ==========

   @Override
   public RefreshToken saveRefreshToken(RefreshToken token) {
      return mapper.toDomain(
            refreshTokenRepository.save(mapper.toEntity(token))
      );
   }

   @Override
   public Optional<RefreshToken> findRefreshTokenByToken(String token) {
      return refreshTokenRepository.findByToken(token)
            .map(mapper::toDomain);
   }

   @Override
   @Transactional
   public void revokeRefreshTokensByUserId(Long userId) {
      refreshTokenRepository.revokeAllByUserId(userId);
   }

   // ========== VERIFICATION TOKEN ==========

   @Override
   public VerificationToken saveVerificationToken(VerificationToken token) {
      return mapper.toDomain(
            verificationTokenRepository.save(mapper.toEntity(token))
      );
   }

   @Override
   public Optional<VerificationToken> findVerificationTokenByToken(String token) {
      return verificationTokenRepository.findByToken(token)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<VerificationToken> findVerificationTokenByUserId(Long userId) {
      return verificationTokenRepository.findByUserId(userId)
            .map(mapper::toDomain);  // ✅ CORRECTO
   }

   @Override
   public long countVerificationTokensByUserSince(Long userId, LocalDateTime since) {
      return verificationTokenRepository.countByUserIdAndCreatedAtAfter(userId, since);
   }

   @Override
   @Transactional
   public void deleteVerificationTokensByUserId(Long userId) {
      verificationTokenRepository.deleteByUserId(userId);
   }

   // ========== PASSWORD RESET TOKEN ==========

   @Override
   public PasswordResetToken savePasswordResetToken(PasswordResetToken token) {
      return mapper.toDomain(
            passwordResetTokenRepository.save(mapper.toEntity(token))
      );
   }

   @Override
   public Optional<PasswordResetToken> findPasswordResetTokenByToken(String token) {
      return passwordResetTokenRepository.findByToken(token)
            .map(mapper::toDomain);
   }

   @Override
   public long countPasswordResetTokensByUserSince(Long userId, LocalDateTime since) {
      return passwordResetTokenRepository.countByUserIdAndCreatedAtAfter(userId, since);
   }

   @Override
   @Transactional
   public void revokePasswordResetTokensByUserId(Long userId) {
      passwordResetTokenRepository.revokeAllByUserId(userId);
   }
}