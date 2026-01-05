package com.winnersystems.smartparking.auth.infrastructure.adapter.output.security;

import com.winnersystems.smartparking.auth.application.port.output.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adaptador para encriptación de contraseñas.
 * Implementa PasswordEncoderPort usando BCryptPasswordEncoder de Spring Security.
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

   private final PasswordEncoder passwordEncoder;

   public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
      this.passwordEncoder = passwordEncoder;
   }

   @Override
   public String encode(String rawPassword) {
      return passwordEncoder.encode(rawPassword);
   }

   @Override
   public boolean matches(String rawPassword, String encodedPassword) {
      return passwordEncoder.matches(rawPassword, encodedPassword);
   }
}
