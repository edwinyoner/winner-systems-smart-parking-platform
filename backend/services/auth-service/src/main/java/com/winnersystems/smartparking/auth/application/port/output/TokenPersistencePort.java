package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.PasswordResetToken;
import com.winnersystems.smartparking.auth.domain.model.RefreshToken;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.domain.model.VerificationToken;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de tokens
 * (RefreshToken, VerificationToken, PasswordResetToken).
 */
public interface TokenPersistencePort {

   // ========== REFRESH TOKEN ==========

   /**
    * Guarda un refresh token
    */
   RefreshToken saveRefreshToken(RefreshToken token);

   /**
    * Busca un refresh token por el token string
    */
   Optional<RefreshToken> findRefreshTokenByToken(String token);

   /**
    * Busca refresh tokens activos de un usuario
    */
   Optional<RefreshToken> findValidRefreshTokenByUser(User user);

   /**
    * Revoca todos los refresh tokens de un usuario
    */
   void revokeAllRefreshTokensByUser(User user);

   /**
    * Elimina refresh tokens expirados (limpieza periódica)
    */
   void deleteExpiredRefreshTokens();

   // ========== VERIFICATION TOKEN ==========

   /**
    * Guarda un verification token
    */
   VerificationToken saveVerificationToken(VerificationToken token);

   /**
    * Busca un verification token por el token string
    */
   Optional<VerificationToken> findVerificationTokenByToken(String token);

   /**
    * Busca verification token por usuario
    */
   Optional<VerificationToken> findVerificationTokenByUser(User user);

   /**
    * Elimina verification tokens expirados
    */
   void deleteExpiredVerificationTokens();

   // ========== PASSWORD RESET TOKEN ==========

   /**
    * Guarda un password reset token
    */
   PasswordResetToken savePasswordResetToken(PasswordResetToken token);

   /**
    * Busca un password reset token por el token string
    */
   Optional<PasswordResetToken> findPasswordResetTokenByToken(String token);

   /**
    * Busca password reset token por usuario
    */
   Optional<PasswordResetToken> findPasswordResetTokenByUser(User user);

   /**
    * Elimina password reset tokens expirados
    */
   void deleteExpiredPasswordResetTokens();
}