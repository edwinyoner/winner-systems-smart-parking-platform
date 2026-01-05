package com.winnersystems.smartparking.auth.application.dto.query;

import java.util.List;

/**
 * Respuesta paginada genérica.
 *
 * <p>Contiene datos paginados y metadata de navegación.
 * Usa factory methods estáticos para construcción conveniente.</p>
 *
 * @param <T> tipo de datos en la lista
 * @param content lista de elementos de la página actual
 * @param currentPage página actual (0-based)
 * @param pageSize tamaño de página solicitado
 * @param totalElements total de elementos en BD
 * @param totalPages total de páginas
 * @param first true si es la primera página
 * @param last true si es la última página
 * @param hasNext true si hay página siguiente
 * @param hasPrevious true si hay página anterior
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record PagedResponse<T>(
      List<T> content,
      int currentPage,
      int pageSize,
      long totalElements,
      int totalPages,
      boolean first,
      boolean last,
      boolean hasNext,
      boolean hasPrevious
) {
   /**
    * Crea una respuesta paginada sin paginación real.
    *
    * <p>Útil cuando se retorna una lista completa como si fuera
    * una sola página (ej: filtros que retornan pocos resultados).</p>
    *
    * @param content lista completa de elementos
    * @param <T> tipo de elementos
    * @return respuesta paginada con una sola página
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
    *
    * <p>Calcula automáticamente totalPages, first, last, hasNext, hasPrevious
    * basándose en los parámetros proporcionados.</p>
    *
    * @param content lista de elementos de la página actual
    * @param currentPage página actual (0-based)
    * @param pageSize tamaño de página
    * @param totalElements total de elementos en BD
    * @param <T> tipo de elementos
    * @return respuesta paginada con metadata calculada
    */
   public static <T> PagedResponse<T> of(List<T> content, int currentPage, int pageSize, long totalElements) {
      int totalPages = (int) Math.ceil((double) totalElements / pageSize);
      boolean first = currentPage == 0;
      boolean last = currentPage >= totalPages - 1;
      boolean hasNext = currentPage < totalPages - 1;
      boolean hasPrevious = currentPage > 0;

      return new PagedResponse<>(
            content,
            currentPage,
            pageSize,
            totalElements,
            totalPages,
            first,
            last,
            hasNext,
            hasPrevious
      );
   }
}