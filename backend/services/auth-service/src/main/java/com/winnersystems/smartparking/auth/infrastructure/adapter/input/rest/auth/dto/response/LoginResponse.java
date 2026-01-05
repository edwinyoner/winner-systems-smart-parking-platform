package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response;

/**
 * Response DTO para autenticación exitosa.
 *
 * <p>Usado en:</p>
 * <ul>
 *   <li>POST /api/auth/login</li>
 *   <li>POST /api/auth/refresh</li>
 * </ul>
 *
 * <p><b>Ejemplo JSON:</b></p>
 * <pre>{@code
 * {
 *   "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "refreshToken": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
 *   "tokenType": "Bearer",
 *   "expiresIn": 1800,
 *   "user": {
 *     "id": 1,
 *     "firstName": "Edwin",
 *     "lastName": "Yoner",
 *     "fullName": "Edwin Yoner",
 *     "email": "edwin@winner-systems.com",
 *     "profilePicture": null,
 *     "status": "ACTIVE"
 *   }
 * }
 * }</pre>
 *
 * @param accessToken JWT para autenticar requests (válido 30 minutos)
 * @param refreshToken UUID para renovar access token (válido 7-30 días)
 * @param tokenType Tipo de token (siempre "Bearer")
 * @param expiresIn Tiempo de expiración del access token en segundos (1800 = 30 min)
 * @param user Información básica del usuario autenticado
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record LoginResponse(
      String accessToken,
      String refreshToken,
      String tokenType,      // "Bearer"
      long expiresIn,        // 1800 segundos = 30 minutos
      UserInfo user
) {
   /**
    * Información básica del usuario autenticado.
    *
    * <p>Contiene solo datos necesarios para el frontend:</p>
    * <ul>
    *   <li>Identificación: id, email</li>
    *   <li>Información personal: nombre completo, foto</li>
    *   <li>Estado: activo/inactivo</li>
    * </ul>
    *
    * @param id ID único del usuario
    * @param firstName Nombre
    * @param lastName Apellido
    * @param fullName Nombre completo (firstName + lastName)
    * @param email Email del usuario
    * @param profilePicture URL de foto de perfil (puede ser null)
    * @param status Estado del usuario: "ACTIVE" o "INACTIVE"
    */
   public record UserInfo(
         Long id,
         String firstName,
         String lastName,
         String fullName,
         String email,
         String profilePicture,
         String status
   ) {
   }
}