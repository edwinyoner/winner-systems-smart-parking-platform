package com.winnersystems.smartparking.auth.infrastructure.config.security;

import com.winnersystems.smartparking.auth.application.port.output.UserPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;  // AGREGAR

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementación de UserDetailsService de Spring Security.
 *
 * <p>Spring Security usa esta clase para cargar los datos del usuario
 * cuando se autentica (en el JwtAuthenticationFilter).</p>
 *
 * <p>Convierte nuestro User (domain) en CustomUserDetails (que incluye userId).</p>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserPersistencePort userPersistencePort;

   public UserDetailsServiceImpl(UserPersistencePort userPersistencePort) {
      this.userPersistencePort = userPersistencePort;
   }

   @Override
   @Transactional(readOnly = true)
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      // 1. Buscar usuario por email
      User user = userPersistencePort.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                  "Usuario no encontrado con email: " + email));

      // 2. Convertir a CustomUserDetails (incluye userId)
      return new CustomUserDetails(
            user.getId(),                    // userId
            user.getEmail(),                 // username
            user.getPassword(),              // password
            true,                            // accountNonExpired
            user.isFullyActive(),            // accountNonLocked
            true,                            // credentialsNonExpired
            user.isFullyActive(),            // enabled
            getAuthorities(user)             // authorities (roles + permisos)
      );
   }

   /**
    * Convierte roles y permisos del usuario a GrantedAuthority de Spring Security.
    *
    * <p>Formato:</p>
    * <ul>
    *   <li>Roles: "ROLE_ADMIN", "ROLE_AUTORIDAD", "ROLE_OPERADOR"</li>
    *   <li>Permisos: "users.create", "parking.update", etc.</li>
    * </ul>
    */
   private Collection<? extends GrantedAuthority> getAuthorities(User user) {
      Set<GrantedAuthority> authorities = new HashSet<>();

      // 1. Agregar ROLES (con prefijo ROLE_)
      for (Role role : user.getRoles()) {
         authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

         // 2. Agregar PERMISOS de cada rol
         for (Permission permission : role.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
         }
      }

      return authorities;
   }
}