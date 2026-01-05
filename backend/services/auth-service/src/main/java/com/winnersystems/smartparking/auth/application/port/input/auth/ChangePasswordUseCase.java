package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ChangePasswordCommand;

/**
 * Caso de uso: Cambiar contraseña de usuario autenticado.
 *
 * <p>Permite a un usuario autenticado cambiar su contraseña actual
 * por una nueva.</p>
 *
 * <h3>Autorización</h3>
 * <p>Este endpoint REQUIERE autenticación (JWT válido).</p>
 *
 * <h3>Funcionalidades</h3>
 * <ul>
 *   <li>Verificación de contraseña actual</li>
 *   <li>Validación de nueva contraseña (fortaleza)</li>
 *   <li>Actualización segura con BCrypt</li>
 *   <li>Revocación de refresh tokens (forzar re-login)</li>
 *   <li>Email de confirmación</li>
 * </ul>
 *
 * <h3>Proceso ejecutado</h3>
 * <ol>
 *   <li>Obtener usuario autenticado desde JWT</li>
 *   <li>Validar que passwords coincidan (newPassword = confirmPassword)</li>
 *   <li>Verificar contraseña actual con BCrypt</li>
 *   <li>Validar que nueva contraseña sea diferente a la actual</li>
 *   <li>Hashear nueva contraseña con BCrypt</li>
 *   <li>Actualizar password en BD</li>
 *   <li>Revocar TODOS los refresh tokens del usuario</li>
 *   <li>Enviar email de confirmación</li>
 * </ol>
 *
 * <h3>Seguridad</h3>
 * <ul>
 *   <li><b>Verificación de contraseña actual:</b> Evita cambios no autorizados</li>
 *   <li><b>Password hasheado:</b> BCrypt con factor 12</li>
 *   <li><b>Revocación de sesiones:</b> Forzar re-login en todos los dispositivos</li>
 *   <li><b>Email de notificación:</b> Usuario es informado del cambio</li>
 * </ul>
 *
 * @throws IllegalArgumentException si las passwords no coinciden
 * @throws IllegalStateException si la contraseña actual es incorrecta
 * @throws IllegalArgumentException si nueva contraseña es igual a la actual
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ChangePasswordUseCase {
   /**
    * Cambia la contraseña del usuario autenticado.
    *
    * @param command datos del cambio de contraseña
    */
   void execute(ChangePasswordCommand command);
}