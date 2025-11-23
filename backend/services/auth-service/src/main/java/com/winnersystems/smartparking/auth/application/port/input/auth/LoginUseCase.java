package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;

/**
 * Caso de uso: INICIAR SESIÓN
 *
 * ¿Qué es un Use Case?
 * Es una INTERFACE que define una acción específica del sistema.
 * Representa un caso de uso del negocio.
 *
 * Este Use Case será implementado por AuthService en la carpeta service.
 */
public interface LoginUseCase {

   /**
    * Autentica un usuario y genera tokens
    *
    * Flujo:
    * 1. Validar captcha
    * 2. Buscar usuario por email
    * 3. Verificar contraseña
    * 4. Verificar que el usuario esté activo
    * 5. Generar access token y refresh token
    * 6. Actualizar última fecha de login
    * 7. Retornar tokens + info del usuario
    *
    * @param command datos de login (email, password, captcha)
    * @return respuesta con tokens y datos del usuario
    * @throws InvalidCredentialsException si credenciales inválidas
    * @throws InvalidCaptchaException si captcha inválido
    * @throws UserNotFoundException si usuario no existe
    */
   AuthResponseDto execute(LoginCommand command);
}