package com.winnersystems.smartparking.parking.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Configuración general de la aplicación Parking Service.
 *
 * Configura:
 * - TimeZone por defecto (América/Lima - Perú)
 * - Clock para manejo consistente de tiempo
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Configuration
public class AppConfig {

   private static final String PERU_TIMEZONE = "America/Lima";

   /**
    * Configura el TimeZone por defecto para toda la aplicación.
    *
    * Perú está en zona horaria America/Lima (UTC-5).
    * Esto afecta a todas las operaciones de fecha/hora en la aplicación.
    */
   @Bean
   public TimeZone timeZone() {
      TimeZone peruTimeZone = TimeZone.getTimeZone(PERU_TIMEZONE);
      TimeZone.setDefault(peruTimeZone);
      return peruTimeZone;
   }

   /**
    * Bean de Clock para manejo consistente de tiempo.
    *
    * Beneficios:
    * - Toda la aplicación usa el mismo reloj
    * - Facilita el testing (se puede mockear)
    * - Asegura que LocalDateTime.now() use hora peruana
    */
   @Bean
   public Clock clock() {
      return Clock.system(ZoneId.of(PERU_TIMEZONE));
   }
}