package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;

/**
 * Caso de uso: RESTABLECER CONTRASEÑA
 *
 * Usuario hace click en el link del email y establece una nueva contraseña.
 */
public interface ResetPasswordUseCase {

   /**
    * Restablece la contraseña usando el token de reset
    *
    * Flujo:
    * 1. Validar que el token existe
    * 2. Validar que NO esté usado
    * 3. Validar que NO haya expirado
    * 4. Validar que las contraseñas coincidan
    * 5. Obtener el usuario asociado
    * 6. Encriptar la nueva contraseña
    * 7. Actualizar la contraseña del usuario
    * 8. Marcar el token como usado
    * 9. Revocar todos los refresh tokens del usuario (por seguridad)
    * 10. Enviar email de confirmación
    *
    * @param command token, nueva contraseña, confirmación
    * @throws TokenExpiredException si el token expiró
    */
   void execute(ResetPasswordCommand command);
}