package com.winnersystems.smartparking.parking.application.port.input.infraction;

import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;

/**
 * Puerto de entrada para obtener información de infracciones.
 *
 * Responsabilidades:
 * - Buscar infracción por ID
 * - Buscar infracción por código (INF-YYYY-NNNNNN)
 *
 * Usado por:
 * - Vista de detalle de infracción
 * - Consultas por código único
 * - Verificación de multas
 * - Reportes de auditoría
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface GetInfractionUseCase {

   /**
    * Obtiene una infracción por su ID.
    *
    * @param infractionId ID de la infracción
    * @return InfractionDto con información de la infracción
    *
    * @throws IllegalArgumentException si no existe infracción con ese ID
    */
   InfractionDto getInfractionById(Long infractionId);

   /**
    * Obtiene una infracción por su código único.
    *
    * @param infractionCode código de infracción (ej: "INF-2026-001234")
    * @return InfractionDto si existe, null si no existe
    */
   InfractionDto getInfractionByCode(String infractionCode);
}