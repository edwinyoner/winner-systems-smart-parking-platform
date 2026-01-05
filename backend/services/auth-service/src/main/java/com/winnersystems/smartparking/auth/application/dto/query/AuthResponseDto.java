package com.winnersystems.smartparking.auth.application.dto.query;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para operaciones de autenticación.
 *
 * <p>Contiene los tokens JWT y la información completa del usuario autenticado.
 * Sigue el estándar OAuth2 Bearer Token.</p>
 *
 * <h3>Flujo de uso</h3>
 * <ol>
 *   <li>Usuario hace login → Recibe accessToken + refreshToken</li>
 *   <li>Frontend almacena tokens (httpOnly cookies recomendado)</li>
 *   <li>Usa accessToken en header: Authorization: Bearer {accessToken}</li>
 *   <li>Cuando accessToken expira → Usa refreshToken para renovar</li>
 *   <li>Cuando refreshToken expira → Usuario debe reloguear</li>
 * </ol>
 *
 * <h3>Seguridad</h3>
 * <ul>
 *   <li><b>accessToken:</b> Duración corta (15-30 min), se envía en cada petición</li>
 *   <li><b>refreshToken:</b> Duración larga (14 días), solo para endpoint /refresh</li>
 *   <li><b>Almacenamiento:</b> httpOnly cookies (más seguro) o localStorage cifrado</li>
 * </ul>
 *
 * @param accessToken JWT token (validez corta)
 * @param refreshToken token para renovar accessToken (validez larga)
 * @param tokenType tipo de token ("Bearer" según OAuth2)
 * @param expiresIn segundos hasta expiración del accessToken
 * @param refreshExpiresIn segundos hasta expiración del refreshToken
 * @param issuedAt timestamp de emisión de los tokens
 * @param user información del usuario autenticado
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record AuthResponseDto(
      String accessToken,
      String refreshToken,
      String tokenType,
      long expiresIn,
      long refreshExpiresIn,
      LocalDateTime issuedAt,
      UserDto user
) {
   /**
    * Crea una respuesta de autenticación con valores estándar.
    *
    * <p>Usa "Bearer" como tokenType por defecto e issuedAt = now().</p>
    *
    * @param accessToken JWT token
    * @param refreshToken token de renovación
    * @param expiresIn segundos hasta expiración del accessToken
    * @param refreshExpiresIn segundos hasta expiración del refreshToken
    * @param user información del usuario
    * @return respuesta de autenticación
    */
   public static AuthResponseDto of(String accessToken, String refreshToken,
                                    long expiresIn, long refreshExpiresIn, UserDto user) {
      return new AuthResponseDto(
            accessToken,
            refreshToken,
            "Bearer",
            expiresIn,
            refreshExpiresIn,
            LocalDateTime.now(),
            user
      );
   }

   /**
    * Crea una respuesta de autenticación con refreshExpiresIn estándar.
    *
    * <p>Usa 14 días (336 horas = 1,209,600 segundos) como tiempo de
    * expiración del refreshToken por defecto.</p>
    *
    * @param accessToken JWT token
    * @param refreshToken token de renovación
    * @param expiresIn segundos hasta expiración del accessToken
    * @param user información del usuario
    * @return respuesta de autenticación
    */
   public static AuthResponseDto of(String accessToken, String refreshToken,
                                    long expiresIn, UserDto user) {
      return new AuthResponseDto(
            accessToken,
            refreshToken,
            "Bearer",
            expiresIn,
            336 * 60 * 60,  // 14 días en segundos
            LocalDateTime.now(),
            user
      );
   }
}