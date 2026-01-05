package com.winnersystems.smartparking.auth.application.dto.query;

import java.time.LocalDateTime;

/**
 * Resultado de la validación de reCAPTCHA.
 *
 * <p>Contiene la respuesta de la API de Google reCAPTCHA v2 o v3.
 *
 * @param success true si la validación fue exitosa
 * @param score score de reCAPTCHA v3 (0.0-1.0, 0.0 si es v2)
 * @param action action esperado en reCAPTCHA v3 (null si es v2)
 * @param challengeTs timestamp del challenge
 * @param hostname hostname donde se generó el token
 * @param errorCodes códigos de error si falló la validación
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record CaptchaValidationResult(
      boolean success,
      double score,
      String action,
      LocalDateTime challengeTs,
      String hostname,
      String[] errorCodes
) {
   /**
    * Crea un resultado exitoso para reCAPTCHA v2.
    *
    * <p>reCAPTCHA v2 no usa score ni action, por lo que
    * estos campos se inicializan en 0.0 y null respectivamente.</p>
    *
    * @param success resultado de la validación
    * @param challengeTs timestamp del challenge
    * @param hostname hostname donde se generó
    * @return resultado de reCAPTCHA v2
    */
   public static CaptchaValidationResult forV2(boolean success, LocalDateTime challengeTs, String hostname) {
      return new CaptchaValidationResult(success, 0.0, null, challengeTs, hostname, new String[0]);
   }

   /**
    * Crea un resultado exitoso para reCAPTCHA v3.
    *
    * <p>reCAPTCHA v3 usa score (0.0-1.0) para determinar si es bot.
    * Scores más altos indican mayor probabilidad de ser humano.</p>
    *
    * @param success resultado de la validación
    * @param score score de 0.0 a 1.0 (1.0 = muy probablemente humano)
    * @param action action esperado (ej: "login", "register")
    * @param challengeTs timestamp del challenge
    * @param hostname hostname donde se generó
    * @return resultado de reCAPTCHA v3
    */
   public static CaptchaValidationResult forV3(boolean success, double score, String action,
                                               LocalDateTime challengeTs, String hostname) {
      return new CaptchaValidationResult(success, score, action, challengeTs, hostname, new String[0]);
   }

   /**
    * Crea un resultado fallido con códigos de error.
    *
    * <p>Usado cuando la API de Google reCAPTCHA retorna errores
    * (token inválido, timeout, etc.).</p>
    *
    * @param errorCodes códigos de error de Google
    * @return resultado fallido
    */
   public static CaptchaValidationResult withErrors(String[] errorCodes) {
      return new CaptchaValidationResult(false, 0.0, null, null, null, errorCodes);
   }
}