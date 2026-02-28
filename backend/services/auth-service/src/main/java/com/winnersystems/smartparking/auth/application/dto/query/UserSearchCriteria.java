package com.winnersystems.smartparking.auth.application.dto.query;

/**
 * Criterios de búsqueda y filtrado para usuarios.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record UserSearchCriteria(
      String search,           // Búsqueda por nombre, email, teléfono
      Long roleId,             // Filtro por rol (ID)
      Boolean status,          // Filtro por estado (activo/inactivo)
      Boolean emailVerified,   // Filtro por email verificado
      int page,                // Página actual (0-based)
      int size,                // Tamaño de página
      String sortBy,           // Campo de ordenamiento
      String sortDirection     // Dirección: "asc" o "desc"
) {
   /**
    * Constructor canónico con validaciones.
    */
   public UserSearchCriteria {
      // Validar página
      if (page < 0) {
         page = 0;
      }

      // Validar tamaño
      if (size < 1) {
         size = 10;
      }

      // Validar sortBy
      if (sortBy == null || sortBy.isBlank()) {
         sortBy = "id";
      }

      // Validar sortDirection
      if (sortDirection == null || sortDirection.isBlank()) {
         sortDirection = "desc";
      }
   }

   /**
    * Factory method con valores por defecto.
    */
   public static UserSearchCriteria of(
         String search,
         Long roleId,
         Boolean status,
         Boolean emailVerified,
         int page,
         int size,
         String sortBy,
         String sortDirection
   ) {
      return new UserSearchCriteria(
            search,
            roleId,
            status,
            emailVerified,
            page,
            size,
            sortBy,
            sortDirection
      );
   }

   /**
    * Factory method simple (solo paginación).
    */
   public static UserSearchCriteria withPagination(int page, int size) {
      return new UserSearchCriteria(
            null,
            null,
            null,
            null,
            page,
            size,
            "id",
            "desc"
      );
   }
}