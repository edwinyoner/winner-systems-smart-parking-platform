package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.UpdateProfileCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: Actualizar perfil del usuario.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface UpdateProfileUseCase {
   UserDto execute(UpdateProfileCommand command);
}