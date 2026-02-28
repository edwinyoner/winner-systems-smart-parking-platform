package com.winnersystems.smartparking.parking.domain.exception;

/**
 * Excepción lanzada cuando se intenta registrar la entrada de un vehículo
 * que ya tiene una transacción activa (ya está dentro del estacionamiento).
 *
 * Escenario:
 * - Operador intenta registrar entrada de placa ABC-123
 * - Sistema detecta que ABC-123 ya tiene una Transaction con status ACTIVE
 * - Se lanza esta excepción
 *
 * Solución:
 * - Primero registrar salida de la transacción anterior
 * - Luego registrar nueva entrada
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class VehicleAlreadyInsideException extends ParkingDomainException {

   private final String plateNumber;
   private final Long activeTransactionId;

   public VehicleAlreadyInsideException(String plateNumber, Long activeTransactionId) {
      super(String.format(
            "El vehículo con placa '%s' ya tiene una transacción activa (ID: %d). " +
                  "Debe registrar la salida antes de una nueva entrada.",
            plateNumber, activeTransactionId
      ));
      this.plateNumber = plateNumber;
      this.activeTransactionId = activeTransactionId;
   }

   public String getPlateNumber() {
      return plateNumber;
   }

   public Long getActiveTransactionId() {
      return activeTransactionId;
   }
}