package com.winnersystems.smartparking.auth.application.port.input.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;

/**
 * Caso de uso: Solicitar restablecimiento de contraseña.
 *
 * <p>Permite a un usuario que olvidó su contraseña solicitar un token
 * de reset que será enviado a su email.</p>
 *
 * <h3>Autorización</h3>
 * <p>Este endpoint es público (no requiere autenticación).</p>
 *
 * <h3>Funcionalidades</h3>
 * <ul>
 *   <li>Validación anti-bot con reCAPTCHA v3 (score mínimo 0.3)</li>
 *   <li>Rate limiting por email (máximo 3 solicitudes por hora)</li>
 *   <li>Generación de token seguro hasheado con BCrypt</li>
 *   <li>Token válido por 1 hora</li>
 *   <li>Email con link de reset personalizado</li>
 *   <li>Tracking de IP y dispositivo para auditoría</li>
 * </ul>
 *
 * <h3>Proceso ejecutado</h3>
 * <ol>
 *   <li>Validar captcha (score mínimo 0.3 - más permisivo que login)</li>
 *   <li>Buscar usuario por email (sin revelar si existe)</li>
 *   <li>Verificar rate limiting (máximo 3 solicitudes/hora)</li>
 *   <li>Generar token UUID (texto plano)</li>
 *   <li>Hashear token con BCrypt</li>
 *   <li>Guardar token hasheado en BD con expiración de 1 hora</li>
 *   <li>Revocar tokens de reset previos del usuario</li>
 *   <li>Enviar email con link: https://app.com/reset?token={UUID}</li>
 *   <li>Retornar mensaje genérico de éxito (siempre)</li>
 * </ol>
 *
 * <h3>Seguridad crítica</h3>
 * <ul>
 *   <li><b>Respuesta genérica:</b> Siempre retornar mensaje de éxito, incluso si
 *       el email no existe. Esto previene user enumeration.</li>
 *   <li><b>Token hasheado:</b> Se guarda con BCrypt en BD (no reversible)</li>
 *   <li><b>Rate limiting:</b> Máximo 3 solicitudes por hora por email</li>
 *   <li><b>Expiración corta:</b> Token válido solo 1 hora</li>
 *   <li><b>Un solo uso:</b> Token se marca como usado al resetear password</li>
 *   <li><b>Revocación:</b> Tokens previos se revocan al generar uno nuevo</li>
 * </ul>
 *
 * @throws InvalidCaptchaException si captcha inválido o score bajo
 * @throws IllegalStateException si se excede rate limit
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ForgotPasswordUseCase {
   /**
    * Genera token de reset y envía email al usuario.
    *
    * <p>Retorna void porque siempre responde con mensaje genérico,
    * sin revelar si el email existe o no.</p>
    *
    * @param command datos de solicitud (email, captcha, tracking)
    */
   void execute(ForgotPasswordCommand command);
}