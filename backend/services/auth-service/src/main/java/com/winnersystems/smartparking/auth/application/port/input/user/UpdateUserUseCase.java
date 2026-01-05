package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.command.UpdateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: Actualizar usuario.
 *
 * <p>Permite actualizar la información de usuarios internos del sistema.
 * Los campos a actualizar dependen de los valores no-null en UpdateUserCommand.</p>
 *
 * <h3>Autorización</h3>
 * <ul>
 *   <li>Cualquier usuario puede actualizar su propio perfil (nombre, teléfono)</li>
 *   <li>ADMIN puede actualizar cualquier usuario</li>
 *   <li>Usuarios con permiso users.update pueden actualizar otros usuarios</li>
 * </ul>
 *
 * <h3>Campos actualizables</h3>
 * <ul>
 *   <li><b>firstName, lastName</b> - nombres (cualquier usuario en su propio perfil)</li>
 *   <li><b>phoneNumber</b> - teléfono (cualquier usuario en su propio perfil)</li>
 *   <li><b>profilePicture</b> - foto de perfil</li>
 *   <li><b>status</b> - activa/desactiva cuenta (solo ADMIN)</li>
 *   <li><b>roles</b> - asigna/remueve roles (solo ADMIN)</li>
 * </ul>
 *
 * <h3>Campos NO actualizables</h3>
 * <ul>
 *   <li>email - requiere verificación separada</li>
 *   <li>password - usar caso de uso de cambio de contraseña</li>
 *   <li>createdAt, createdBy - auditoría inmutable</li>
 *   <li>deletedAt, deletedBy - usar DeleteUserUseCase</li>
 * </ul>
 *
 * <h3>Reglas de actualización</h3>
 * <ul>
 *   <li>Valor <b>null</b> en command = NO actualizar ese campo</li>
 *   <li>Valor <b>no-null</b> = Actualizar a ese valor</li>
 *   <li>Usuario NO puede cambiar su propio status o roles</li>
 *   <li>Al menos 1 rol debe permanecer asignado</li>
 * </ul>
 *
 * <h3>Notificaciones automáticas</h3>
 * <ul>
 *   <li>Rol asignado → email al usuario</li>
 *   <li>Rol removido → email al usuario</li>
 *   <li>Cuenta desactivada → email al usuario</li>
 *   <li>Cuenta activada → email al usuario</li>
 * </ul>
 *
 * @throws UserNotFoundException si el usuario no existe o está eliminado
 * @throws IllegalStateException si usuario intenta cambiar su propio status/roles
 * @throws RoleNotAssignedException si algún rol especificado no existe
 * @throws IllegalArgumentException si roleIds está vacío
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface UpdateUserUseCase {

   /**
    * Actualiza la información de un usuario.
    *
    * <p>Actualiza solo los campos no-null en el comando.
    * Los campos con valor null se mantienen sin cambios.</p>
    *
    * @param command datos a actualizar (incluye userId y updatedBy)
    * @return usuario actualizado con cambios aplicados
    * @throws UserNotFoundException si el usuario no existe o está eliminado
    * @throws IllegalStateException si intenta cambiar su propio status/roles
    * @throws RoleNotAssignedException si algún rol no existe
    * @throws IllegalArgumentException si roleIds está vacío
    */
   UserDto execute(UpdateUserCommand command);
}