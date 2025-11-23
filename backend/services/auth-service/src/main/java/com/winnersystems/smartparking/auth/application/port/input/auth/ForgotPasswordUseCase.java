package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;

/**
 * Caso de uso: OLVIDÉ MI CONTRASEÑA
 *
 * Usuario no recuerda su contraseña y solicita restablecerla.
 */
public interface ForgotPasswordUseCase {

   /**
    * Genera un token de reset y envía email al usuario
    *
    * Flujo:
    * 1. Validar captcha
    * 2. Buscar usuario por email
    * 3. Verificar que el usuario esté activo
    * 4. Generar token de reset (expira en 1 hora)
    * 5. Guardar token en BD
    * 6. Enviar email con link de reset
    *
    * NOTA DE SEGURIDAD:
    * Siempre retornar mensaje genérico de éxito, incluso si el email no existe.
    * Esto previene que atacantes descubran qué emails están registrados.
    *
    * @param command email del usuario
    */
   void execute(ForgotPasswordCommand command);
}