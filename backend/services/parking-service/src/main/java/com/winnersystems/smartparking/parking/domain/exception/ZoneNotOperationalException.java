package com.winnersystems.smartparking.parking.domain.exception;

import java.time.LocalTime;

/**
 * Excepción lanzada cuando se intenta operar en una zona que no está operativa
 * en el horario actual.
 *
 * Escenarios:
 * - Zona tiene horario 08:00-18:00 y son las 20:00
 * - Zona está cerrada por evento especial
 * - Zona deshabilitada por mantenimiento
 * - Zona marcada como inactiva (status = false)
 *
 * Esta excepción protege operaciones fuera de horario.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class ZoneNotOperationalException extends ParkingDomainException {

   private final String zoneName;
   private final String reason;

   public ZoneNotOperationalException(String zoneName, String reason) {
      super(String.format(
            "La zona '%s' no está operativa. Razón: %s",
            zoneName, reason
      ));
      this.zoneName = zoneName;
      this.reason = reason;
   }

   public ZoneNotOperationalException(String zoneName,
                                      LocalTime currentTime,
                                      LocalTime openingTime,
                                      LocalTime closingTime) {
      super(String.format(
            "La zona '%s' no está operativa a las %s. " +
                  "Horario de operación: %s - %s",
            zoneName, currentTime, openingTime, closingTime
      ));
      this.zoneName = zoneName;
      this.reason = String.format("Fuera de horario (%s - %s)", openingTime, closingTime);
   }

   public String getZoneName() {
      return zoneName;
   }

   public String getReason() {
      return reason;
   }
}