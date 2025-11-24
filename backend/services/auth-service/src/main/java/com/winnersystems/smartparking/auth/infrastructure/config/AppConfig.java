package com.winnersystems.smartparking.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración general de la aplicación.
 * Define beans compartidos.
 */
@Configuration
public class AppConfig {

   /**
    * Bean de RestTemplate para hacer peticiones HTTP
    * Usado por RecaptchaAdapter
    */
   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplate();
   }
}