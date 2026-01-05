package com.winnersystems.smartparking.auth.application.port.output;

/**
 * Puerto de salida para hashing de contraseñas.
 *
 * <h3>Implementación: BCrypt</h3>
 * <p>BCrypt es el estándar de facto para hashear contraseñas porque:</p>
 * <ul>
 *   <li>Genera salt automáticamente (único por cada hash)</li>
 *   <li>Es adaptativo: el cost factor puede aumentarse con el tiempo</li>
 *   <li>Resistente a ataques rainbow table y fuerza bruta</li>
 *   <li>No reversible: no se puede obtener la contraseña original</li>
 * </ul>
 *
 * <h3>Formato del hash BCrypt</h3>
 * <pre>
 * $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
 * |  | |  |                                           |
 * |  | |  |                                           +- Hash (31 chars)
 * |  | |  +- Salt (22 chars)
 * |  | +- Cost factor (2^10 = 1024 rounds)
 * |  +- Minor version
 * +- BCrypt identifier
 * </pre>
 *
 * <h3>Uso típico</h3>
 * <pre>
 * // Hashear al crear/cambiar contraseña
 * String hash = passwordEncoderPort.encode("myPassword123");
 * user.setPassword(hash);
 *
 * // Verificar al hacer login
 * if (passwordEncoderPort.matches("myPassword123", user.getPassword())) {
 *     // Contraseña correcta
 * }
 * </pre>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface PasswordEncoderPort {

   /**
    * Hashea una contraseña en texto plano usando BCrypt.
    *
    * <p>El hash generado incluye automáticamente:</p>
    * <ul>
    *   <li>Salt aleatorio único (previene rainbow tables)</li>
    *   <li>Cost factor configurado (típicamente 10-12 rounds)</li>
    *   <li>Identificador del algoritmo ($2a, $2b)</li>
    * </ul>
    *
    * <p><b>Importante:</b></p>
    * <ul>
    *   <li>Cada llamada genera un hash DIFERENTE (salt único)</li>
    *   <li>El proceso es intencionalmente LENTO (seguridad)</li>
    *   <li>NUNCA guardar la contraseña en texto plano</li>
    * </ul>
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>CreateUserUseCase - Al crear usuario</li>
    *   <li>ResetPasswordUseCase - Al resetear password</li>
    *   <li>ChangePasswordUseCase - Al cambiar password</li>
    * </ul>
    *
    * @param rawPassword contraseña sin hashear (texto plano)
    * @return contraseña hasheada (hash BCrypt - ~60 caracteres)
    * @throws IllegalArgumentException si rawPassword es null o vacío
    */
   String encode(String rawPassword);

   /**
    * Verifica si una contraseña en texto plano coincide con su hash BCrypt.
    *
    * <p>Este método:</p>
    * <ul>
    *   <li>Extrae el salt del hash existente</li>
    *   <li>Aplica el mismo salt a la contraseña provista</li>
    *   <li>Compara los hashes resultantes</li>
    *   <li>Es resistente a timing attacks</li>
    * </ul>
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>LoginUseCase - Al validar credenciales</li>
    *   <li>ChangePasswordUseCase - Al validar password actual</li>
    * </ul>
    *
    * @param rawPassword contraseña sin hashear (del usuario)
    * @param encodedPassword contraseña hasheada (de la BD)
    * @return true si coinciden, false si no
    * @throws IllegalArgumentException si algún parámetro es null
    */
   boolean matches(String rawPassword, String encodedPassword);
}