package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Comando para autenticación de usuarios internos.
 *
 * <p>Encapsula los datos necesarios para ejecutar el login.
 * Este comando es parte de la capa de Application y NO contiene validaciones
 * de framework. Las validaciones se realizan en la capa de Infrastructure
 * mediante LoginRequest.java con Jakarta Validation.</p>
 *
 * <h3>Flujo de Login</h3>
 * <ol>
 *   <li>Usuario ingresa email + password en frontend</li>
 *   <li>Frontend resuelve reCAPTCHA v3</li>
 *   <li>Backend valida captcha (score mínimo 0.5)</li>
 *   <li>Backend valida credenciales con BCrypt</li>
 *   <li>Backend genera access token (JWT) + refresh token</li>
 *   <li>Backend actualiza lastLoginAt</li>
 *   <li>Backend retorna tokens + información del usuario</li>
 * </ol>
 *
 * @param email email del usuario
 * @param password contraseña (será verificada con BCrypt)
 * @param captchaToken token de reCAPTCHA v3
 * @param rememberMe true=sesión extendida (30 días), false=sesión normal (7 días)
 * @param deviceInfo User-Agent del navegador/dispositivo (auditoría)
 * @param ipAddress IP del cliente (auditoría)
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record LoginCommand(
      String email,
      String password,
      String selectedRole,
      String captchaToken,
      boolean rememberMe,
      String deviceInfo,
      String ipAddress
) {
}