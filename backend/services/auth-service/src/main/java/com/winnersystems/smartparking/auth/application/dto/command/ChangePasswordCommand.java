package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Command para cambiar contraseña de usuario autenticado.
 *
 * @param userId ID del usuario (extraído del JWT)
 * @param currentPassword contraseña actual
 * @param newPassword nueva contraseña
 * @param confirmPassword confirmación de nueva contraseña
 * @param ipAddress IP del cliente (auditoría)
 * @param userAgent User-Agent del navegador
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ChangePasswordCommand(
      Long userId,
      String currentPassword,
      String newPassword,
      String confirmPassword,
      String ipAddress,
      String userAgent
) {
   public ChangePasswordCommand {
      // Validaciones básicas
      if (userId == null || userId <= 0) {
         throw new IllegalArgumentException("userId es requerido");
      }
      if (currentPassword == null || currentPassword.isBlank()) {
         throw new IllegalArgumentException("currentPassword es requerido");
      }
      if (newPassword == null || newPassword.isBlank()) {
         throw new IllegalArgumentException("newPassword es requerido");
      }
      if (confirmPassword == null || confirmPassword.isBlank()) {
         throw new IllegalArgumentException("confirmPassword es requerido");
      }
   }
}