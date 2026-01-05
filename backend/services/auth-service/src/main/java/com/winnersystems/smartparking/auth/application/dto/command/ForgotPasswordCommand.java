package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Comando para solicitar restablecimiento de contraseña.
 *
 * <p>Este comando es parte de la capa de Application y NO contiene validaciones
 * de framework. Las validaciones se realizan en la capa de Infrastructure
 * mediante ForgotPasswordRequest.java con Jakarta Validation.</p>
 *
 * <h3>Flujo de recuperación de contraseña</h3>
 * <ol>
 *   <li>Usuario hace clic en "Olvidé mi contraseña"</li>
 *   <li>Ingresa su email y resuelve reCAPTCHA v3</li>
 *   <li>Backend valida captcha (score mínimo 0.3 - más permisivo)</li>
 *   <li>Backend verifica que email exista (sin revelar si existe)</li>
 *   <li>Backend genera PasswordResetToken (válido 1 hora, hasheado)</li>
 *   <li>Backend envía email con link: https://app.com/reset?token=xyz</li>
 *   <li>Usuario hace clic en link e ingresa nueva contraseña</li>
 * </ol>
 *
 * <h3>Características de seguridad</h3>
 * <ul>
 *   <li>Token expira en 1 hora (ventana corta)</li>
 *   <li>Token hasheado con BCrypt en BD (no reversible)</li>
 *   <li>IP y User-Agent guardados para auditoría</li>
 *   <li>Token de un solo uso (se marca como usado)</li>
 *   <li>Rate limiting: máximo 3 solicitudes por hora por email</li>
 *   <li>Respuesta genérica (no revela si email existe)</li>
 * </ul>
 *
 * @param email email del usuario
 * @param captchaToken token de reCAPTCHA v3 (anti-spam)
 * @param ipAddress IP desde donde se solicita (auditoría)
 * @param userAgent navegador/dispositivo User-Agent (auditoría)
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ForgotPasswordCommand(
      String email,
      String captchaToken,
      String ipAddress,
      String userAgent
) {
}