package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response;

/**
 * DTO REST para respuestas simples con mensaje.
 * Usado para operaciones que solo necesitan confirmar éxito.
 */
public record MessageResponse(
      String message
) {
}