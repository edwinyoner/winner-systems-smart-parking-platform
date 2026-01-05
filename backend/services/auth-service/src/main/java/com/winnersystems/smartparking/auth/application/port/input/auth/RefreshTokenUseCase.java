package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;

/**
 * Caso de uso: Refrescar token de acceso.
 *
 * <p>Cuando el access token expira (15-30 minutos), el cliente usa el
 * refresh token para obtener un nuevo access token SIN volver a hacer login.</p>
 *
 * <h3>Autorización</h3>
 * <ul>
 *   <li>RefreshToken debe ser válido (no expirado, no revocado)</li>
 *   <li>No requiere access token válido (ese es el punto)</li>
 * </ul>
 *
 * <h3>Funcionalidades</h3>
 * <ul>
 *   <li>Renovación de access token sin re-autenticación</li>
 *   <li>Mantiene sesión activa del usuario</li>
 *   <li>Valida estado del refresh token (expiración, revocación)</li>
 *   <li>No rota refresh token (se mantiene el mismo)</li>
 * </ul>
 *
 * <h3>Proceso ejecutado</h3>
 * <ol>
 *   <li>Buscar refresh token en BD (comparar con BCrypt)</li>
 *   <li>Validar que NO esté revocado (revoked = false)</li>
 *   <li>Validar que NO haya expirado (expiresAt > now)</li>
 *   <li>Obtener usuario asociado al token</li>
 *   <li>Verificar que usuario esté activo y email verificado</li>
 *   <li>Generar nuevo access token (JWT con 30 min validez)</li>
 *   <li>Retornar nuevo accessToken + mismo refreshToken</li>
 * </ol>
 *
 * <h3>Nota sobre Refresh Token Rotation</h3>
 * <p>Esta implementación NO rota el refresh token (no genera uno nuevo).
 * El mismo refresh token se puede usar múltiples veces hasta su expiración
 * o revocación manual. Para mayor seguridad, considerar implementar rotation
 * en versiones futuras.</p>
 *
 * @throws TokenExpiredException si el refresh token expiró
 * @throws IllegalStateException si el token está revocado
 * @throws UserNotFoundException si el usuario asociado no existe
 * @throws IllegalStateException si usuario inactivo o email no verificado
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RefreshTokenUseCase {
   /**
    * Renueva el access token usando un refresh token válido.
    *
    * @param refreshToken refresh token actual (texto plano)
    * @return respuesta con nuevo accessToken y mismo refreshToken
    */
   AuthResponseDto executeRefresh(String refreshToken);
}