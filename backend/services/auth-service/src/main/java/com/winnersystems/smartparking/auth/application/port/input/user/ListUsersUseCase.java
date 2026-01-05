package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.dto.query.UserSearchCriteria;

/**
 * Caso de uso: Listar usuarios.
 *
 * <p>Permite listar y buscar usuarios del sistema con paginación,
 * filtros y ordenamiento.</p>
 *
 * <h3>Autorización requerida</h3>
 * <ul>
 *   <li>Rol: <b>ADMIN</b></li>
 *   <li>Permiso: <b>users.read</b></li>
 * </ul>
 *
 * <h3>Funcionalidades</h3>
 * <ul>
 *   <li>Búsqueda por texto en nombre, apellido o email</li>
 *   <li>Filtrar por estado (activo/inactivo)</li>
 *   <li>Filtrar por email verificado</li>
 *   <li>Filtrar por rol</li>
 *   <li>Incluir/excluir usuarios eliminados</li>
 *   <li>Ordenamiento personalizado</li>
 * </ul>
 *
 * <h3>Usuarios eliminados</h3>
 * <p>Por defecto, NO incluye usuarios eliminados (deletedAt != null).
 * Para incluir usuarios eliminados, especificar includeDeleted = true
 * en UserSearchCriteria.</p>
 *
 * <h3>Ordenamiento</h3>
 * <p>Por defecto, ordena por fecha de creación descendente (más recientes primero).</p>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ListUsersUseCase {

   /**
    * Lista usuarios con búsqueda, filtros y paginación.
    *
    * <p>Aplica los criterios de búsqueda y filtros especificados
    * en UserSearchCriteria. Si criteria es null o vacío, retorna
    * todos los usuarios NO eliminados.</p>
    *
    * @param criteria criterios de búsqueda y filtros (puede ser null)
    * @param page número de página (0-based)
    * @param size tamaño de página (recomendado: 10-50)
    * @return respuesta paginada con usuarios y metadata
    */
   PagedResponse<UserDto> execute(UserSearchCriteria criteria, int page, int size);
}