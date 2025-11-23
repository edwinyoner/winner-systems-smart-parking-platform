package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.command.UpdateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: ACTUALIZAR USUARIO
 */
public interface UpdateUserUseCase {

   /**
    * Actualiza la información de un usuario
    *
    * Flujo:
    * 1. Buscar usuario por ID
    * 2. Validar que existe
    * 3. Actualizar datos (nombre, teléfono, etc.)
    * 4. Actualizar roles si cambiaron
    * 5. Actualizar status si cambió
    * 6. Guardar cambios
    * 7. Retornar UserDto actualizado
    *
    * @param command datos a actualizar
    * @return usuario actualizado
    * @throws UserNotFoundException si el usuario no existe
    */
   UserDto execute(UpdateUserCommand command);
}