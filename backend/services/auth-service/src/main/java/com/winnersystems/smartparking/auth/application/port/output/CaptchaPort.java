package com.winnersystems.smartparking.auth.application.port.output;

/**
 * Puerto de salida para validación de CAPTCHA (reCAPTCHA de Google).
 * Previene bots y automatización maliciosa.
 */
public interface CaptchaPort {

   /**
    * Valida un token de reCAPTCHA
    * @param captchaToken token obtenido del frontend
    * @param remoteIp IP del cliente (opcional)
    * @return true si es válido, false si no
    */
   boolean validateCaptcha(String captchaToken, String remoteIp);

   /**
    * Valida reCAPTCHA v3 con score
    * @param captchaToken token obtenido del frontend
    * @param remoteIp IP del cliente
    * @param minScore score mínimo aceptable (0.0 a 1.0)
    * @return true si score >= minScore, false si no
    */
   boolean validateCaptchaWithScore(String captchaToken, String remoteIp, double minScore);
}