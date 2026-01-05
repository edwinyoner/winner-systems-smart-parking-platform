package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.application.dto.query.CaptchaValidationResult;

/**
 * Puerto de salida para validación de reCAPTCHA de Google.
 *
 * <p>Valida tokens de reCAPTCHA v2 o v3 contra la API de Google.
 * Retorna resultado completo con score, action, errores, etc.</p>
 *
 * <h3>Configuración típica (application.yml)</h3>
 * <pre>
 * recaptcha:
 *   enabled: true
 *   version: v3
 *   site-key: 6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI
 *   secret-key: 6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe
 *   verify-url: https://www.google.com/recaptcha/api/siteverify
 * </pre>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CaptchaPort {

   /**
    * Valida un token de reCAPTCHA contra la API de Google.
    *
    * <p>Envía el token a la API de Google y retorna el resultado completo
    * con success, score (v3), action (v3), errores, etc.</p>
    *
    * @param captchaToken token obtenido del frontend (grecaptcha.execute o getResponse)
    * @param remoteIp IP del cliente (opcional pero recomendado)
    * @return resultado completo de la validación
    * @throws CaptchaServiceException si la API de Google no responde
    */
   CaptchaValidationResult validate(String captchaToken, String remoteIp);

   /**
    * Verifica si reCAPTCHA está habilitado.
    *
    * @return true si está habilitado, false si no
    */
   boolean isEnabled();
}