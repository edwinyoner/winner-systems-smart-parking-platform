package com.winnersystems.smartparking.auth.application.port.input.auth;

/**
 * Puerto de entrada para verificar el email de un usuario.
 *
 * <p>Permite activar una cuenta de usuario mediante un token de verificación
 * enviado por email durante el proceso de registro.</p>
 *
 * <p><b>Flujo:</b></p>
 * <ol>
 *   <li>Usuario recibe email con token de verificación</li>
 *   <li>Usuario hace clic en el enlace de verificación</li>
 *   <li>Sistema valida el token y marca el email como verificado</li>
 * </ol>
 *
 * @author Edwin Yoner - Winner Systems
 * @version 1.0
 */
public interface VerifyEmailUseCase {

   /**
    * Verifica el email de un usuario mediante un token.
    *
    * @param token Token de verificación único enviado por email
    * @throws com.winnersystems.smartparking.auth.domain.exception.TokenInvalidException si el token no existe
    * @throws com.winnersystems.smartparking.auth.domain.exception.TokenExpiredException si el token expiró
    */
   void verifyEmail(String token);
}