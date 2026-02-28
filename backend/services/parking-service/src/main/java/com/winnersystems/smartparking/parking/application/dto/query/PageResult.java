package com.winnersystems.smartparking.parking.application.dto.query;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resultado paginado genérico.
 *
 * Encapsula el resultado de una consulta paginada con metadata.
 * Este DTO se usa internamente en la capa de Application/Domain.
 *
 * @param <T> Tipo de elemento en el resultado
 * @param content Lista de elementos de la página actual
 * @param pageNumber Número de página actual (base 0)
 * @param pageSize Tamaño de página solicitado
 * @param totalElements Total de elementos en todas las páginas
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record PageResult<T>(
      List<T> content,
      int pageNumber,
      int pageSize,
      long totalElements
) {

   /**
    * Constructor compacto con validaciones.
    */
   public PageResult {
      if (content == null) {
         content = Collections.emptyList();
      }
      if (pageNumber < 0) {
         throw new IllegalArgumentException("El número de página no puede ser negativo");
      }
      if (pageSize <= 0) {
         throw new IllegalArgumentException("El tamaño de página debe ser mayor a 0");
      }
      if (totalElements < 0) {
         throw new IllegalArgumentException("El total de elementos no puede ser negativo");
      }

      // Hacer la lista inmutable
      content = Collections.unmodifiableList(content);
   }

   // ========================= FACTORY METHODS =========================

   /**
    * Crea un resultado vacío.
    *
    * @param <T> Tipo de elemento
    * @return PageResult vacío
    */
   public static <T> PageResult<T> empty() {
      return new PageResult<>(Collections.emptyList(), 0, 10, 0);
   }

   /**
    * Crea un resultado vacío con parámetros personalizados.
    *
    * @param pageNumber Número de página
    * @param pageSize Tamaño de página
    * @param <T> Tipo de elemento
    * @return PageResult vacío
    */
   public static <T> PageResult<T> empty(int pageNumber, int pageSize) {
      return new PageResult<>(Collections.emptyList(), pageNumber, pageSize, 0);
   }

   /**
    * Crea un resultado con una sola página (sin paginación real).
    *
    * @param content Lista completa de elementos
    * @param <T> Tipo de elemento
    * @return PageResult con todos los elementos en una página
    */
   public static <T> PageResult<T> of(List<T> content) {
      return new PageResult<>(content, 0, content.size(), content.size());
   }

   /**
    * Crea un resultado paginado.
    *
    * @param content Elementos de la página actual
    * @param pageNumber Número de página actual
    * @param pageSize Tamaño de página
    * @param totalElements Total de elementos
    * @param <T> Tipo de elemento
    * @return PageResult configurado
    */
   public static <T> PageResult<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
      return new PageResult<>(content, pageNumber, pageSize, totalElements);
   }

   /**
    * Crea un resultado paginado desde un PageRequest.
    *
    * @param content Elementos de la página actual
    * @param request PageRequest usado para la consulta
    * @param totalElements Total de elementos
    * @param <T> Tipo de elemento
    * @return PageResult configurado
    */
   public static <T> PageResult<T> of(List<T> content, PageRequest request, long totalElements) {
      return new PageResult<>(content, request.page(), request.size(), totalElements);
   }

   // ========================= COMPUTED PROPERTIES =========================

   /**
    * Calcula el número total de páginas.
    *
    * @return Total de páginas
    */
   public int getTotalPages() {
      return pageSize == 0 ? 1 : (int) Math.ceil((double) totalElements / pageSize);
   }

   /**
    * Verifica si es la primera página.
    *
    * @return true si pageNumber == 0
    */
   public boolean isFirst() {
      return pageNumber == 0;
   }

   /**
    * Verifica si es la última página.
    *
    * @return true si es la última página
    */
   public boolean isLast() {
      return pageNumber >= getTotalPages() - 1;
   }

   /**
    * Verifica si hay una página siguiente.
    *
    * @return true si hay siguiente
    */
   public boolean hasNext() {
      return pageNumber < getTotalPages() - 1;
   }

   /**
    * Verifica si hay una página anterior.
    *
    * @return true si hay anterior
    */
   public boolean hasPrevious() {
      return pageNumber > 0;
   }

   /**
    * Verifica si el resultado está vacío.
    *
    * @return true si content está vacío
    */
   public boolean isEmpty() {
      return content.isEmpty();
   }

   /**
    * Obtiene el número de elementos en la página actual.
    *
    * @return Cantidad de elementos
    */
   public int getNumberOfElements() {
      return content.size();
   }

   /**
    * Verifica si hay contenido.
    *
    * @return true si content no está vacío
    */
   public boolean hasContent() {
      return !content.isEmpty();
   }

   // ========================= TRANSFORMATION METHODS =========================

   /**
    * Mapea el contenido a otro tipo.
    *
    * @param mapper Función de mapeo
    * @param <U> Tipo destino
    * @return PageResult con contenido mapeado
    */
   public <U> PageResult<U> map(Function<T, U> mapper) {
      List<U> mappedContent = content.stream()
            .map(mapper)
            .collect(Collectors.toList());

      return new PageResult<>(mappedContent, pageNumber, pageSize, totalElements);
   }

   /**
    * Convierte a PagedResponse (para REST responses).
    *
    * @return PagedResponse equivalente
    */
   public PagedResponse<T> toPagedResponse() {
      return new PagedResponse<>(
            content,
            pageNumber,
            pageSize,
            totalElements,
            getTotalPages(),
            isFirst(),
            isLast(),
            hasNext(),
            hasPrevious()
      );
   }

   /**
    * Convierte a PagedResponse con mapeo.
    *
    * @param mapper Función de mapeo
    * @param <U> Tipo destino
    * @return PagedResponse mapeado
    */
   public <U> PagedResponse<U> toPagedResponse(Function<T, U> mapper) {
      return map(mapper).toPagedResponse();
   }

   @Override
   public String toString() {
      return String.format(
            "PageResult[page=%d/%d, size=%d, totalElements=%d, hasContent=%s]",
            pageNumber + 1,
            getTotalPages(),
            pageSize,
            totalElements,
            hasContent()
      );
   }
}