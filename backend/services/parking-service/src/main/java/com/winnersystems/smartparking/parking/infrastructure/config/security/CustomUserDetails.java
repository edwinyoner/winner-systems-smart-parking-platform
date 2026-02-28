package com.winnersystems.smartparking.parking.infrastructure.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Detalles del usuario extraídos del JWT Token.
 * Contiene el ID del usuario para auditoría JPA.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class CustomUserDetails implements UserDetails {

   private final Long id;
   private final String email;
   private final String role;
   private final Collection<? extends GrantedAuthority> authorities;

   public CustomUserDetails(
         Long id,
         String email,
         String role,
         Collection<? extends GrantedAuthority> authorities
   ) {
      this.id = id;
      this.email = email;
      this.role = role;
      this.authorities = authorities;
   }

   /**
    * Obtiene el ID del usuario (para auditoría).
    */
   public Long getId() {
      return id;
   }

   /**
    * Obtiene el rol del usuario.
    */
   public String getRole() {
      return role;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
   }

   @Override
   public String getPassword() {
      return null; // No se necesita, token ya validado
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}