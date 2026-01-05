package com.winnersystems.smartparking.auth.application.dto.query;

/**
 * Criterios de búsqueda para filtrar usuarios.
 *
 * <p>Todos los campos son opcionales (null = no filtrar por ese criterio).
 * Este DTO es inmutable y se usa para consultas de lectura.</p>
 *
 * @param searchTerm Texto a buscar en firstName, lastName o email
 * @param status true=activos, false=inactivos, null=todos
 * @param emailVerified true=verificados, false=no verificados, null=todos
 * @param includeDeleted true=incluir eliminados, false/null=excluir eliminados
 * @param roleName Filtrar por rol específico (ej: "ADMIN", "OPERADOR")
 * @param sortBy Campo por el cual ordenar (ej: "firstName", "createdAt")
 * @param sortDirection "asc" o "desc"
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record UserSearchCriteria(
      String searchTerm,
      Boolean status,
      Boolean emailVerified,
      Boolean includeDeleted,
      String roleName,
      String sortBy,
      String sortDirection
) {
}