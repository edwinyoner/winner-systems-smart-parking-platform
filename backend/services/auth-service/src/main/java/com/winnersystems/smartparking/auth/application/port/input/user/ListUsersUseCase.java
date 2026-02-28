package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.dto.query.UserSearchCriteria;

/**
 * Caso de uso: Listar usuarios.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ListUsersUseCase {

   /**
    * Lista usuarios con búsqueda, filtros y paginación.
    *
    * @param criteria criterios de búsqueda (incluye page y size)
    * @return respuesta paginada con usuarios y metadata
    */
   PagedResponse<UserDto> execute(UserSearchCriteria criteria);
}