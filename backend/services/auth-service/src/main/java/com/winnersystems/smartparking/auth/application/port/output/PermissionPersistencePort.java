package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Puerto de salida para persistencia de permisos.
 *
 * <p>Los permisos son la unidad más granular de control de acceso (PBAC).
 * Se asignan a roles y definen acciones específicas que los usuarios pueden realizar.</p>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface PermissionPersistencePort {

   /**
    * Busca múltiples permisos por sus IDs.
    *
    * @param ids conjunto de IDs de permisos
    * @return lista de permisos encontrados
    */
   List<Permission> findAllByIds(Set<Long> ids);

   /**
    * Lista todos los permisos del sistema.
    *
    * @return lista de todos los permisos
    */
   List<Permission> findAll();

   /**
    * Busca un permiso por su ID.
    *
    * @param id identificador del permiso
    * @return Optional con el permiso si existe, empty si no
    */
   Optional<Permission> findById(Long id);

   /**
    * Busca un permiso por su nombre único.
    *
    * @param name nombre del permiso
    * @return Optional con el permiso si existe, empty si no
    */
   Optional<Permission> findByName(String name);

   /**
    * Guarda un nuevo permiso o actualiza uno existente.
    *
    * @param permission permiso a guardar
    * @return permiso guardado con ID generado
    */
   Permission save(Permission permission);
}