package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comando para registrar la ENTRADA de un vehículo al estacionamiento.
 *
 * Este comando captura toda la información necesaria para crear una Transaction
 * en estado ACTIVE.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordEntryCommand {

   // ========================= IDENTIFICACIÓN =========================

   private String plateNumber;                     // Placa del vehículo (NOT NULL)
   private Long vehicleTypeId;                     // Tipo de vehículo (opcional)

   // ========================= UBICACIÓN =========================

   private Long zoneId;                            // ID de la zona (NOT NULL)
   private Long spaceId;                           // ID del espacio específico (NOT NULL)

   // ========================= CONDUCTOR =========================

   private Long documentTypeId;                    // Tipo de documento (NOT NULL)
   private String documentNumber;                  // Número de documento (NOT NULL)
   private String customerName;                    // Nombre del conductor (opcional)
   private String customerPhone;                   // Teléfono (opcional)
   private String customerEmail;                   // Email (opcional)

   // ========================= REGISTRO =========================

   private Long operatorId;                        // Operador que registra (NOT NULL)
   private String entryMethod;                     // MANUAL, CAMERA_AI, SENSOR

   // ========================= EVIDENCIA (OPCIONAL) =========================

   private String photoUrl;                        // URL foto entrada (opcional)
   private Double plateConfidence;                 // Confianza IA (0.0-1.0)

   // ========================= OBSERVACIONES =========================

   private String notes;                           // Notas adicionales (opcional)
}