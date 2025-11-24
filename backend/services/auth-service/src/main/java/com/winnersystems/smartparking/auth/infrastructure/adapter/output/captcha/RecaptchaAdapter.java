package com.winnersystems.smartparking.auth.infrastructure.adapter.output.captcha;

import com.winnersystems.smartparking.auth.application.port.output.CaptchaPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Adaptador para validación de Google reCAPTCHA.
 * Implementa CaptchaPort.
 */
@Component
public class RecaptchaAdapter implements CaptchaPort {

   private final RestTemplate restTemplate;

   @Value("${recaptcha.secret-key:}")
   private String secretKey;

   @Value("${recaptcha.verify-url:https://www.google.com/recaptcha/api/siteverify}")
   private String verifyUrl;

   @Value("${recaptcha.min-score:0.5}")
   private double minScore;

   public RecaptchaAdapter(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
   }

   @Override
   public boolean validateCaptcha(String captchaToken, String remoteIp) {
      // Si no hay secret key configurado, retornar true (para desarrollo)
      if (secretKey == null || secretKey.isEmpty()) {
         return true;
      }

      // Si no hay token, retornar false
      if (captchaToken == null || captchaToken.isEmpty()) {
         return false;
      }

      try {
         // Preparar parámetros para la petición a Google
         MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
         params.add("secret", secretKey);
         params.add("response", captchaToken);

         if (remoteIp != null && !remoteIp.isEmpty()) {
            params.add("remoteip", remoteIp);
         }

         // Hacer petición a Google reCAPTCHA API
         ResponseEntity<Map> response = restTemplate.postForEntity(
               verifyUrl,
               params,
               Map.class
         );

         if (response.getBody() == null) {
            return false;
         }

         // Verificar respuesta
         Map<String, Object> body = response.getBody();
         Boolean success = (Boolean) body.get("success");

         return Boolean.TRUE.equals(success);

      } catch (Exception e) {
         // En caso de error al validar, retornar false
         System.err.println("Error validando reCAPTCHA: " + e.getMessage());
         return false;
      }
   }

   @Override
   public boolean validateCaptchaWithScore(String captchaToken, String remoteIp, double minScoreParam) {
      // Si no hay secret key configurado, retornar true (para desarrollo)
      if (secretKey == null || secretKey.isEmpty()) {
         return true;
      }

      // Si no hay token, retornar false
      if (captchaToken == null || captchaToken.isEmpty()) {
         return false;
      }

      try {
         // Preparar parámetros
         MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
         params.add("secret", secretKey);
         params.add("response", captchaToken);

         if (remoteIp != null && !remoteIp.isEmpty()) {
            params.add("remoteip", remoteIp);
         }

         // Hacer petición a Google reCAPTCHA API
         ResponseEntity<Map> response = restTemplate.postForEntity(
               verifyUrl,
               params,
               Map.class
         );

         if (response.getBody() == null) {
            return false;
         }

         // Verificar respuesta y score (para reCAPTCHA v3)
         Map<String, Object> body = response.getBody();
         Boolean success = (Boolean) body.get("success");

         if (!Boolean.TRUE.equals(success)) {
            return false;
         }

         // Verificar score (solo para reCAPTCHA v3)
         Object scoreObj = body.get("score");
         if (scoreObj != null) {
            double score = ((Number) scoreObj).doubleValue();
            return score >= minScoreParam;
         }

         // Si no hay score (reCAPTCHA v2), solo verificar success
         return true;

      } catch (Exception e) {
         // En caso de error al validar, retornar false
         System.err.println("Error validando reCAPTCHA con score: " + e.getMessage());
         return false;
      }
   }
}