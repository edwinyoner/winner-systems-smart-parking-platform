package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;

/**
 * Caso de uso: Iniciar sesión.
 *
 * <p>Autentica un usuario interno del sistema y genera tokens de acceso.</p>
 *
 * <h3>Autorización</h3>
 * <p>Este endpoint es público (no requiere autenticación previa).</p>
 *
 * <h3>Funcionalidades</h3>
 * <ul>
 *   <li>Validación anti-bot con reCAPTCHA v3</li>
 *   <li>Verificación de credenciales con BCrypt</li>
 *   <li>Generación de tokens JWT (access + refresh)</li>
 *   <li>Multi-device support (múltiples sesiones activas)</li>
 *   <li>Tracking de dispositivo e IP para auditoría</li>
 * </ul>
 *
 * <h3>Validaciones</h3>
 * <ul>
 *   <li>Captcha debe tener score mínimo 0.5</li>
 *   <li>Usuario debe existir en el sistema</li>
 *   <li>Contraseña debe coincidir (BCrypt)</li>
 *   <li>Usuario debe estar activo (status = true)</li>
 *   <li>Email debe estar verificado</li>
 * </ul>
 *
 * <h3>Tokens generados</h3>
 * <ul>
 *   <li><b>Access Token:</b> JWT con validez de 30 minutos</li>
 *   <li><b>Refresh Token:</b> UUID hasheado, validez 7-30 días según rememberMe</li>
 * </ul>
 *
 * @throws InvalidCredentialsException si credenciales inválidas
 * @throws InvalidCaptchaException si captcha inválido
 * @throws UserNotFoundException si usuario no existe
 * @throws IllegalStateException si usuario inactivo o email no verificado
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface LoginUseCase {
   /**
    * Autentica un usuario y genera tokens de acceso.
    *
    * @param command datos de login
    * @return respuesta con tokens y datos del usuario
    */
   AuthResponseDto execute(LoginCommand command);
}