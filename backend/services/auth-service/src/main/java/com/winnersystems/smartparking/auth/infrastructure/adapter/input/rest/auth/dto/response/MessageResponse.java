package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response;

/**
 * Response DTO simple para operaciones que solo requieren confirmación.
 *
 * <p>Usado en:</p>
 * <ul>
 *   <li>POST /api/auth/logout</li>
 *   <li>POST /api/auth/forgot-password</li>
 *   <li>POST /api/auth/reset-password</li>
 * </ul>
 *
 * <p><b>Ejemplo JSON:</b></p>
 * <pre>{@code
 * {
 *   "message": "Sesión cerrada exitosamente"
 * }
 * }</pre>
 *
 * @param message Mensaje descriptivo de la operación realizada
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record MessageResponse(
      String message
) {
   // Factory methods para mensajes comunes

   public static MessageResponse success(String message) {
      return new MessageResponse(message);
   }

   public static MessageResponse logoutSuccess() {
      return new MessageResponse("Sesión cerrada exitosamente");
   }

   public static MessageResponse passwordResetEmailSent() {
      return new MessageResponse("Si el email existe, recibirás un enlace para restablecer tu contraseña");
   }

   public static MessageResponse passwordResetSuccess() {
      return new MessageResponse("Contraseña restablecida exitosamente");
   }
}