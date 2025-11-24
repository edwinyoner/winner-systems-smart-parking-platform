package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO REST para refrescar el access token.
 */
public record RefreshTokenRequest(
      @NotBlank(message = "Refresh token es requerido")
      String refreshToken
) {
}