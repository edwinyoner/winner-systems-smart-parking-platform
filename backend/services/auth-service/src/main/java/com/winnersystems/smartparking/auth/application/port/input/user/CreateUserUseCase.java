package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.command.CreateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: Crear usuario interno.
 *
 * <p>Este caso de uso permite registrar usuarios del staff interno del
 * sistema Smart Parking. Los tipos de usuario interno incluyen:</p>
 *
 * <ul>
 *   <li>ADMIN – Administradores del sistema</li>
 *   <li>AUTORIDAD – Personal de supervisión</li>
 *   <li>OPERADOR – Personal operativo</li>
 * </ul>
 *
 * <p><b>Nota:</b> Los ciudadanos (usuarios finales de la app móvil)
 * se autentican mediante Google OAuth y son administrados
 * en un servicio independiente.</p>
 *
 * <h3>Autorización requerida</h3>
 * <ul>
 *   <li>Rol: <b>ADMIN</b></li>
 *   <li>Permiso: <b>users.create</b></li>
 * </ul>
 *
 * <h3>Funcionalidades incluidas</h3>
 * <ul>
 *   <li>Validación de captcha anti-bot</li>
 *   <li>Verificación de unicidad del email</li>
 *   <li>Protección de contraseña con BCrypt</li>
 *   <li>Asignación de roles</li>
 *   <li>Generación de token de verificación (24 horas)</li>
 *   <li>Envío de email de bienvenida</li>
 *   <li>Registro de auditoría</li>
 * </ul>
 *
 * <h3>Estado inicial del usuario creado</h3>
 * <ul>
 *   <li><b>status:</b> false (inactivo hasta verificar email)</li>
 *   <li><b>emailVerified:</b> false</li>
 *   <li><b>deleted:</b> false</li>
 * </ul>
 *
 * <h3>Excepciones</h3>
 * <ul>
 *   <li><b>EmailAlreadyExistsException</b> – email ya registrado</li>
 *   <li><b>InvalidCaptchaException</b> – captcha inválido</li>
 *   <li><b>RoleNotAssignedException</b> – rol no existe o inactivo</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CreateUserUseCase {
   /**
    * Ejecuta el proceso de creación de un usuario interno.
    *
    * @param command datos necesarios para crear el usuario
    * @return datos del usuario recién creado
    */
   UserDto execute(CreateUserCommand command);
}