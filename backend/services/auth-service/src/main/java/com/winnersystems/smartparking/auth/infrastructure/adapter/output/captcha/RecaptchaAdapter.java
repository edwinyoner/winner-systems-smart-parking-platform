package com.winnersystems.smartparking.auth.infrastructure.adapter.output.captcha;

import com.winnersystems.smartparking.auth.application.dto.query.CaptchaValidationResult;
import com.winnersystems.smartparking.auth.application.port.output.CaptchaPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Adaptador para validación de Google reCAPTCHA v3.
 * Implementa CaptchaPort.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class RecaptchaAdapter implements CaptchaPort {

   private final RestTemplate restTemplate;

   @Value("${recaptcha.secret-key:}")
   private String secretKey;

   @Value("${recaptcha.verify-url:https://www.google.com/recaptcha/api/siteverify}")
   private String verifyUrl;

   @Value("${recaptcha.enabled:false}")
   private boolean enabled;

   public RecaptchaAdapter(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
   }

   @Override
   public CaptchaValidationResult validate(String captchaToken, String ipAddress) {

      // Si está deshabilitado (modo desarrollo) → devolver éxito por defecto
      if (!enabled || secretKey == null || secretKey.isEmpty()) {
         return new CaptchaValidationResult(
               true,          // success
               1.0,           // score
               null,          // action
               null,          // challengeTs
               "localhost",   // hostname
               new String[0]  // errorCodes
         );
      }

      // Si no se recibió token
      if (captchaToken == null || captchaToken.isEmpty()) {
         return new CaptchaValidationResult(
               false,                          // success
               0.0,                            // score
               null,                           // action
               null,                           // challengeTs
               ipAddress,                      // hostname
               new String[]{"missing-input-response"} // errorCodes
         );
      }

      try {
         // Parámetros de petición
         MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
         params.add("secret", secretKey);
         params.add("response", captchaToken);

         if (ipAddress != null && !ipAddress.isEmpty()) {
            params.add("remoteip", ipAddress);
         }

         // Llamada a la API de Google
         ResponseEntity<Map> response = restTemplate.postForEntity(
               verifyUrl,
               params,
               Map.class
         );

         if (response.getBody() == null) {
            return new CaptchaValidationResult(
                  false,
                  0.0,
                  null,
                  null,
                  ipAddress,
                  new String[]{"invalid-response"}
            );
         }

         Map<String, Object> body = response.getBody();

         Boolean success = (Boolean) body.get("success");

         double score = body.get("score") != null
               ? ((Number) body.get("score")).doubleValue()
               : 0.0;

         String action = (String) body.get("action");
         String hostname = (String) body.get("hostname");

         // Convertir error-codes a String[]
         List<?> errorList = (List<?>) body.get("error-codes");
         String[] errorCodes = (errorList != null)
               ? errorList.stream().map(Object::toString).toArray(String[]::new)
               : new String[0];

         return new CaptchaValidationResult(
               Boolean.TRUE.equals(success),
               score,
               action,
               null,         // challengeTs (Google v3 no envía timestamp)
               hostname,
               errorCodes
         );

      } catch (Exception e) {

         System.err.println("Error validando reCAPTCHA: " + e.getMessage());

         return new CaptchaValidationResult(
               false,
               0.0,
               null,
               null,
               ipAddress,
               new String[]{"connection-error"}
         );
      }
   }

   @Override
   public boolean isEnabled() {
      return enabled && secretKey != null && !secretKey.isEmpty();
   }
}