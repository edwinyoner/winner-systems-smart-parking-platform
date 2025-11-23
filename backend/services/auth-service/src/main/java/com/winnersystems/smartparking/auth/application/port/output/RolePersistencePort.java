package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;
import com.winnersystems.smartparking.auth.domain.model.Role;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de roles.
 */
public interface RolePersistencePort {

   /**
    * Guarda un nuevo rol o actualiza uno existente
    */
   Role save(Role role);

   /**
    * Busca un rol por su ID
    */
   Optional<Role> findById(Long id);

   /**
    * Busca un rol por su tipo (ADMIN, USER, etc.)
    */
   Optional<Role> findByRoleType(RoleType roleType);

   /**
    * Verifica si existe un rol de cierto tipo
    */
   boolean existsByRoleType(RoleType roleType);

   /**
    * Lista todos los roles
    */
   List<Role> findAll();

   /**
    * Lista solo roles activos
    */
   List<Role> findActiveRoles();

   /**
    * Elimina un rol por ID
    */
   void deleteById(Long id);
}