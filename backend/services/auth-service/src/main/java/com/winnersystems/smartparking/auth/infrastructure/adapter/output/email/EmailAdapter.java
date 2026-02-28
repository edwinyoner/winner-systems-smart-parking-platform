package com.winnersystems.smartparking.auth.infrastructure.adapter.output.email;

import com.winnersystems.smartparking.auth.application.port.output.EmailPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Set;

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

   @Override
   public void sendWelcomeEmailWithCredentials(
         String toEmail,
         String userName,
         String email,
         String password,
         Set<String> roles,
         String verificationLink,
         int validityHours) {

      String subject = "¡Bienvenido a Smart Parking! - Credenciales de Acceso";

      // ✅ Construir lista HTML de roles
      StringBuilder rolesHtml = new StringBuilder();
      for (String role : roles) {
         String roleDisplayName = getRoleDisplayName(role);
         String roleDescription = getRoleDescription(role);

         rolesHtml.append("""
            <div class="role-badge">
                <div class="role-name">🔹 %s</div>
                <div class="role-desc">%s</div>
            </div>
            """.formatted(roleDisplayName, roleDescription));
      }

      String body = """
         <!DOCTYPE html>
         <html>
         <head>
             <style>
                 body { 
                     font-family: Arial, sans-serif; 
                     line-height: 1.6; 
                     color: #333;
                 }
                 .container { 
                     max-width: 600px; 
                     margin: 0 auto; 
                     padding: 20px; 
                 }
                 .header { 
                     background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                     color: white; 
                     padding: 30px 20px; 
                     text-align: center; 
                     border-radius: 8px 8px 0 0;
                 }
                 .header h1 {
                     margin: 0;
                     font-size: 28px;
                 }
                 .content { 
                     padding: 30px 20px; 
                     background-color: #f9f9f9; 
                     border-radius: 0 0 8px 8px;
                 }
                 .credentials-box {
                     background: white;
                     border: 2px solid #667eea;
                     border-radius: 8px;
                     padding: 20px;
                     margin: 20px 0;
                 }
                 .credential-item {
                     margin: 10px 0;
                     padding: 10px;
                     background: #f0f0f0;
                     border-radius: 4px;
                 }
                 .credential-label {
                     font-weight: bold;
                     color: #667eea;
                     display: block;
                     margin-bottom: 5px;
                 }
                 .credential-value {
                     font-size: 16px;
                     color: #333;
                     font-family: monospace;
                     word-break: break-all;
                 }
                 .roles-box {
                     background: white;
                     border: 2px solid #28a745;
                     border-radius: 8px;
                     padding: 20px;
                     margin: 20px 0;
                 }
                 .role-badge {
                     background: #e8f5e9;
                     border-left: 4px solid #28a745;
                     padding: 12px;
                     margin: 10px 0;
                     border-radius: 4px;
                 }
                 .role-name {
                     font-weight: bold;
                     color: #28a745;
                     font-size: 16px;
                     margin-bottom: 5px;
                 }
                 .role-desc {
                     color: #666;
                     font-size: 14px;
                 }
                 .button { 
                     display: inline-block; 
                     padding: 15px 30px; 
                     background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                     color: white !important; 
                     text-decoration: none; 
                     border-radius: 8px;
                     font-weight: bold;
                     margin: 20px 0;
                 }
                 .button:hover {
                     opacity: 0.9;
                 }
                 .warning-box {
                     background: #fff3cd;
                     border: 1px solid #ffc107;
                     border-radius: 4px;
                     padding: 15px;
                     margin: 20px 0;
                 }
                 .info-box {
                     background: #d1ecf1;
                     border: 1px solid #17a2b8;
                     border-radius: 4px;
                     padding: 15px;
                     margin: 20px 0;
                 }
                 .link-box {
                     background: #e9ecef;
                     padding: 15px;
                     border-radius: 4px;
                     margin-top: 15px;
                     word-break: break-all;
                 }
                 .footer {
                     text-align: center;
                     margin-top: 30px;
                     color: #666;
                     font-size: 12px;
                 }
             </style>
         </head>
         <body>
             <div class="container">
                 <div class="header">
                     <h1>Smart Parking</h1>
                     <p style="margin: 10px 0 0 0;">Sistema de Gestión Municipal</p>
                 </div>
                 
                 <div class="content">
                     <h2 style="color: #667eea;">¡Hola %s!</h2>
                     
                     <p>Bienvenido a <strong>Smart Parking</strong>. Se ha creado una cuenta para ti en nuestro sistema.</p>
                     
                     <div class="credentials-box">
                         <h3 style="margin-top: 0; color: #667eea;">
                             🔐 Tus Credenciales de Acceso
                         </h3>
                         
                         <div class="credential-item">
                             <span class="credential-label">📧 Usuario (Email):</span>
                             <span class="credential-value">%s</span>
                         </div>
                         
                         <div class="credential-item">
                             <span class="credential-label">🔑 Contraseña Temporal:</span>
                             <span class="credential-value">%s</span>
                         </div>
                     </div>
                     
                     <div class="roles-box">
                         <h3 style="margin-top: 0; color: #28a745;">
                             👤 Roles Asignados
                         </h3>
                         <p style="margin-bottom: 15px; color: #666;">
                             Puedes iniciar sesión con cualquiera de estos roles:
                         </p>
                         %s
                     </div>
                     
                     <div class="warning-box">
                         <strong>⚠️ Importante:</strong>
                         <ul style="margin: 10px 0 0 0; padding-left: 20px;">
                             <li>Esta es una contraseña temporal generada por el administrador</li>
                             <li>Por seguridad, te recomendamos cambiarla después del primer inicio de sesión</li>
                             <li>Guarda estas credenciales en un lugar seguro</li>
                         </ul>
                     </div>
                     
                     <div class="info-box">
                         <strong>ℹ️ Selector de Roles:</strong>
                         <p style="margin: 10px 0 0 0;">
                             En la pantalla de inicio de sesión podrás seleccionar con qué rol deseas acceder. 
                             Cada rol tiene diferentes permisos y acceso a diferentes secciones del sistema.
                         </p>
                     </div>
                     
                     <p><strong>Paso 1:</strong> Verifica tu correo electrónico haciendo clic en el siguiente botón:</p>
                     
                     <div style="text-align: center;">
                         <a href="%s" class="button">
                             ✅ Verificar Email
                         </a>
                     </div>
                     
                     <p><strong>Paso 2:</strong> Una vez verificado, podrás iniciar sesión en:</p>
                     <p style="text-align: center;">
                         <a href="http://localhost:4200/login" style="color: #667eea; font-weight: bold;">
                             http://localhost:4200/login
                         </a>
                     </p>
                     
                     <p style="margin-top: 20px;"><small>O copia y pega este enlace de verificación en tu navegador:</small></p>
                     <div class="link-box">
                         <small>%s</small>
                     </div>
                     
                     <p style="margin-top: 20px;">
                         <small>⏱️ Este enlace expira en <strong>%d horas</strong>.</small>
                     </p>
                     
                     <div class="footer">
                         <p>Este es un correo automático, por favor no respondas.</p>
                         <p style="margin-top: 10px;">
                             © 2026 Smart Parking - Winner Systems Corporation<br>
                             Sistema de Gestión de Estacionamiento Inteligente
                         </p>
                     </div>
                 </div>
             </div>
         </body>
         </html>
         """.formatted(
            userName,           // Saludo
            email,              // Usuario (Email)
            password,           // Contraseña temporal
            rolesHtml.toString(), // ✅ Roles HTML
            verificationLink,   // Botón de verificación
            verificationLink,   // Link manual
            validityHours       // Horas de validez
      );

      sendHtmlEmail(toEmail, subject, body);
   }

   // ✅ Métodos auxiliares para nombres y descripciones
   private String getRoleDisplayName(String role) {
      return switch (role) {
         case "ADMIN" -> "Administrador";
         case "AUTORIDAD" -> "Autoridad Municipal";
         case "OPERADOR" -> "Operador";
         default -> role;
      };
   }

   private String getRoleDescription(String role) {
      return switch (role) {
         case "ADMIN" -> "Acceso completo al sistema Smart Parking";
         case "AUTORIDAD" -> "Gestión de zonas, tarifas, reportes y configuración municipal";
         case "OPERADOR" -> "Operación diaria del sistema de estacionamiento";
         default -> "Rol personalizado";
      };
   }
}