package com.winnersystems.smartparking.auth.infrastructure.config;

import com.winnersystems.smartparking.auth.application.port.output.EmailPort;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.repository.PermissionRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.repository.RoleRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity.VerificationTokenEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.repository.VerificationTokenRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Data Seeder - Inicializa datos por defecto en la base de datos.
 *
 * <p>Se ejecuta automáticamente al iniciar la aplicación (CommandLineRunner).</p>
 *
 * <p><b>IMPORTANTE:</b> Usa JPA Entities directamente (no modelos de dominio)
 * porque está en la capa de Infrastructure.</p>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

   private final PermissionRepository permissionRepository;
   private final RoleRepository roleRepository;
   private final UserRepository userRepository;
   private final VerificationTokenRepository verificationTokenRepository;
   private final PasswordEncoder passwordEncoder;
   private final EmailPort emailPort;

   @Override
   @Transactional
   public void run(String... args) {
      log.info("========================================");
      log.info("Iniciando Data Seeder...");
      log.info("========================================");

      seedPermissions();
      seedRoles();
      seedAdminUser();

      log.info("========================================");
      log.info("Data Seeder completado exitosamente");
      log.info("========================================");
   }

   /**
    * Crea los permisos del sistema si no existen.
    */
   private void seedPermissions() {
      if (permissionRepository.count() > 0) {
         log.info("Permisos ya existen. Saltando...");
         return;
      }

      log.info("Creando permisos...");

      String[][] permissions = {
            // ========== GESTIÓN DE USUARIOS ==========
            {"users.create", "Crear nuevos usuarios en el sistema"},
            {"users.read", "Ver información de usuarios"},
            {"users.update", "Actualizar información de usuarios"},
            {"users.delete", "Eliminar usuarios del sistema"},
            {"users.restore", "Restaurar usuarios eliminados"},

            // ========== GESTIÓN DE ROLES ==========
            {"roles.create", "Crear nuevos roles"},
            {"roles.read", "Ver información de roles"},
            {"roles.update", "Actualizar roles existentes"},
            {"roles.delete", "Eliminar roles del sistema"},

            // ========== GESTIÓN DE PERMISOS ==========
            {"permissions.create", "Crear nuevos permisos"},
            {"permissions.read", "Ver información de permisos"},
            {"permissions.update", "Actualizar permisos existentes"},
            {"permissions.delete", "Eliminar permisos del sistema"},

            // ========== GESTIÓN DE ESTACIONAMIENTOS ==========

            // Turnos (Shifts)
            {"shifts.create", "Crear nuevos turnos de trabajo"},
            {"shifts.read", "Ver información de turnos"},
            {"shifts.update", "Actualizar turnos existentes"},
            {"shifts.delete", "Eliminar turnos"},

            // Zonas de Estacionamiento
            {"zones.create", "Crear nuevas zonas de estacionamiento"},
            {"zones.read", "Ver información de zonas"},
            {"zones.update", "Actualizar zonas existentes"},
            {"zones.delete", "Eliminar zonas"},
            {"zones.activate", "Activar/Desactivar zonas"},

            // Tipos de Espacio
            {"space-types.create", "Crear nuevos tipos de espacio"},
            {"space-types.read", "Ver tipos de espacio"},
            {"space-types.update", "Actualizar tipos de espacio"},
            {"space-types.delete", "Eliminar tipos de espacio"},

            // Espacios de Estacionamiento
            {"spaces.create", "Crear nuevos espacios"},
            {"spaces.read", "Ver información de espacios"},
            {"spaces.update", "Actualizar espacios existentes"},
            {"spaces.delete", "Eliminar espacios"},
            {"spaces.bulk-create", "Creación masiva de espacios"},

            // Tipos de Documento
            {"document-types.create", "Crear nuevos tipos de documento"},
            {"document-types.read", "Ver tipos de documento"},
            {"document-types.update", "Actualizar tipos de documento"},
            {"document-types.delete", "Eliminar tipos de documento"},

            // Clientes
            {"customers.create", "Crear nuevos clientes"},
            {"customers.read", "Ver información de clientes"},
            {"customers.update", "Actualizar información de clientes"},
            {"customers.delete", "Eliminar clientes"},

            // Vehículos
            {"vehicles.create", "Registrar nuevos vehículos"},
            {"vehicles.read", "Ver información de vehículos"},
            {"vehicles.update", "Actualizar información de vehículos"},
            {"vehicles.delete", "Eliminar vehículos"},

            // Transacciones
            {"transactions.entry", "Registrar entrada de vehículos"},
            {"transactions.exit", "Registrar salida de vehículos"},
            {"transactions.view", "Ver transacciones del sistema"},
            {"transactions.cancel", "Cancelar transacciones"},

            // Tipos de Pago
            {"payment-types.create", "Crear nuevos tipos de pago"},
            {"payment-types.read", "Ver tipos de pago"},
            {"payment-types.update", "Actualizar tipos de pago"},
            {"payment-types.delete", "Eliminar tipos de pago"},

            // Pagos
            {"payments.process", "Procesar pagos de estacionamiento"},
            {"payments.view", "Ver información de pagos"},
            {"payments.refund", "Realizar reembolsos"},
            {"payments.cancel", "Cancelar pagos"},

            // Infracciones
            {"infractions.create", "Crear nuevas infracciones"},
            {"infractions.read", "Ver infracciones registradas"},
            {"infractions.update", "Actualizar infracciones"},
            {"infractions.delete", "Eliminar infracciones"},
            {"infractions.resolve", "Resolver infracciones"},

            // ========== GESTIÓN DE TARIFAS ==========
            {"rates.create", "Crear nuevas tarifas"},
            {"rates.read", "Ver tarifas configuradas"},
            {"rates.update", "Actualizar tarifas existentes"},
            {"rates.delete", "Eliminar tarifas"},

            // ========== REPORTES ==========
            {"reports.view", "Ver reportes y estadísticas del sistema"},
            {"reports.export", "Exportar reportes (PDF, Excel)"}
      };

      for (String[] perm : permissions) {
         PermissionEntity permission = PermissionEntity.builder()
               .name(perm[0])
               .description(perm[1])
               .status(true)
               .build();

         permissionRepository.save(permission);
      }

      log.info("{} permisos creados", permissions.length);
   }

   /**
    * Crea los 3 roles del sistema con sus permisos.
    */
   private void seedRoles() {
      if (roleRepository.count() > 0) {
         log.info("Roles ya existen. Saltando...");
         return;
      }

      log.info("Creando roles...");

      // ========== ROL: ADMIN ==========
      RoleEntity adminRole = RoleEntity.builder()
            .name("ADMIN")
            .description("Acceso completo al sistema Smart Parking")
            .status(true)
            .permissions(getAllPermissions())
            .build();

      roleRepository.save(adminRole);
      log.info("Rol ADMIN creado con {} permisos (TODOS)", adminRole.getPermissions().size());

      // ========== ROL: AUTORIDAD ==========
      RoleEntity autoridadRole = RoleEntity.builder()
            .name("AUTORIDAD")
            .description("Gestión de usuarios, zonas, tarifas y reportes municipales")
            .status(true)
            .permissions(getPermissionsByNames(
                  // Gestión de usuarios (crear, leer, actualizar - SIN eliminar)
                  "users.create", "users.read", "users.update",
                  // Gestión de roles (solo lectura)
                  "roles.read",
                  // Gestión de permisos (solo lectura)
                  "permissions.read",

                  // Gestión de Turnos
                  "shifts.create", "shifts.read", "shifts.update", "shifts.delete",
                  // Gestión de Zonas
                  "zones.create", "zones.read", "zones.update", "zones.delete", "zones.activate",
                  // Gestión de Tipos de Espacio
                  "space-types.create", "space-types.read", "space-types.update", "space-types.delete",
                  // Gestión de Espacios
                  "spaces.create", "spaces.read", "spaces.update", "spaces.delete", "spaces.bulk-create",
                  // Gestión de Tipos de Documento
                  "document-types.create", "document-types.read", "document-types.update", "document-types.delete",
                  // Gestión de Clientes
                  "customers.create", "customers.read", "customers.update", "customers.delete",
                  // Gestión de Vehículos
                  "vehicles.create", "vehicles.read", "vehicles.update", "vehicles.delete",
                  // Ver transacciones (sin registrar)
                  "transactions.view",
                  // Gestión de Tipos de Pago
                  "payment-types.create", "payment-types.read", "payment-types.update", "payment-types.delete",
                  // Ver pagos
                  "payments.view",
                  // Gestión de Infracciones
                  "infractions.create", "infractions.read", "infractions.update", "infractions.delete", "infractions.resolve",
                  // Gestión completa de tarifas
                  "rates.create", "rates.read", "rates.update", "rates.delete",
                  // Reportes
                  "reports.view", "reports.export"
            ))
            .build();

      roleRepository.save(autoridadRole);
      log.info("Rol AUTORIDAD creado con {} permisos", autoridadRole.getPermissions().size());

      // ========== ROL: OPERADOR ==========
      RoleEntity operadorRole = RoleEntity.builder()
            .name("OPERADOR")
            .description("Operación diaria del sistema de estacionamiento")
            .status(true)
            .permissions(getPermissionsByNames(
                  // Solo lectura de usuarios (ver su perfil)
                  "users.read",

                  // Ver información de zonas y espacios
                  "zones.read",
                  "spaces.read",
                  "space-types.read",
                  // Ver tipos de documento
                  "document-types.read",
                  // Gestión de clientes
                  "customers.create", "customers.read", "customers.update",
                  // Gestión de vehículos
                  "vehicles.create", "vehicles.read", "vehicles.update",
                  // Operaciones de transacciones
                  "transactions.entry", "transactions.exit", "transactions.view",
                  // Ver tipos de pago
                  "payment-types.read",
                  // Procesar pagos
                  "payments.process", "payments.view",
                  // Ver infracciones
                  "infractions.read",
                  // Solo lectura de tarifas
                  "rates.read",
                  // Ver reportes (sin exportar)
                  "reports.view"
            ))
            .build();

      roleRepository.save(operadorRole);
      log.info("Rol OPERADOR creado con {} permisos", operadorRole.getPermissions().size());
   }

   /**
    * Crea el usuario admin por defecto CON LOS 3 ROLES.
    */
   private void seedAdminUser() {
      if (userRepository.existsByEmail("edwinyoner@gmail.com")) {
         log.info("Usuario admin ya existe. Saltando...");
         return;
      }

      log.info("Creando usuario admin con los 3 roles...");

      // Obtener los 3 roles
      RoleEntity adminRole = roleRepository.findByName("ADMIN")
            .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

      RoleEntity autoridadRole = roleRepository.findByName("AUTORIDAD")
            .orElseThrow(() -> new RuntimeException("Rol AUTORIDAD no encontrado"));

      RoleEntity operadorRole = roleRepository.findByName("OPERADOR")
            .orElseThrow(() -> new RuntimeException("Rol OPERADOR no encontrado"));

      String tempPassword = "Admin123!";

      // Crear usuario con los 3 roles
      UserEntity admin = UserEntity.builder()
            .firstName("Admin")
            .lastName("System")
            .email("edwinyoner@gmail.com")
            .password(passwordEncoder.encode(tempPassword))
            .phoneNumber("+51987654321")
            .status(true)
            .emailVerified(false)
            .roles(Set.of(adminRole, autoridadRole, operadorRole))
            .build();

      userRepository.save(admin);

      log.info("Usuario admin creado con {} roles", admin.getRoles().size());
      log.info("   - Email: edwinyoner@gmail.com");
      log.info("   - Password: {}", tempPassword);
      log.info("   - Roles: ADMIN, AUTORIDAD, OPERADOR");
      log.info("  IMPORTANTE: Verificar email antes del primer login");

      // GENERAR TOKEN Y ENVIAR EMAIL DE VERIFICACIÓN
      try {
         String verificationToken = UUID.randomUUID().toString();

         VerificationTokenEntity tokenEntity = VerificationTokenEntity.builder()
               .token(verificationToken)
               .userId(admin.getId())
               .expiresAt(LocalDateTime.now().plusHours(24))
               .build();

         verificationTokenRepository.save(tokenEntity);

         String verificationLink = "http://192.168.1.4:4200/verify-email?token=" + verificationToken;

         sendAdminWelcomeEmailWithVerification(admin, tempPassword, verificationLink);

         log.info("Email de verificación enviado a: {}", admin.getEmail());
         log.info("Token: {}", verificationToken);
         log.info("Expira en: 24 horas");
      } catch (Exception e) {
         log.error("Error al enviar email de verificación: {}", e.getMessage());
         log.warn("El usuario fue creado pero el email no pudo enviarse");
      }
   }

   // ========== HELPERS ==========

   /**
    * Obtiene todos los permisos del sistema.
    */
   private Set<PermissionEntity> getAllPermissions() {
      return new HashSet<>(permissionRepository.findAll());
   }

   /**
    * Obtiene permisos por sus nombres.
    */
   private Set<PermissionEntity> getPermissionsByNames(String... names) {
      Set<PermissionEntity> permissions = new HashSet<>();
      for (String name : names) {
         permissionRepository.findByName(name)
               .ifPresent(permissions::add);
      }
      return permissions;
   }

   /**
    * Envía email de bienvenida con credenciales Y verificación.
    */
   private void sendAdminWelcomeEmailWithVerification(UserEntity user, String tempPassword, String verificationLink) {
      String subject = "Bienvenido a Smart Parking - Verifica tu Email";

      String htmlBody = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { 
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
                        line-height: 1.6;
                        color: #333;
                        margin: 0;
                        padding: 0;
                        background-color: #f4f4f4;
                    }
                    .email-wrapper {
                        padding: 20px;
                    }
                    .container { 
                        max-width: 600px; 
                        margin: 0 auto; 
                        background: #ffffff;
                        border-radius: 10px;
                        overflow: hidden;
                        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                    }
                    .header { 
                        background: linear-gradient(135deg, #4CAF50 0%%, #45a049 100%%);
                        color: white; 
                        padding: 30px 20px; 
                        text-align: center;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 28px;
                    }
                    .header p {
                        margin: 5px 0 0 0;
                        opacity: 0.9;
                    }
                    .content { 
                        padding: 30px 20px; 
                    }
                    .verification-box {
                        background: #f0f8f0;
                        padding: 20px;
                        margin: 20px 0;
                        border-left: 4px solid #4CAF50;
                        border-radius: 5px;
                        text-align: center;
                    }
                    .verification-box h3 {
                        margin-top: 0;
                        color: #4CAF50;
                    }
                    .credentials { 
                        background: #f8f9fa; 
                        padding: 20px; 
                        margin: 20px 0; 
                        border-left: 4px solid #2196F3;
                        border-radius: 5px;
                    }
                    .credentials h3 {
                        margin-top: 0;
                        color: #2196F3;
                    }
                    .credentials p {
                        margin: 10px 0;
                    }
                    .credentials strong {
                        color: #1976D2;
                    }
                    .password-box {
                        background: white;
                        padding: 10px 15px;
                        border: 2px dashed #2196F3;
                        border-radius: 5px;
                        font-family: 'Courier New', monospace;
                        font-size: 18px;
                        font-weight: bold;
                        color: #1976D2;
                        text-align: center;
                        margin: 10px 0;
                    }
                    .button {
                        display: inline-block;
                        padding: 14px 35px;
                        background: #4CAF50;
                        color: white !important;
                        text-decoration: none;
                        border-radius: 5px;
                        margin: 15px 0;
                        font-weight: bold;
                        font-size: 16px;
                    }
                    .button:hover {
                        background: #45a049;
                    }
                    .warning { 
                        background: #fff3cd; 
                        padding: 15px; 
                        margin: 20px 0; 
                        border-left: 4px solid #ffc107;
                        border-radius: 5px;
                    }
                    .warning h3 {
                        margin-top: 0;
                        color: #856404;
                    }
                    .warning ul {
                        margin: 10px 0;
                        padding-left: 20px;
                    }
                    .footer {
                        text-align: center;
                        padding: 20px;
                        background: #2d3748;
                        color: white;
                    }
                    .footer p {
                        margin: 5px 0;
                    }
                </style>
            </head>
            <body>
                <div class="email-wrapper">
                    <div class="container">
                        <div class="header">
                            <h1>Smart Parking Platform</h1>
                            <p>Sistema de Gestión de Estacionamiento Inteligente</p>
                        </div>
                        
                        <div class="content">
                            <h2 style="color: #4CAF50;">¡Bienvenido, Administrador!</h2>
                            <p>Hola <strong>%s %s</strong>,</p>
                            <p>Se ha creado exitosamente tu cuenta de <strong>Administrador</strong> en Smart Parking Platform.</p>
                            
                            <div class="verification-box">
                                <h3>Verifica tu correo electrónico</h3>
                                <p>Para activar tu cuenta y garantizar la seguridad, por favor confirma tu dirección de correo haciendo clic en el botón:</p>
                                <a href="%s" class="button">Verificar mi correo</a>
                                <p style="font-size: 12px; margin-top: 15px; color: #666;">
                                    Si el botón no funciona, copia y pega este enlace en tu navegador:<br>
                                    <span style="word-break: break-all; color: #4CAF50;">%s</span>
                                </p>
                                <p style="font-size: 12px; color: #666; margin-top: 10px;">
                                    Este enlace expira en 24 horas.
                                </p>
                            </div>
                            
                            <div class="credentials">
                                <h3>🔐 Tus credenciales de acceso</h3>
                                <p>Una vez verificado tu email, podrás iniciar sesión con:</p>
                                
                                <p style="margin-top: 15px;"><strong>Usuario:</strong></p>
                                <p style="font-size: 16px;">%s</p>
                                
                                <p style="margin-top: 15px;"><strong>Contraseña temporal:</strong></p>
                                <div class="password-box">%s</div>
                                
                                <p style="margin-top: 15px;"><strong>Roles asignados:</strong> Administrador, Autoridad, Operador</p>
                                <p style="font-size: 14px; color: #666;">Tendrás acceso completo al sistema y podrás probar todas las funcionalidades.</p>
                            </div>
                            
                            <div class="warning">
                                <h3>⚠️ IMPORTANTE - Seguridad</h3>
                                <ul>
                                    <li>Primero <strong>verifica tu email</strong> haciendo clic en el botón verde de arriba</li>
                                    <li>Esta es una <strong>contraseña temporal</strong></li>
                                    <li>Debes cambiarla en tu <strong>primer inicio de sesión</strong></li>
                                    <li><strong>No compartas</strong> estas credenciales con nadie</li>
                                    <li>Utiliza una contraseña segura:
                                        <ul>
                                            <li>Mínimo 8 caracteres</li>
                                            <li>Incluye mayúsculas y minúsculas</li>
                                            <li>Incluye números y símbolos</li>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                            
                            <p style="margin-top: 30px; color: #666; font-size: 14px;">
                                Si tienes problemas para acceder o necesitas ayuda, contacta con el equipo de soporte técnico.
                            </p>
                        </div>
                        
                        <div class="footer">
                            <p style="margin: 0; font-weight: bold;">Winner Systems Corporation S.A.C.</p>
                            <p style="opacity: 0.9;">Smart Parking Platform</p>
                            <p style="opacity: 0.8;">Huaraz, Áncash, Perú 🇵🇪</p>
                            <p style="font-size: 12px; opacity: 0.7; margin-top: 10px;">
                                Este es un correo automático, por favor no responder directamente.
                            </p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """,
            user.getFirstName(),
            user.getLastName(),
            verificationLink,
            verificationLink,
            user.getEmail(),
            tempPassword
      );

      emailPort.sendHtmlEmail(user.getEmail(), subject, htmlBody);
   }
}