package com.winnersystems.smartparking.auth.domain.exception;

import com.winnersystems.smartparking.auth.domain.enums.TokenType;

/**
 * Excepción lanzada cuando un TOKEN ha EXPIRADO.
 * Aplica para todos los tipos de tokens:
 * - REFRESH_TOKEN (30 días)
 * - EMAIL_VERIFICATION (24 horas)
 * - PASSWORD_RESET (1 hora)
 *
 * Cuándo se lanza:
 * - Intentar usar un token después de su fecha de expiración
 * - Validar un token que ya no es válido por tiempo
 */
public class TokenExpiredException extends RuntimeException {

   private final TokenType tokenType;
   private final String token;

   /**
    * Constructor con tipo de token
    */
   public TokenExpiredException(TokenType tokenType) {
      super(String.format("El token de tipo '%s' ha expirado",
            tokenType.getDescription()));
      this.tokenType = tokenType;
      this.token = null;
   }

   /**
    * Constructor con mensaje genérico
    */
   public TokenExpiredException(String message) {
      super(message);
      this.tokenType = null;
      this.token = null;
   }

   /**
    * Constructor con tipo y token específico
    */
   public TokenExpiredException(TokenType tokenType, String token) {
      super(String.format("El token de tipo '%s' ha expirado",
            tokenType.getDescription()));
      this.tokenType = tokenType;
      this.token = token;
   }

   /**
    * Constructor completo con mensaje personalizado
    */
   public TokenExpiredException(TokenType tokenType, String token, String customMessage) {
      super(customMessage);
      this.tokenType = tokenType;
      this.token = token;
   }

   public TokenType getTokenType() {
      return tokenType;
   }

   public String getToken() {
      return token;
   }
}