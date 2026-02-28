package com.winnersystems.smartparking.parking.application.dto.query;

import java.util.List;
import java.util.function.Function;

/**
 * Respuesta paginada genérica.
 *
 * @author Edwin Yoner - Winner Systems
 */
public record PagedResponse<T>(
      List<T> content,
      int pageNumber,        // ó "number" como en auth
      int pageSize,          // ó "size" como en auth
      long totalElements,
      int totalPages,
      boolean first,
      boolean last,
      boolean hasNext,
      boolean hasPrevious
) {
   public static <T> PagedResponse<T> of(List<T> content) {
      return new PagedResponse<>(
            content,
            0,
            content.size(),
            content.size(),
            1,
            true,
            true,
            false,
            false
      );
   }

   public static <T> PagedResponse<T> of(
         List<T> content,
         int page,
         int size,
         long totalElements
   ) {
      int totalPages = (int) Math.ceil((double) totalElements / size);
      return new PagedResponse<>(
            content,
            page,
            size,
            totalElements,
            totalPages,
            page == 0,
            page >= totalPages - 1,
            page < totalPages - 1,
            page > 0
      );
   }

   /**
    * Mapea el contenido a otro tipo manteniendo la metadata de paginación.
    *
    * @param mapper función de mapeo
    * @param <U> tipo destino
    * @return PagedResponse con contenido mapeado
    */
   public <U> PagedResponse<U> map(Function<T, U> mapper) {
      List<U> mappedContent = content.stream()
            .map(mapper)
            .toList();

      return new PagedResponse<>(
            mappedContent,
            pageNumber,
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