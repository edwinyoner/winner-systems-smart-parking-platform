package com.winnersystems.smartparking.auth.infrastructure.adapter.output.email;

import com.winnersystems.smartparking.auth.application.port.output.EmailPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Adaptador para envío de emails.
 * Implementa EmailPort usando JavaMailSender de Spring.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class EmailAdapter implements EmailPort {

   private final JavaMailSender mailSender;

   @Value("${mail.from:noreply@smartparking.com}")
   private String fromEmail;

   @Value("${mail.from-name:Smart Parking System}")
   private String fromName;

   public EmailAdapter(JavaMailSender mailSender) {
      this.mailSender = mailSender;
   }

   // ========== AUTENTICACIÓN Y VERIFICACIÓN ==========

   @Override
   public void sendWelcomeEmail(String toEmail, String userName, String verificationLink, int validityHours) {
      String subject = "¡Bienvenido a Smart Parking!";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                    .button { 
                        display: inline-block; 
                        padding: 10px 20px; 
                        background-color: #4CAF50; 
                        color: white; 
                        text-decoration: none; 
                        border-radius: 5px; 
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>¡Hola %s!</h2>
                        <p>Bienvenido a Smart Parking. Para completar tu registro, por favor verifica tu correo electrónico.</p>
                        <p style="text-align: center; margin: 30px 0;">
                            <a href="%s" class="button">Verificar Email</a>
                        </p>
                        <p>O copia y pega este enlace en tu navegador:</p>
                        <p style="word-break: break-all;">%s</p>
                        <p><small>Este enlace expira en %d horas.</small></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, verificationLink, verificationLink, validityHours);

      sendHtmlEmail(toEmail, subject, body);
   }

   @Override
   public void sendVerificationEmail(String toEmail, String userName, String verificationLink, int validityHours) {
      String subject = "Verificar Email - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                    .button { 
                        display: inline-block; 
                        padding: 10px 20px; 
                        background-color: #4CAF50; 
                        color: white; 
                        text-decoration: none; 
                        border-radius: 5px; 
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Por favor verifica tu correo electrónico para activar tu cuenta.</p>
                        <p style="text-align: center; margin: 30px 0;">
                            <a href="%s" class="button">Verificar Email</a>
                        </p>
                        <p>O copia y pega este enlace en tu navegador:</p>
                        <p style="word-break: break-all;">%s</p>
                        <p><small>Este enlace expira en %d horas.</small></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, verificationLink, verificationLink, validityHours);

      sendHtmlEmail(toEmail, subject, body);
   }

   @Override
   public void sendEmailVerifiedConfirmation(String toEmail, String userName) {
      String subject = "Email Verificado - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>¡Felicidades %s!</h2>
                        <p>Tu email ha sido verificado exitosamente. Ya puedes usar todas las funcionalidades de Smart Parking.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName);

      sendHtmlEmail(toEmail, subject, body);
   }

   // ========== RECUPERACIÓN DE CONTRASEÑA ==========

   @Override
   public void sendPasswordResetEmail(String toEmail, String userName, String resetLink, int validityHours) {
      String subject = "Restablecer Contraseña - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                    .button { 
                        display: inline-block; 
                        padding: 10px 20px; 
                        background-color: #FF9800; 
                        color: white; 
                        text-decoration: none; 
                        border-radius: 5px; 
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Recibimos una solicitud para restablecer tu contraseña.</p>
                        <p style="text-align: center; margin: 30px 0;">
                            <a href="%s" class="button">Restablecer Contraseña</a>
                        </p>
                        <p>O copia y pega este enlace en tu navegador:</p>
                        <p style="word-break: break-all;">%s</p>
                        <p><small>Este enlace expira en %d hora(s).</small></p>
                        <p>Si no solicitaste este cambio, ignora este correo.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, resetLink, resetLink, validityHours);

      sendHtmlEmail(toEmail, subject, body);
   }

   @Override
   public void sendPasswordChangedEmail(String toEmail, String userName, String changedAt) {
      String subject = "Contraseña Cambiada - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #2196F3; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Tu contraseña ha sido cambiada exitosamente el %s.</p>
                        <p>Si no realizaste este cambio, por favor contacta a soporte inmediatamente.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, changedAt);

      sendHtmlEmail(toEmail, subject, body);
   }

   // ========== GESTIÓN DE CUENTA ==========

   @Override
   public void sendAccountDeactivatedEmail(String toEmail, String userName, String reason) {
      String subject = "Cuenta Desactivada - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #F44336; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Tu cuenta ha sido desactivada.</p>
                        <p><strong>Motivo:</strong> %s</p>
                        <p>Si tienes preguntas, por favor contacta al administrador.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, reason != null ? reason : "Sin motivo especificado");

      sendHtmlEmail(toEmail, subject, body);
   }

   @Override
   public void sendAccountActivatedEmail(String toEmail, String userName) {
      String subject = "Cuenta Activada - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>¡Bienvenido de nuevo %s!</h2>
                        <p>Tu cuenta ha sido reactivada exitosamente.</p>
                        <p>Ya puedes iniciar sesión y usar el sistema.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName);

      sendHtmlEmail(toEmail, subject, body);
   }

   @Override
   public void sendRoleAssignedEmail(String toEmail, String userName, String roleName, String assignedBy) {
      String subject = "Nuevo Rol Asignado - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #9C27B0; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Se te ha asignado un nuevo rol: <strong>%s</strong></p>
                        <p><small>Asignado por: %s</small></p>
                        <p>Tus permisos han sido actualizados.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, roleName, assignedBy);

      sendHtmlEmail(toEmail, subject, body);
   }

   @Override
   public void sendRoleRemovedEmail(String toEmail, String userName, String roleName, String removedBy) {
      String subject = "Rol Removido - Smart Parking";

      String body = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #FF5722; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Smart Parking</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Se te ha removido el rol: <strong>%s</strong></p>
                        <p><small>Removido por: %s</small></p>
                        <p>Tus permisos han sido actualizados.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, roleName, removedBy);

      sendHtmlEmail(toEmail, subject, body);
   }

   // ========== EMAIL GENÉRICO ==========

   @Override
   public void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
      try {
         MimeMessage message = mailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

         helper.setFrom(fromEmail, fromName);
         helper.setTo(toEmail);
         helper.setSubject(subject);
         helper.setText(htmlBody, true); // true = HTML

         mailSender.send(message);

      } catch (MessagingException e) {
         throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
      } catch (Exception e) {
         throw new RuntimeException("Error inesperado al enviar email", e);
      }
   }

   @Override
   public void sendVerificationEmail(String toEmail, String userName, String verificationLink) {
      // Llamar a la versión completa con validez por defecto de 24 horas
      sendVerificationEmail(toEmail, userName, verificationLink, 24);
   }
}