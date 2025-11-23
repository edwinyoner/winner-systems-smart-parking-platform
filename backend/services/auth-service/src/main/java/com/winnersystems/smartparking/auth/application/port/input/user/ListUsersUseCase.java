package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

import java.util.List;

/**
 * Caso de uso: LISTAR USUARIOS
 */
public interface ListUsersUseCase {

   /**
    * Lista todos los usuarios (no eliminados)
    *
    * @return lista de usuarios
    */
   List<UserDto> execute();

   /**
    * Lista usuarios con paginación
    *
    * @param page número de página (0-based)
    * @param size tamaño de página
    * @return lista paginada de usuarios
    */
   List<UserDto> execute(int page, int size);

   /**
    * Lista solo usuarios activos
    *
    * @return lista de usuarios con status ACTIVE
    */
   List<UserDto> executeActiveUsers();
}