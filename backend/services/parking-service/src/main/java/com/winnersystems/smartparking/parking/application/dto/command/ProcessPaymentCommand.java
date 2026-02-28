package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Comando para procesar el PAGO de una transacción de estacionamiento.
 *
 * Este comando registra el pago realizado, actualiza el estado de la Transaction
 * a PAID y opcionalmente envía el comprobante digital.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentCommand {

   // ========================= IDENTIFICACIÓN =========================

   private Long transactionId;                     // ID de la transacción (NOT NULL)

   // ========================= PAGO =========================

   private Long paymentTypeId;                     // ID del tipo de pago (NOT NULL)
   private BigDecimal amountPaid;                  // Monto pagado (NOT NULL)
   private String referenceNumber;                 // Número de referencia (opcional, depende del método)

   // ========================= REGISTRO =========================

   private Long operatorId;                        // Operador que cobra (NOT NULL)

   // ========================= COMPROBANTE =========================

   @Builder.Default
   private Boolean sendReceipt = true;             // ¿Enviar comprobante? (default true)

   // ========================= OBSERVACIONES =========================

   private String notes;                           // Notas adicionales (opcional)
}