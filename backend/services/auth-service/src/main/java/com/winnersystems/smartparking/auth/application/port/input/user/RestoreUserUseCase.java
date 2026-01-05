package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: Restaurar usuario eliminado.
 *
 * <p>Restaura un usuario previamente eliminado mediante soft delete.
 * El usuario recupera acceso al sistema.</p>
 *
 * <h3>Autorización requerida</h3>
 * <ul>
 *   <li>Rol: <b>ADMIN</b></li>
 *   <li>Permiso: <b>users.restore</b></li>
 * </ul>
 *
 * <h3>Efectos de la restauración</h3>
 * <ul>
 *   <li>deletedAt = null (ya no está eliminado)</li>
 *   <li>deletedBy = null</li>
 *   <li>status = true (cuenta reactivada)</li>
 *   <li>Usuario puede volver a iniciar sesión</li>
 *   <li>Email de notificación enviado</li>
 * </ul>
 *
 * <h3>Restricciones</h3>
 * <ul>
 *   <li>Usuario debe estar eliminado (deletedAt != null)</li>
 * </ul>
 *
 * @throws UserNotFoundException si el usuario no existe
 * @throws IllegalStateException si el usuario no está eliminado
 *
 * @see DeleteUserUseCase
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RestoreUserUseCase {

   /**
    * Restaura un usuario previamente eliminado.
    *
    * <p>El usuario recupera acceso al sistema y puede volver a iniciar sesión.
    * Se envía una notificación por email informando que su cuenta ha sido reactivada.</p>
    *
    * @param userId ID del usuario a restaurar
    * @param restoredBy ID del administrador que ejecuta la restauración
    * @return usuario restaurado con datos actualizados
    * @throws UserNotFoundException si el usuario no existe
    * @throws IllegalStateException si el usuario no está eliminado
    */
   UserDto executeRestore(Long userId, Long restoredBy);
}