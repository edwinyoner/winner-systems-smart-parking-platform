package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;

/**
 * Caso de uso: REFRESCAR TOKEN
 *
 * Cuando el access token expira (15 min), el cliente usa el refresh token
 * para obtener un nuevo access token SIN volver a hacer login.
 */
public interface RefreshTokenUseCase {

   /**
    * Genera un nuevo access token usando un refresh token válido
    *
    * Flujo:
    * 1. Validar que el refresh token existe
    * 2. Validar que NO esté revocado
    * 3. Validar que NO haya expirado
    * 4. Obtener el usuario asociado
    * 5. Generar nuevo access token
    * 6. (Opcional) Generar nuevo refresh token (rotation)
    * 7. Retornar nueva respuesta de autenticación
    *
    * @param refreshToken refresh token actual
    * @return nueva respuesta con access token renovado
    * @throws TokenExpiredException si el refresh token expiró
    */
   AuthResponseDto execute(String refreshToken);
}