package com.winnersystems.smartparking.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Clase principal del microservicio auth-service.
 *
 * Microservicio de autenticación y autorización para Smart Parking.
 * Implementa arquitectura hexagonal con puertos y adaptadores.
 *
 * @author Edwin Yoner - Winner Systems Corporation S.A.C.
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

   public static void main(String[] args) {
      SpringApplication.run(AuthServiceApplication.class, args);
   }
}