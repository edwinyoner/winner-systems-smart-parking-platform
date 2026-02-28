package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Command para actualizar perfil del usuario.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record UpdateProfileCommand(
      Long userId,
      String firstName,
      String lastName,
      String phoneNumber,
      String profilePicture
) {}