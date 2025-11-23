package com.winnersystems.smartparking.auth.application.port.input.user;

/**
 * Caso de uso: ELIMINAR USUARIO
 */
public interface DeleteUserUseCase {

   /**
    * Elimina un usuario (soft delete)
    *
    * Flujo:
    * 1. Buscar usuario por ID
    * 2. Validar que existe
    * 3. Marcar como eliminado (deleted = true)
    * 4. Desactivar (status = INACTIVE)
    * 5. Revocar todos sus tokens
    * 6. Guardar cambios
    *
    * @param userId ID del usuario a eliminar
    * @throws UserNotFoundException si el usuario no existe
    */
   void deleteById(Long userId);

   /**
    * Elimina permanentemente un usuario (hard delete)
    * USAR CON CUIDADO - No se puede deshacer
    *
    * @param userId ID del usuario
    */
   void executePermanent(Long userId);
}