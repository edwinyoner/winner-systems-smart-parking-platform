package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.PasswordResetToken;
import com.winnersystems.smartparking.auth.domain.model.RefreshToken;
import com.winnersystems.smartparking.auth.domain.model.VerificationToken;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de tokens.
 *
 * <p>Maneja 3 tipos de tokens del sistema:</p>
 * <ul>
 *   <li><b>RefreshToken:</b> Renovar access tokens (30 días)</li>
 *   <li><b>VerificationToken:</b> Verificar emails (24 horas)</li>
 *   <li><b>PasswordResetToken:</b> Reset de contraseñas (1 hora)</li>
 * </ul>
 *
 * <h3>Arquitectura de Tokens - UUID v4</h3>
 * <p>Todos los tokens son UUID v4 (128 bits de entropía = 2^122 combinaciones).
 * Se almacenan en texto plano con índice único en base de datos.</p>
 *
 * <p><b>Flujo de búsqueda:</b></p>
 * <ol>
 *   <li>Buscar directamente por token UUID: findXxxByToken(token)</li>
 *   <li>Base de datos usa índice único para búsqueda O(1)</li>
 *   <li>Procesar token encontrado</li>
 * </ol>
 *
 * <p><b>Seguridad:</b></p>
 * <ul>
 *   <li>UUID v4 es criptográficamente seguro (imposible de adivinar)</li>
 *   <li>Índice único previene duplicados</li>
 *   <li>Expiración automática por tiempo</li>
 *   <li>Revocación manual al hacer logout o cambiar password</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface TokenPersistencePort {

   // ========== REFRESH TOKEN ==========

   /**
    * Guarda un nuevo refresh token o actualiza uno existente.
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>LoginUseCase - Crear nuevo refresh token</li>
    *   <li>LogoutUseCase - Actualizar con revoked=true</li>
    *   <li>RefreshTokenUseCase - Renovar access token</li>
    * </ul>
    *
    * @param token refresh token a guardar (con UUID en texto plano)
    * @return refresh token guardado con ID generado
    */
   RefreshToken saveRefreshToken(RefreshToken token);

   /**
    * Busca un refresh token por su valor UUID.
    *
    * <p>Búsqueda directa O(1) usando índice único en base de datos.</p>
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>LogoutUseCase - Buscar token para revocar</li>
    *   <li>RefreshTokenUseCase - Buscar token para renovar</li>
    * </ul>
    *
    * @param token valor UUID del refresh token
    * @return Optional con el token si existe, Optional.empty() si no
    */
   Optional<RefreshToken> findRefreshTokenByToken(String token);

   /**
    * Revoca TODOS los refresh tokens de un usuario.
    * Actualiza revoked=true para todos los tokens del userId.
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>ResetPasswordUseCase - Forzar re-login al cambiar password</li>
    * </ul>
    *
    * @param userId ID del usuario
    */
   void revokeRefreshTokensByUserId(Long userId);

   // ========== VERIFICATION TOKEN ==========

   /**
    * Guarda un nuevo verification token o actualiza uno existente.
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>CreateUserUseCase - Crear token de verificación inicial</li>
    *   <li>ResendVerificationUseCase - Crear nuevo token</li>
    *   <li>VerifyEmailUseCase - Actualizar con verified=true</li>
    * </ul>
    *
    * @param token verification token a guardar (con UUID en texto plano)
    * @return verification token guardado con ID generado
    */
   VerificationToken saveVerificationToken(VerificationToken token);

   /**
    * Busca un verification token por su valor UUID.
    *
    * <p>Búsqueda directa O(1) usando índice único en base de datos.</p>
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>VerifyEmailUseCase - Buscar token para verificar email</li>
    * </ul>
    *
    * @param token valor UUID del verification token
    * @return Optional con el token si existe, Optional.empty() si no
    */
   Optional<VerificationToken> findVerificationTokenByToken(String token);

   /**
    * Cuenta verification tokens creados desde cierta fecha por un usuario.
    *
    * <p><b>CRÍTICO para rate limiting:</b> Máximo 3 reenvíos por hora.</p>
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>ResendVerificationUseCase - Prevenir spam de verificación</li>
    * </ul>
    *
    * @param userId ID del usuario
    * @param since fecha desde la cual contar (típicamente: now - 1 hora)
    * @return cantidad de tokens creados después de 'since'
    */
   long countVerificationTokensByUserSince(Long userId, LocalDateTime since);

   /**
    * Elimina TODOS los verification tokens de un usuario.
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>ResendVerificationUseCase - Limpiar tokens previos al crear uno nuevo</li>
    * </ul>
    *
    * <p>Garantiza que solo un token de verificación esté activo a la vez.</p>
    *
    * @param userId ID del usuario
    */
   void deleteVerificationTokensByUserId(Long userId);

   // ========== PASSWORD RESET TOKEN ==========

   /**
    * Guarda un nuevo password reset token o actualiza uno existente.
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>ForgotPasswordUseCase - Crear nuevo token de reset</li>
    *   <li>ResetPasswordUseCase - Actualizar con used=true</li>
    * </ul>
    *
    * @param token password reset token a guardar (con UUID en texto plano)
    * @return password reset token guardado con ID generado
    */
   PasswordResetToken savePasswordResetToken(PasswordResetToken token);

   /**
    * Busca un password reset token por su valor UUID.
    *
    * <p>Búsqueda directa O(1) usando índice único en base de datos.</p>
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>ResetPasswordUseCase - Buscar token para resetear password</li>
    * </ul>
    *
    * @param token valor UUID del password reset token
    * @return Optional con el token si existe, Optional.empty() si no
    */
   Optional<PasswordResetToken> findPasswordResetTokenByToken(String token);

   /**
    * Cuenta password reset tokens creados en la última hora por un usuario.
    *
    * <p><b>CRÍTICO para rate limiting:</b> Máximo 3 solicitudes por hora.</p>
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>ForgotPasswordUseCase - Prevenir spam de reset password</li>
    * </ul>
    *
    * @param userId ID del usuario
    * @param since fecha desde la cual contar (típicamente: now - 1 hora)
    * @return cantidad de tokens creados después de 'since'
    */
   long countPasswordResetTokensByUserSince(Long userId, LocalDateTime since);

   /**
    * Revoca (marca como usado) TODOS los password reset tokens de un usuario.
    * Actualiza used=true para todos los tokens del userId.
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>ForgotPasswordUseCase - Revocar tokens previos al crear uno nuevo</li>
    * </ul>
    *
    * <p>Garantiza que solo un token de reset esté activo a la vez.</p>
    *
    * @param userId ID del usuario
    */
   void revokePasswordResetTokensByUserId(Long userId);
}