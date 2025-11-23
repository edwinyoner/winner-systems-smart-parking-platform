package com.winnersystems.smartparking.auth.application.port.input.auth;

/**
 * Caso de uso: CERRAR SESIÓN
 */
public interface LogoutUseCase {

   /**
    * Cierra la sesión del usuario revocando sus tokens
    *
    * Flujo:
    * 1. Buscar refresh token del usuario
    * 2. Revocar el refresh token
    * 3. (Opcional) Agregar access token a blacklist
    *
    * @param userId ID del usuario que hace logout
    */
   void execute(Long userId);

   /**
    * Cierra sesión revocando un refresh token específico
    *
    * @param refreshToken token a revocar
    */
   void executeWithToken(String refreshToken);
}