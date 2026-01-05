package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO para renovar el access token.
 *
 * <p>Usado en:</p>
 * <ul>
 *   <li>POST /api/auth/refresh</li>
 *   <li>POST /api/auth/logout</li>
 * </ul>
 *
 * <p><b>Flujo:</b></p>
 * <ol>
 *   <li>Access token expira (30 minutos)</li>
 *   <li>Frontend envía refresh token</li>
 *   <li>Backend valida y genera nuevo access token</li>
 *   <li>Refresh token NO se rota (se reutiliza)</li>
 * </ol>
 *
 * @param refreshToken UUID v4 del refresh token (requerido)
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record RefreshTokenRequest(
      @NotBlank(message = "Refresh token es requerido")
      String refreshToken  // UUID v4: "f47ac10b-58cc-4372-a567-0e02b2c3d479"
) {
}