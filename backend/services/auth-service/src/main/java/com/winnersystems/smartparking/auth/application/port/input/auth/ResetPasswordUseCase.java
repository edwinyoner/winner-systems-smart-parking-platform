package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;

/**
 * Caso de uso: Restablecer contraseña.
 *
 * <p>Usuario hace clic en el link del email y establece una nueva contraseña.
 * Este es el paso 2 del flujo de recuperación de contraseña.</p>
 *
 * <h3>Autorización</h3>
 * <p>Este endpoint es público (no requiere autenticación).
 * La autorización se valida mediante el token de reset.</p>
 *
 * <h3>Funcionalidades</h3>
 * <ul>
 *   <li>Validación de token de reset (no expirado, no usado)</li>
 *   <li>Actualización segura de contraseña con BCrypt</li>
 *   <li>Revocación de todos los refresh tokens (forzar re-login)</li>
 *   <li>Email de confirmación de cambio</li>
 *   <li>Token de un solo uso (se marca como usado)</li>
 *   <li>Validación opcional de IP de origen</li>
 * </ul>
 *
 * <h3>Proceso ejecutado</h3>
 * <ol>
 *   <li>Buscar token en BD (comparar con BCrypt)</li>
 *   <li>Validar que NO esté usado (used = false)</li>
 *   <li>Validar que NO haya expirado (expiresAt > now)</li>
 *   <li>Validar que passwords coincidan (newPassword = confirmPassword)</li>
 *   <li>Obtener usuario asociado al token</li>
 *   <li>Hashear nueva contraseña con BCrypt</li>
 *   <li>Actualizar password del usuario</li>
 *   <li>Marcar token como usado (no puede reutilizarse)</li>
 *   <li>Revocar TODOS los refresh tokens del usuario</li>
 *   <li>Enviar email de confirmación de cambio</li>
 * </ol>
 *
 * <h3>Seguridad</h3>
 * <ul>
 *   <li><b>Token de un solo uso:</b> Se marca como usado, no puede reutilizarse</li>
 *   <li><b>Expiración corta:</b> Token válido solo 1 hora</li>
 *   <li><b>Password hasheado:</b> Se guarda con BCrypt (irreversible)</li>
 *   <li><b>Revocación de sesiones:</b> Todos los refresh tokens son revocados</li>
 *   <li><b>Validación de IP:</b> Opcionalmente se puede validar que IP coincida</li>
 *   <li><b>Email de confirmación:</b> Usuario es notificado del cambio</li>
 * </ul>
 *
 * @throws TokenExpiredException si el token expiró
 * @throws IllegalStateException si el token ya fue usado
 * @throws IllegalArgumentException si las passwords no coinciden
 * @throws UserNotFoundException si el usuario asociado no existe
 * @throws IllegalStateException si el usuario está inactivo
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ResetPasswordUseCase {
   /**
    * Restablece la contraseña usando el token de reset.
    *
    * @param command token, nueva contraseña, confirmación, tracking
    */
   void execute(ResetPasswordCommand command);
}