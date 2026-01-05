package com.winnersystems.smartparking.auth.application.port.input.auth;

/**
 * Puerto de entrada para reenviar el email de verificación.
 *
 * <p>Permite a un usuario solicitar un nuevo email de verificación en caso de:</p>
 * <ul>
 *   <li>No haber recibido el email original</li>
 *   <li>Token de verificación expirado</li>
 *   <li>Email perdido o eliminado</li>
 * </ul>
 *
 * <p><b>Seguridad:</b> Solo se puede reenviar si el email aún no está verificado.</p>
 *
 * @author Edwin Yoner - Winner Systems
 * @version 1.0
 */
public interface ResendVerificationUseCase {

   /**
    * Reenvía el email de verificación a un usuario.
    *
    * <p>Genera un nuevo token y envía un nuevo email de verificación.</p>
    *
    * @param email Email del usuario que solicita reenvío
    * @throws com.winnersystems.smartparking.auth.domain.exception.UserNotFoundException si el usuario no existe
    * @throws IllegalStateException si el email ya está verificado
    */
   void resendVerification(String email);
}