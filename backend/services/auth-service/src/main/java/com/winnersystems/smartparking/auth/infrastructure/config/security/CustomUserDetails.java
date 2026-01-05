package com.winnersystems.smartparking.auth.infrastructure.config.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Implementación personalizada de UserDetails.
 * Agrega el userId al principal para facilitar su acceso en los controllers.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Getter
public class CustomUserDetails implements UserDetails {

   private final Long userId;
   private final String email;
   private final String password;
   private final boolean accountNonExpired;
   private final boolean accountNonLocked;
   private final boolean credentialsNonExpired;
   private final boolean enabled;
   private final Collection<? extends GrantedAuthority> authorities;

   public CustomUserDetails(
         Long userId,
         String email,
         String password,
         boolean accountNonExpired,
         boolean accountNonLocked,
         boolean credentialsNonExpired,
         boolean enabled,
         Collection<? extends GrantedAuthority> authorities) {

      this.userId = userId;
      this.email = email;
      this.password = password;
      this.accountNonExpired = accountNonExpired;
      this.accountNonLocked = accountNonLocked;
      this.credentialsNonExpired = credentialsNonExpired;
      this.enabled = enabled;
      this.authorities = authorities;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return accountNonExpired;
   }

   @Override
   public boolean isAccountNonLocked() {
      return accountNonLocked;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return credentialsNonExpired;
   }

   @Override
   public boolean isEnabled() {
      return enabled;
   }
}