package com.winnersystems.smartparking.auth.application.dto.query;

import java.util.List;

/**
 * Respuesta paginada genérica.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record PagedResponse<T>(
      List<T> content,
      int number,              // CAMBIO: currentPage → number
      int size,                // CAMBIO: pageSize → size
      long totalElements,
      int totalPages,
      boolean first,
      boolean last,
      boolean hasNext,
      boolean hasPrevious
) {
   /**
    * Crea una respuesta paginada sin paginación real.
    */
   public static <T> PagedResponse<T> of(List<T> content) {
      return new PagedResponse<>(
            content,
            0,                      // Primera página
            content.size(),         // Tamaño = cantidad de elementos
            content.size(),         // Total = cantidad de elementos
            1,                      // Una sola página
            true,                   // Es primera
            true,                   // Es última
            false,                  // No hay siguiente
            false                   // No hay anterior
      );
   }

   /**
    * Crea una respuesta paginada con cálculo automático de metadata.
    */
   public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements) {
      int totalPages = (int) Math.ceil((double) totalElements / size);
      boolean first = page == 0;
      boolean last = page >= totalPages - 1;
      boolean hasNext = page < totalPages - 1;
      boolean hasPrevious = page > 0;

      return new PagedResponse<>(
            content,
            page,
            size,
            totalElements,
            totalPages,
            first,
            last,
            hasNext,
            hasPrevious
      );
   }
}