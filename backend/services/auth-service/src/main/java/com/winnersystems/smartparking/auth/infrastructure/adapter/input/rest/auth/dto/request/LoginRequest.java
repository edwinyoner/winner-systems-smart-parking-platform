package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO para autenticación de usuarios.
 *
 * <p>Usado en: POST /api/auth/login</p>
 *
 * <p><b>¿Por qué separar LoginRequest de LoginCommand?</b></p>
 * <ul>
 *   <li>LoginRequest: Contrato REST con validaciones HTTP</li>
 *   <li>LoginCommand: DTO de Application con IP, User-Agent, etc.</li>
 * </ul>
 *
 * <p>Esto permite evolucionar la API REST sin afectar Application Layer.</p>
 *
 * @param email Email del usuario (requerido)
 * @param password Contraseña del usuario (requerida)
 * @param captchaToken Token de reCAPTCHA v3 (opcional si está deshabilitado)
 * @param rememberMe Si true, refresh token dura 30 días; si false, 7 días
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record LoginRequest(
      @NotBlank(message = "Email es requerido")
      @Email(message = "Email debe ser válido")
      String email,

      @NotBlank(message = "Password es requerido")
      String password,

      String selectedRole,

      String captchaToken,  // Opcional

      boolean rememberMe    // Default false
) {
}