package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comando para registrar la SALIDA de un vehículo del estacionamiento.
 *
 * Este comando actualiza una Transaction de ACTIVE a COMPLETED y calcula
 * el monto a pagar basado en la duración y tarifa aplicable.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordExitCommand {

   // ========================= IDENTIFICACIÓN =========================

   private Long transactionId;                     // ID de la transacción activa
   // O alternativamente:
   private String plateNumber;                     // Buscar por placa (alternativo)

   // ========================= DOCUMENTO DE SALIDA (SEGURIDAD) =========================

   private Long exitDocumentTypeId;                // Tipo de documento salida (NOT NULL)
   private String exitDocumentNumber;              // Número documento salida (NOT NULL)
   // DEBE coincidir con entrada (anti-robo)

   // ========================= REGISTRO =========================

   private Long operatorId;                        // Operador que registra salida (NOT NULL)
   private String exitMethod;                      // MANUAL, CAMERA_AI, SENSOR

   // ========================= EVIDENCIA (OPCIONAL) =========================

   private String photoUrl;                        // URL foto salida (opcional)
   private Double plateConfidence;                 // Confianza IA (0.0-1.0)

   // ========================= OBSERVACIONES =========================

   private String notes;                           // Notas adicionales (opcional)
}