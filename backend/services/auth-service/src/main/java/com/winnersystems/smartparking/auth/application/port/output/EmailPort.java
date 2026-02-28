package com.winnersystems.smartparking.auth.application.port.output;

import java.util.Set;

/**
 * Puerto de salida para envío de emails.
 *
 * <p>Maneja el envío de notificaciones por email para el sistema Smart Parking.
 * Cada método representa un tipo específico de email del negocio.</p>
 *
 * <h3>Implementación recomendada</h3>
 * <ul>
 *   <li>SMTP tradicional (JavaMail, Spring Mail)</li>
 *   <li>SendGrid, AWS SES, Mailgun (proveedores cloud)</li>
 *   <li>Templates HTML responsive con branding</li>
 *   <li>Soporte para español (sistema peruano)</li>
 *   <li>Retry logic para fallos temporales</li>
 *   <li>Logging de emails enviados (auditoría)</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface EmailPort {

   // ========== AUTENTICACIÓN Y VERIFICACIÓN ==========

   /**
    * Envía email de bienvenida con link de verificación.
    *
    * <p>Usado en CreateUserUseCase al crear nueva cuenta.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param verificationLink link completo (https://app.com/verify?token=xyz)
    * @param validityHours validez del link (típicamente 24)
    */
   void sendWelcomeEmail(String toEmail, String userName, String verificationLink, int validityHours);

   /**
    * Envía email de verificación (sin validityHours).
    *
    * <p>Sobrecarga simplificada para usar validez por defecto (24 horas).</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param verificationLink link completo de verificación
    */
   void sendVerificationEmail(String toEmail, String userName, String verificationLink);

   /**
    * Envía email de verificación (con validityHours).
    *
    * <p>Usado cuando usuario solicita reenviar verificación.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param verificationLink link completo de verificación
    * @param validityHours validez del link
    */
   void sendVerificationEmail(String toEmail, String userName, String verificationLink, int validityHours);

   /**
    * Envía confirmación de email verificado exitosamente.
    *
    * <p>Usado en VerifyEmailUseCase después del clic en link.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    */
   void sendEmailVerifiedConfirmation(String toEmail, String userName);

   // ========== RECUPERACIÓN DE CONTRASEÑA ==========

   /**
    * Envía email para restablecer contraseña.
    *
    * <p>Usado en ForgotPasswordUseCase. Incluye link con token
    * para crear nueva contraseña.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param resetLink link completo (https://app.com/reset?token=abc)
    * @param validityHours validez del link (típicamente 1)
    */
   void sendPasswordResetEmail(String toEmail, String userName, String resetLink, int validityHours);

   /**
    * Envía confirmación de contraseña cambiada exitosamente.
    *
    * <p>Usado en ResetPasswordUseCase. Notifica al usuario
    * que su contraseña fue modificada (seguridad).</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param changedAt fecha y hora del cambio (formato legible)
    */
   void sendPasswordChangedEmail(String toEmail, String userName, String changedAt);

   // ========== GESTIÓN DE CUENTA ==========

   /**
    * Envía notificación de cuenta desactivada.
    *
    * <p>Usado en DeleteUserUseCase (soft delete). Notifica que
    * su cuenta fue desactivada por un administrador.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param reason razón de la desactivación (opcional)
    */
   void sendAccountDeactivatedEmail(String toEmail, String userName, String reason);

   /**
    * Envía notificación de cuenta reactivada.
    *
    * <p>Usado en RestoreUserUseCase. Notifica que su cuenta
    * fue reactivada.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    */
   void sendAccountActivatedEmail(String toEmail, String userName);

   /**
    * Envía notificación de nuevo rol asignado.
    *
    * <p>Usado en UpdateUserUseCase cuando se asignan roles.
    * Notifica que se le asignaron nuevos permisos.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param roleName nombre del rol asignado
    * @param assignedBy nombre del administrador que asignó el rol
    */
   void sendRoleAssignedEmail(String toEmail, String userName, String roleName, String assignedBy);

   /**
    * Envía notificación de rol removido.
    *
    * <p>Usado en UpdateUserUseCase cuando se remueven roles.
    * Notifica que se le removieron permisos.</p>
    *
    * @param toEmail email destino
    * @param userName nombre completo del usuario
    * @param roleName nombre del rol removido
    * @param removedBy nombre del administrador que removió el rol
    */
   void sendRoleRemovedEmail(String toEmail, String userName, String roleName, String removedBy);

   // ========== EMAIL GENÉRICO ==========

   /**
    * Envía un email genérico con HTML.
    *
    * <p>Para casos especiales no cubiertos por los métodos específicos.</p>
    *
    * @param toEmail email destino
    * @param subject asunto del email
    * @param htmlBody cuerpo del email en formato HTML
    */
   void sendHtmlEmail(String toEmail, String subject, String htmlBody);

   /**
    * Envía email de bienvenida CON credenciales de acceso.
    * Usado cuando un ADMIN crea un usuario nuevo.
    */
   void sendWelcomeEmailWithCredentials(
         String toEmail,
         String userName,
         String email,
         String password,
         Set<String> roles,
         String verificationLink,
         int validityHours
   );
}