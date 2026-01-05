package com.winnersystems.smartparking.auth.application.port.input.auth;

/**
 * Caso de uso: Cerrar sesión.
 *
 * <p>Revoca el refresh token específico del dispositivo actual,
 * invalidando la sesión del usuario.</p>
 *
 * <h3>Autorización</h3>
 * <ul>
 *   <li>Usuario debe estar autenticado (accessToken válido)</li>
 *   <li>RefreshToken debe pertenecer al usuario autenticado</li>
 * </ul>
 *
 * <h3>Funcionalidades</h3>
 * <ul>
 *   <li>Revocación de refresh token específico</li>
 *   <li>Soporte multi-device (solo cierra sesión en dispositivo actual)</li>
 *   <li>Access token sigue válido hasta expiración natural</li>
 *   <li>Auditoría de cierre de sesión</li>
 * </ul>
 *
 * <h3>Proceso ejecutado</h3>
 * <ol>
 *   <li>Buscar refresh token en BD (comparar con BCrypt)</li>
 *   <li>Validar que pertenece al usuario autenticado</li>
 *   <li>Marcar como revocado (revoked = true, revokedAt = now())</li>
 *   <li>Usuario no podrá renovar su access token con ese refresh token</li>
 * </ol>
 *
 * <h3>Nota sobre Access Token</h3>
 * <p>El access token (JWT) sigue siendo válido hasta su expiración natural
 * (15-30 minutos). Si se requiere invalidación inmediata, implementar
 * una blacklist de tokens en Redis.</p>
 *
 * @throws IllegalArgumentException si refreshToken es inválido
 * @throws IllegalStateException si token ya está revocado
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface LogoutUseCase {
   /**
    * Cierra la sesión del usuario revocando su refresh token.
    *
    * @param refreshToken token a revocar (texto plano del frontend)
    */
   void executeLogout(String refreshToken);
}