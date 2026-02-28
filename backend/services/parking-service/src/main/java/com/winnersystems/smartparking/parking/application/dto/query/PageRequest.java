package com.winnersystems.smartparking.parking.application.dto.query;

import java.util.Objects;

/**
 * Solicitud de paginación genérica.
 *
 * Representa los parámetros de paginación y ordenamiento
 * de manera independiente del framework.
 *
 * @param page Número de página (base 0)
 * @param size Tamaño de página
 * @param sortBy Campo por el cual ordenar (opcional)
 * @param sortDirection Dirección del ordenamiento: ASC o DESC
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record PageRequest(
      int page,
      int size,
      String sortBy,
      String sortDirection
) {

   /**
    * Constructor compacto con validaciones.
    */
   public PageRequest {
      // Validar página no negativa
      if (page < 0) {
         throw new IllegalArgumentException("El número de página no puede ser negativo");
      }

      // Validar tamaño positivo (máximo 100)
      if (size <= 0) {
         throw new IllegalArgumentException("El tamaño de página debe ser mayor a 0");
      }
      if (size > 100) {
         throw new IllegalArgumentException("El tamaño de página no puede exceder 100");
      }

      // Validar dirección de ordenamiento
      if (sortDirection != null &&
            !sortDirection.equalsIgnoreCase("ASC") &&
            !sortDirection.equalsIgnoreCase("DESC")) {
         throw new IllegalArgumentException("La dirección de ordenamiento debe ser ASC o DESC");
      }

      // Normalizar sortDirection a uppercase
      if (sortDirection != null) {
         sortDirection = sortDirection.toUpperCase();
      }
   }

   // ========================= FACTORY METHODS =========================

   /**
    * Crea una solicitud de paginación básica sin ordenamiento.
    *
    * @param page Número de página (base 0)
    * @param size Tamaño de página
    * @return PageRequest configurado
    */
   public static PageRequest of(int page, int size) {
      return new PageRequest(page, size, null, null);
   }

   /**
    * Crea una solicitud de paginación con ordenamiento ascendente.
    *
    * @param page Número de página (base 0)
    * @param size Tamaño de página
    * @param sortBy Campo por el cual ordenar
    * @return PageRequest configurado con ordenamiento ASC
    */
   public static PageRequest of(int page, int size, String sortBy) {
      return new PageRequest(page, size, sortBy, "ASC");
   }

   /**
    * Crea una solicitud de paginación con ordenamiento personalizado.
    *
    * @param page Número de página (base 0)
    * @param size Tamaño de página
    * @param sortBy Campo por el cual ordenar
    * @param sortDirection ASC o DESC
    * @return PageRequest configurado
    */
   public static PageRequest of(int page, int size, String sortBy, String sortDirection) {
      return new PageRequest(page, size, sortBy, sortDirection);
   }

   /**
    * Crea una solicitud para la primera página.
    *
    * @param size Tamaño de página
    * @return PageRequest para la primera página
    */
   public static PageRequest firstPage(int size) {
      return new PageRequest(0, size, null, null);
   }

   /**
    * Crea una solicitud para la primera página con ordenamiento.
    *
    * @param size Tamaño de página
    * @param sortBy Campo por el cual ordenar
    * @return PageRequest para la primera página ordenada
    */
   public static PageRequest firstPage(int size, String sortBy) {
      return new PageRequest(0, size, sortBy, "ASC");
   }

   /**
    * Crea una solicitud con tamaño de página por defecto (10).
    *
    * @param page Número de página
    * @return PageRequest con tamaño 10
    */
   public static PageRequest withDefaultSize(int page) {
      return new PageRequest(page, 10, null, null);
   }

   // ========================= UTILITY METHODS =========================

   /**
    * Calcula el offset para consultas SQL.
    *
    * @return offset = page * size
    */
   public int getOffset() {
      return page * size;
   }

   /**
    * Obtiene la página siguiente.
    *
    * @return PageRequest para la siguiente página
    */
   public PageRequest next() {
      return new PageRequest(page + 1, size, sortBy, sortDirection);
   }

   /**
    * Obtiene la página anterior.
    *
    * @return PageRequest para la página anterior (null si está en la primera)
    */
   public PageRequest previous() {
      if (page == 0) {
         return null;
      }
      return new PageRequest(page - 1, size, sortBy, sortDirection);
   }

   /**
    * Verifica si tiene ordenamiento configurado.
    *
    * @return true si sortBy no es null
    */
   public boolean hasSorting() {
      return sortBy != null && !sortBy.trim().isEmpty();
   }

   /**
    * Verifica si el ordenamiento es ascendente.
    *
    * @return true si sortDirection es ASC
    */
   public boolean isAscending() {
      return "ASC".equals(sortDirection);
   }

   /**
    * Verifica si el ordenamiento es descendente.
    *
    * @return true si sortDirection es DESC
    */
   public boolean isDescending() {
      return "DESC".equals(sortDirection);
   }

   /**
    * Verifica si es la primera página.
    *
    * @return true si page == 0
    */
   public boolean isFirstPage() {
      return page == 0;
   }

   @Override
   public String toString() {
      return String.format("PageRequest[page=%d, size=%d, sortBy=%s, sortDirection=%s]",
            page, size, sortBy, sortDirection);
   }
}