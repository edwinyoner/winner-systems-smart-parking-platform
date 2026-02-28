package com.winnersystems.smartparking.parking.domain.exception;

/**
 * Excepción CRÍTICA lanzada cuando el documento de salida NO coincide con el
 * documento de entrada.
 *
 * Esta es una medida de seguridad ANTI-ROBO. El sistema debe validar que la
 * persona que retira el vehículo es la misma que lo dejó.
 *
 * Escenario:
 * - Juan Pérez (DNI 43567890) dejó el vehículo ABC-123
 * - María López (DNI 98765432) intenta retirar ABC-123
 * - Sistema detecta que los documentos NO coinciden
 * - Se lanza esta excepción y se BLOQUEA la salida
 * - Se genera una alerta de seguridad
 *
 * Acciones del sistema:
 * - Notificar al supervisor
 * - Posiblemente crear Infraction
 * - Registrar intento en logs de seguridad
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class DocumentMismatchException extends ParkingDomainException {

   private final String entryDocumentNumber;
   private final String exitDocumentNumber;
   private final Long transactionId;

   public DocumentMismatchException(String entryDocumentNumber,
                                    String exitDocumentNumber,
                                    Long transactionId) {
      super(String.format(
            "ALERTA DE SEGURIDAD: El documento de salida '%s' NO coincide con el " +
                  "documento de entrada '%s' (Transaction ID: %d). " +
                  "Posible intento de robo. Se ha bloqueado la operación.",
            exitDocumentNumber, entryDocumentNumber, transactionId
      ));
      this.entryDocumentNumber = entryDocumentNumber;
      this.exitDocumentNumber = exitDocumentNumber;
      this.transactionId = transactionId;
   }

   public String getEntryDocumentNumber() {
      return entryDocumentNumber;
   }

   public String getExitDocumentNumber() {
      return exitDocumentNumber;
   }

   public Long getTransactionId() {
      return transactionId;
   }
}