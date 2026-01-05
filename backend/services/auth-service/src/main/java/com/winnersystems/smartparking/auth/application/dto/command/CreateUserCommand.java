package com.winnersystems.smartparking.auth.application.dto.command;

import java.util.Set;

/**
 * Comando para crear un nuevo usuario interno del sistema.
 *
 * <p>Este comando es parte de la capa de Application y NO contiene validaciones
 * de framework. Las validaciones se realizan en la capa de Infrastructure
 * mediante CreateUserRequest.java con Jakarta Validation.</p>
 *
 * @param firstName Nombre del usuario
 * @param lastName Apellido del usuario
 * @param email Email único del usuario
 * @param password Contraseña en texto plano (se hasheará con BCrypt)
 * @param phoneNumber Teléfono del usuario (opcional)
 * @param roleIds IDs de roles a asignar al usuario
 * @param captchaToken Token de reCAPTCHA v3 para validación anti-bot
 * @param ipAddress IP desde donde se realiza la solicitud (auditoría)
 * @param createdBy ID del usuario administrador que crea este usuario
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record CreateUserCommand(
      String firstName,
      String lastName,
      String email,
      String password,
      String phoneNumber,
      Set<Long> roleIds,
      String captchaToken,
      String ipAddress,
      Long createdBy
) {
}