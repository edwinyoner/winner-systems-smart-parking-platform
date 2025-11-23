package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.Permission;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de permisos.
 */
public interface PermissionPersistencePort {

   /**
    * Guarda un nuevo permiso o actualiza uno existente
    */
   Permission save(Permission permission);

   /**
    * Busca un permiso por su ID
    */
   Optional<Permission> findById(Long id);

   /**
    * Busca un permiso por su nombre (ej: "users.create")
    */
   Optional<Permission> findByName(String name);

   /**
    * Verifica si existe un permiso con ese nombre
    */
   boolean existsByName(String name);

   /**
    * Lista todos los permisos
    */
   List<Permission> findAll();

   /**
    * Lista permisos por módulo (ej: "users", "parking")
    */
   List<Permission> findByModule(String module);

   /**
    * Elimina un permiso por ID
    */
   void deleteById(Long id);
}