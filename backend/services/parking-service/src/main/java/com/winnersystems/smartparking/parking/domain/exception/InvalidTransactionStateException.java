package com.winnersystems.smartparking.parking.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación en una transacción
 * que no está en el estado correcto.
 *
 * Escenarios:
 * - Intentar registrar salida de una transacción ya COMPLETED
 * - Intentar crear pago para una transacción CANCELLED
 * - Intentar cancelar una transacción ya PAID
 * - Intentar modificar una transacción en estado incorrecto
 *
 * Esta excepción protege la máquina de estados de Transaction.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class InvalidTransactionStateException extends ParkingDomainException {

   private final Long transactionId;
   private final String currentStatus;
   private final String requiredStatus;
   private final String attemptedOperation;

   public InvalidTransactionStateException(Long transactionId,
                                           String currentStatus,
                                           String requiredStatus,
                                           String attemptedOperation) {
      super(String.format(
            "La transacción ID %d está en estado '%s' pero se requiere '%s' " +
                  "para realizar la operación: %s",
            transactionId, currentStatus, requiredStatus, attemptedOperation
      ));
      this.transactionId = transactionId;
      this.currentStatus = currentStatus;
      this.requiredStatus = requiredStatus;
      this.attemptedOperation = attemptedOperation;
   }

   public Long getTransactionId() {
      return transactionId;
   }

   public String getCurrentStatus() {
      return currentStatus;
   }

   public String getRequiredStatus() {
      return requiredStatus;
   }

   public String getAttemptedOperation() {
      return attemptedOperation;
   }
}