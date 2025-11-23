package com.winnersystems.smartparking.auth.application.port.output;

/**
 * Puerto de salida para encriptación de contraseñas.
 * La implementación típicamente usa BCrypt.
 */
public interface PasswordEncoderPort {

   /**
    * Encripta una contraseña en texto plano
    * @param rawPassword contraseña sin encriptar
    * @return contraseña encriptada (hash)
    */
   String encode(String rawPassword);

   /**
    * Verifica si una contraseña coincide con su hash
    * @param rawPassword contraseña sin encriptar
    * @param encodedPassword contraseña encriptada (hash)
    * @return true si coinciden, false si no
    */
   boolean matches(String rawPassword, String encodedPassword);
}