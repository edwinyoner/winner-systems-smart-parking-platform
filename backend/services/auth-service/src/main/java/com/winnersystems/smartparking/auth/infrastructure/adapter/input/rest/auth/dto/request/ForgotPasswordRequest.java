package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO para solicitar restablecimiento de contraseña.
 *
 * <p>Usado en: POST /api/auth/forgot-password</p>
 *
 * <p><b>Flujo:</b></p>
 * <ol>
 *   <li>Usuario ingresa su email</li>
 *   <li>Sistema valida captcha</li>
 *   <li>Si email existe, envía link de reset</li>
 *   <li>Si no existe, NO revela información (seguridad)</li>
 * </ol>
 *
 * @param email Email del usuario (requerido)
 * @param captchaToken Token de reCAPTCHA v3 (opcional si está deshabilitado)
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ForgotPasswordRequest(
      @NotBlank(message = "Email es requerido")
      @Email(message = "Email debe ser válido")
      String email,

      String captchaToken  // Opcional (si captcha está deshabilitado)
) {
}