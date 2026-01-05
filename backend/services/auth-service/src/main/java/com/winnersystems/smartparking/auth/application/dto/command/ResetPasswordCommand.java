package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Comando para restablecer contraseña con token.
 *
 * <p>Este es el paso 2 del flujo de recuperación de contraseña.
 * Este comando es parte de la capa de Application y NO contiene validaciones
 * de framework. Las validaciones se realizan en la capa de Infrastructure
 * mediante ResetPasswordRequest.java con Jakarta Validation.</p>
 *
 * <h3>Flujo completo de reset de contraseña</h3>
 * <ol>
 *   <li>Usuario ejecuta ForgotPasswordCommand (solicita reset)</li>
 *   <li>Usuario recibe email con link: https://app.com/reset?token=xyz</li>
 *   <li>Usuario hace clic en link e ingresa nueva contraseña</li>
 *   <li>Frontend envía ResetPasswordCommand (este comando)</li>
 *   <li>Backend valida token (no expirado, no usado)</li>
 *   <li>Backend actualiza contraseña hasheada con BCrypt</li>
 *   <li>Token se marca como usado (no puede reutilizarse)</li>
 *   <li>Todos los refresh tokens del usuario son revocados (forzar re-login)</li>
 *   <li>Email de confirmación enviado</li>
 * </ol>
 *
 * <h3>Validaciones de seguridad</h3>
 * <ul>
 *   <li>Token debe existir en BD (se busca comparando con BCrypt)</li>
 *   <li>Token NO debe estar expirado (1 hora)</li>
 *   <li>Token NO debe estar usado</li>
 *   <li>newPassword debe cumplir requisitos de complejidad</li>
 *   <li>confirmPassword debe coincidir con newPassword</li>
 *   <li>IP puede validarse opcionalmente contra la que solicitó reset</li>
 * </ul>
 *
 * @param token token recibido en el email (texto plano, no hasheado)
 * @param newPassword nueva contraseña (será hasheada con BCrypt)
 * @param confirmPassword confirmación de contraseña (doble validación)
 * @param ipAddress IP desde donde se resetea (auditoría/seguridad)
 * @param userAgent navegador/dispositivo User-Agent (auditoría)
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ResetPasswordCommand(
      String token,
      String newPassword,
      String confirmPassword,
      String ipAddress,
      String userAgent
) {
}