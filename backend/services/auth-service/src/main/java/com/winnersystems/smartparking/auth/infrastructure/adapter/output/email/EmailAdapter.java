package com.winnersystems.smartparking.auth.infrastructure.adapter.output.email;

import com.winnersystems.smartparking.auth.application.port.output.EmailPort;
import com.winnersystems.smartparking.auth.domain.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Adaptador para envío de emails.
 * Implementa EmailPort usando JavaMailSender de Spring.
 */
@Component
public class EmailAdapter implements EmailPort {

   private final JavaMailSender mailSender;

   @Value("${mail.from:noreply@smartparking.com}")
   private String fromEmail;

   @Value("${mail.from-name:Smart Parking System}")
   private String fromName;

   @Value("${app.frontend-url:http://localhost:4200}")
   private String frontendUrl;

   public EmailAdapter(JavaMailSender mailSender) {
      this.mailSender = mailSender;
   }

   @Override
   public void sendWelcomeEmail(User user, String verificationToken) {
      String subject = "¡Bienvenido a Smart Parking!";
      String verifyUrl = frontendUrl + "/verify-email?token=" + verificationToken;

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
                        <p><small>Este enlace expira en 24 horas.</small></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(user.getFirstName(), verifyUrl, verifyUrl);

      sendEmail(user.getEmail(), subject, body);
   }

   @Override
   public void sendVerificationEmail(User user, String verificationToken) {
      sendWelcomeEmail(user, verificationToken);
   }

   @Override
   public void sendPasswordResetEmail(User user, String resetToken) {
      String subject = "Restablecer Contraseña - Smart Parking";
      String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;

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
                        <p><small>Este enlace expira en 1 hora.</small></p>
                        <p>Si no solicitaste este cambio, ignora este correo.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(user.getFirstName(), resetUrl, resetUrl);

      sendEmail(user.getEmail(), subject, body);
   }

   @Override
   public void sendPasswordChangedEmail(User user) {
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
                        <p>Tu contraseña ha sido cambiada exitosamente.</p>
                        <p>Si no realizaste este cambio, por favor contacta a soporte inmediatamente.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(user.getFirstName());

      sendEmail(user.getEmail(), subject, body);
   }

   @Override
   public void sendEmail(String to, String subject, String body) {
      try {
         MimeMessage message = mailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

         helper.setFrom(fromEmail, fromName);
         helper.setTo(to);
         helper.setSubject(subject);
         helper.setText(body, true); // true = HTML

         mailSender.send(message);

      } catch (MessagingException e) {
         throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
      } catch (Exception e) {
         throw new RuntimeException("Error inesperado al enviar email", e);
      }
   }
}