package com.winnersystems.smartparking.parking.domain.exception;

/**
 * Excepción base para todas las excepciones de dominio del módulo de Parking.
 *
 * Todas las excepciones específicas del dominio de estacionamiento deben heredar
 * de esta clase para facilitar el manejo centralizado de errores.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class ParkingDomainException extends RuntimeException {

   public ParkingDomainException(String message) {
      super(message);
   }

   public ParkingDomainException(String message, Throwable cause) {
      super(message, cause);
   }
}