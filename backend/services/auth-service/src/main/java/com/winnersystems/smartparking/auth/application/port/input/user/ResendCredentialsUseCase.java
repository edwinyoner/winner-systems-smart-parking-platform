package com.winnersystems.smartparking.auth.application.port.input.user;

/**
 * Caso de uso: Reenviar credenciales por email.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ResendCredentialsUseCase {

   /**
    * Reenvía las credenciales de acceso a un usuario específico.
    *
    * @param userId ID del usuario
    * @param plainPassword contraseña en texto plano (temporal)
    */
   void execute(Long userId, String plainPassword);
}