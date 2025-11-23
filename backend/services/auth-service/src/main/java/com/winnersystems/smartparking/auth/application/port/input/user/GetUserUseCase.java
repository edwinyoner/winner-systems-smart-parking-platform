package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: OBTENER USUARIO
 */
public interface GetUserUseCase {

   /**
    * Obtiene un usuario por su ID
    *
    * @param userId ID del usuario
    * @return usuario encontrado
    * @throws UserNotFoundException si el usuario no existe
    */
   UserDto getById(Long userId);

   /**
    * Obtiene un usuario por su email
    *
    * @param email email del usuario
    * @return usuario encontrado
    * @throws UserNotFoundException si el usuario no existe
    */
   UserDto executeByEmail(String email);
}