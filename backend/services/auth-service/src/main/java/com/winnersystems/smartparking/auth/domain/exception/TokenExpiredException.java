package com.winnersystems.smartparking.auth.domain.exception;

/**
 * Excepción lanzada cuando un TOKEN ha EXPIRADO.
 *
 * <p>Aplica para todos los tipos de tokens:</p>
 * <ul>
 *   <li>RefreshToken: 14 días (336 horas)</li>
 *   <li>VerificationToken: 24 horas</li>
 *   <li>PasswordResetToken: 1 hora</li>
 * </ul>
 *
 * <p><b>Cuándo se lanza:</b></p>
 * <ul>
 *   <li>Intentar usar un token después de su fecha de expiración</li>
 *   <li>Validar un token que ya no es válido por tiempo</li>
 * </ul>
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class TokenExpiredException extends RuntimeException {

   private final String tokenType;                       // "RefreshToken", "PasswordResetToken", etc.
   private final String token;                           // Token específico (opcional)

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor simple con mensaje genérico.
    * Usar cuando no necesitas especificar tipo de token.
    *
    * @param message mensaje de error
    */
   public TokenExpiredException(String message) {
      super(message);
      this.tokenType = null;
      this.token = null;
   }

   /**
    * Constructor con tipo de token y mensaje automático.
    *
    * @param tokenType tipo de token ("RefreshToken", "PasswordResetToken", "VerificationToken")
    * @param isTypeSpecified flag para diferenciar de constructor genérico (siempre pasar true)
    */
   public TokenExpiredException(String tokenType, boolean isTypeSpecified) {
      super(String.format("El token de tipo '%s' ha expirado", tokenType));
      this.tokenType = tokenType;
      this.token = null;
   }

   /**
    * Constructor con tipo y token específico.
    *
    * @param tokenType tipo de token
    * @param token el token que expiró (parcial, últimos caracteres)
    */
   public TokenExpiredException(String tokenType, String token) {
      super(String.format("El token de tipo '%s' ha expirado", tokenType));
      this.tokenType = tokenType;
      this.token = token;
   }

   /**
    * Constructor completo con mensaje personalizado.
    *
    * @param tokenType tipo de token
    * @param token el token que expiró
    * @param customMessage mensaje personalizado
    */
   public TokenExpiredException(String tokenType, String token, String customMessage) {
      super(customMessage);
      this.tokenType = tokenType;
      this.token = token;
   }

   // ========================= GETTERS =========================

   /**
    * Obtiene el tipo de token que expiró.
    *
    * @return tipo de token ("RefreshToken", "PasswordResetToken", etc.)
    */
   public String getTokenType() {
      return tokenType;
   }

   /**
    * Obtiene el token que expiró (si está disponible).
    *
    * @return token (parcial o completo)
    */
   public String getToken() {
      return token;
   }
}