package com.winnersystems.smartparking.auth.application.port.input.user;

import com.winnersystems.smartparking.auth.application.dto.command.CreateUserCommand;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;

/**
 * Caso de uso: CREAR USUARIO
 *
 * Puede ser usado para:
 * - Registro de usuarios (auto-registro)
 * - Creación de usuarios por admin
 */
public interface CreateUserUseCase {

   /**
    * Crea un nuevo usuario en el sistema
    *
    * Flujo:
    * 1. Validar captcha (si aplica)
    * 2. Validar que el email NO exista
    * 3. Encriptar la contraseña
    * 4. Crear entidad User
    * 5. Asignar roles
    * 6. Guardar en BD
    * 7. Generar token de verificación de email
    * 8. Enviar email de bienvenida
    * 9. Retornar UserDto
    *
    * @param command datos del nuevo usuario
    * @return usuario creado
    * @throws EmailAlreadyExistsException si el email ya existe
    */
   UserDto execute(CreateUserCommand command);
}