package com.winnersystems.smartparking.auth.application.dto.command;

import java.util.Set;

/**
 * Comando para actualizar un usuario existente.
 *
 * <p>Todos los campos excepto userId y updatedBy son opcionales.
 * Un valor null indica que ese campo NO se debe actualizar.</p>
 *
 * @param userId ID del usuario a actualizar (obligatorio)
 * @param firstName Nombre (null = no actualizar)
 * @param lastName Apellido (null = no actualizar)
 * @param phoneNumber Teléfono (null = no actualizar, "" = eliminar)
 * @param profilePicture URL foto (null = no actualizar, "" = eliminar)
 * @param roleIds Roles (null = no actualizar, Set vacío = remover todos)
 * @param updatedBy ID del administrador que actualiza (obligatorio)
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record UpdateUserCommand(
      Long userId,
      String firstName,
      String lastName,
      String phoneNumber,
      String profilePicture,
      Set<Long> roleIds,
      Long updatedBy
) {
}