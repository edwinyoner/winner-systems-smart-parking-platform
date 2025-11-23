package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de usuarios.
 *
 * ¿Qué es un "Puerto"?
 * Es una INTERFACE que define las operaciones que necesita la aplicación
 * sin importar CÓMO se implementan (podría ser MySQL, PostgreSQL, MongoDB, etc.)
 *
 * Este puerto será implementado en la capa INFRASTRUCTURE.
 */
public interface UserPersistencePort {

   /**
    * Guarda un nuevo usuario o actualiza uno existente
    * @param user usuario a guardar
    * @return usuario guardado con ID generado
    */
   User save(User user);

   /**
    * Busca un usuario por su ID
    * @param id identificador del usuario
    * @return Optional con el usuario si existe, empty si no
    */
   Optional<User> findById(Long id);

   /**
    * Busca un usuario por su email
    * @param email email del usuario
    * @return Optional con el usuario si existe, empty si no
    */
   Optional<User> findByEmail(String email);

   /**
    * Verifica si existe un usuario con el email dado
    * @param email email a verificar
    * @return true si existe, false si no
    */
   boolean existsByEmail(String email);

   /**
    * Lista todos los usuarios (no eliminados)
    * @return lista de usuarios
    */
   List<User> findAll();

   /**
    * Lista usuarios con paginación
    * @param page número de página (0-based)
    * @param size tamaño de página
    * @return lista paginada de usuarios
    */
   List<User> findAll(int page, int size);

   /**
    * Busca usuarios por rol
    * @param roleName nombre del rol (ADMIN, USER, etc.)
    * @return lista de usuarios con ese rol
    */
   List<User> findByRole(String roleName);

   /**
    * Busca usuarios activos
    * @return lista de usuarios con status ACTIVE
    */
   List<User> findActiveUsers();

   /**
    * Elimina un usuario (soft delete o hard delete según implementación)
    * @param id ID del usuario a eliminar
    */
   void deleteById(Long id);

   /**
    * Cuenta el total de usuarios
    * @return total de usuarios
    */
   long count();

   /**
    * Cuenta usuarios activos
    * @return total de usuarios con status ACTIVE
    */
   long countActive();
}