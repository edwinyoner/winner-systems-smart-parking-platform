package com.winnersystems.smartparking.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración CORS para Spring Cloud Gateway.
 * Se necesita configuración manual porque Gateway usa WebFlux (reactivo).
 */
@Configuration
public class CorsConfiguration {

   @Bean
   public CorsWebFilter corsWebFilter() {
      org.springframework.web.cors.CorsConfiguration corsConfig =
            new org.springframework.web.cors.CorsConfiguration();

      // Orígenes permitidos
      corsConfig.setAllowedOriginPatterns(List.of("http://localhost:4200"));

      // Métodos HTTP permitidos
      corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

      // Headers permitidos
      corsConfig.setAllowedHeaders(List.of("*"));

      // Permitir credenciales (cookies, auth headers)
      corsConfig.setAllowCredentials(true);

      // Tiempo de cache para preflight requests
      corsConfig.setMaxAge(3600L);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", corsConfig);

      return new CorsWebFilter(source);
   }
}