package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Puerto de salida para persistencia de roles.
 *
 * <h3>Roles del sistema</h3>
 * <ul>
 *   <li><b>ADMIN:</b> Rol de sistema (acceso total, no modificable)</li>
 *   <li><b>AUTORIDAD:</b> Supervisión de operaciones</li>
 *   <li><b>OPERADOR:</b> Operaciones diarias</li>
 *   <li><b>Personalizados:</b> Se pueden crear nuevos roles dinámicamente</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RolePersistencePort {

   /**
    * Busca múltiples roles por sus IDs.
    *
    * @param ids conjunto de IDs de roles
    * @return lista de roles encontrados
    */
   List<Role> findAllByIds(Set<Long> ids);

   /**
    * Lista todos los roles del sistema.
    *
    * @return lista de todos los roles (incluye roles de sistema y personalizados)
    */
   List<Role> findAll();

   /**
    * Busca un rol por su ID.
    *
    * @param id identificador del rol
    * @return Optional con el rol si existe, empty si no
    */
   Optional<Role> findById(Long id);

   /**
    * Busca un rol por su nombre único.
    *
    * @param name nombre del rol (case-sensitive)
    * @return Optional con el rol si existe, empty si no
    */
   Optional<Role> findByName(String name);

   /**
    * Guarda un nuevo rol o actualiza uno existente.
    *
    * @param role rol a guardar
    * @return rol guardado con ID generado
    */
   Role save(Role role);

   /**
    * Obtiene todos los roles activos
    */
   List<Role> findAllActive();

}