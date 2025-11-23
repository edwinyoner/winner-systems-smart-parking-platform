package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.User;

/**
 * Puerto de salida para envío de emails.
 * La implementación puede usar SMTP, SendGrid, AWS SES, etc.
 */
public interface EmailPort {

   /**
    * Envía email de bienvenida con link de verificación
    * @param user usuario recién registrado
    * @param verificationToken token de verificación
    */
   void sendWelcomeEmail(User user, String verificationToken);

   /**
    * Envía email de verificación de cuenta
    * @param user usuario
    * @param verificationToken token de verificación
    */
   void sendVerificationEmail(User user, String verificationToken);

   /**
    * Envía email para restablecer contraseña
    * @param user usuario que olvidó su contraseña
    * @param resetToken token de reset
    */
   void sendPasswordResetEmail(User user, String resetToken);

   /**
    * Envía email de confirmación de cambio de contraseña
    * @param user usuario
    */
   void sendPasswordChangedEmail(User user);

   /**
    * Envía email genérico
    * @param to email destino
    * @param subject asunto
    * @param body cuerpo del email (puede ser HTML)
    */
   void sendEmail(String to, String subject, String body);
}