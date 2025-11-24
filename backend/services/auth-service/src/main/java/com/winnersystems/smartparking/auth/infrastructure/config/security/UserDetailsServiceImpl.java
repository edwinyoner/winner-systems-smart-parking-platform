package com.winnersystems.smartparking.auth.infrastructure.config.security;

import com.winnersystems.smartparking.auth.application.port.output.UserPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementación de UserDetailsService de Spring Security.
 *
 * Spring Security usa esta clase para cargar los datos del usuario
 * cuando se autentica (en el JwtAuthenticationFilter).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserPersistencePort userRepository;

   public UserDetailsServiceImpl(UserPersistencePort userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      // 1. Buscar usuario por email
      User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                  "Usuario no encontrado con email: " + email));

      // 2. Convertir a UserDetails de Spring Security
      return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(getAuthorities(user))
            .accountExpired(false)
            .accountLocked(!user.isFullyActive())
            .credentialsExpired(false)
            .disabled(!user.canLogin())
            .build();
   }

   /**
    * Convierte los roles del usuario a GrantedAuthority de Spring Security
    */
   private Collection<? extends GrantedAuthority> getAuthorities(User user) {
      return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleType().name()))
            .collect(Collectors.toList());
   }
}