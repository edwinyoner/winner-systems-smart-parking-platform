package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: Obtener usuario por ID.
 *
 * <p>Retorna información completa del usuario incluyendo sus roles.
 * Solo devuelve usuarios activos (deletedAt == null).</p>
 *
 * <p><b>Autorización:</b></p>
 * <ul>
 *   <li>Usuario puede obtener su propio perfil</li>
 *   <li>ADMIN puede obtener cualquier perfil</li>
 *   <li>Usuarios con permiso users.read pueden ver perfiles</li>
 * </ul>
 *
 * @throws UserNotFoundException si el usuario no existe o está eliminado
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface GetUserUseCase {
   /**
    * Obtiene un usuario por su ID.
    *
    * @param userId ID del usuario
    * @return usuario con sus roles
    * @throws UserNotFoundException si el usuario no existe o está eliminado
    */
   UserDto execute(Long userId);
}