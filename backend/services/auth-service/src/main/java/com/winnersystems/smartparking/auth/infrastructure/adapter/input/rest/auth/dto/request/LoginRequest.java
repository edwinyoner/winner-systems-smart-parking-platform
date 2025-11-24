package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO REST para el request de login.
 *
 * ¿Por qué otro DTO si ya existe LoginCommand?
 * - LoginRequest: Específico para REST API (validaciones HTTP)
 * - LoginCommand: Específico para Application (lógica de negocio)
 *
 * Esto permite que la API REST cambie sin afectar Application.
 */
public record LoginRequest(
      @NotBlank(message = "Email es requerido")
      @Email(message = "Email debe ser válido")
      String email,

      @NotBlank(message = "Password es requerido")
      String password,

      String captchaToken,

      boolean rememberMe
) {
}