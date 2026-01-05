package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.advice;

import com.winnersystems.smartparking.auth.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones REST.
 *
 * <p>Captura TODAS las excepciones lanzadas por los controllers y las convierte
 * en respuestas HTTP apropiadas con formato JSON consistente.</p>
 *
 * <p><b>Ventajas:</b></p>
 * <ul>
 *   <li>Respuestas de error consistentes en toda la API</li>
 *   <li>No expone stack traces al cliente</li>
 *   <li>Logging centralizado de errores</li>
 *   <li>Códigos HTTP semánticos correctos</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

   /**
    * Usuario no encontrado → 404 NOT FOUND
    */
   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<ErrorResponse> handleUserNotFound(
         UserNotFoundException ex,
         HttpServletRequest request) {

      log.warn("Usuario no encontrado: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Usuario no encontrado",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
   }

   /**
    * Credenciales inválidas → 401 UNAUTHORIZED
    */
   @ExceptionHandler(InvalidCredentialsException.class)
   public ResponseEntity<ErrorResponse> handleInvalidCredentials(
         InvalidCredentialsException ex,
         HttpServletRequest request) {

      log.warn("Intento de login fallido: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Credenciales inválidas",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
   }

   /**
    * Email ya existe → 409 CONFLICT
    */
   @ExceptionHandler(EmailAlreadyExistsException.class)
   public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
         EmailAlreadyExistsException ex,
         HttpServletRequest request) {

      log.warn("Email duplicado: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Email ya registrado",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
   }

   /**
    * Token expirado → 401 UNAUTHORIZED
    */
   @ExceptionHandler(TokenExpiredException.class)
   public ResponseEntity<ErrorResponse> handleTokenExpired(
         TokenExpiredException ex,
         HttpServletRequest request) {

      log.warn("Token expirado: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Token expirado",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
   }

   /**
    * CAPTCHA inválido → 400 BAD REQUEST
    */
   @ExceptionHandler(InvalidCaptchaException.class)
   public ResponseEntity<ErrorResponse> handleInvalidCaptcha(
         InvalidCaptchaException ex,
         HttpServletRequest request) {

      log.warn("Verificación CAPTCHA fallida: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Verificación CAPTCHA fallida",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   /**
    * Rol no asignado → 403 FORBIDDEN
    */
   @ExceptionHandler(RoleNotAssignedException.class)
   public ResponseEntity<ErrorResponse> handleRoleNotAssigned(
         RoleNotAssignedException ex,
         HttpServletRequest request) {

      log.warn("Rol no asignado: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Rol no asignado",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
   }

   /**
    * Argumento inválido → 400 BAD REQUEST
    * Usado en validaciones de negocio (ej: "Rol no encontrado: XYZ")
    */
   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<ErrorResponse> handleIllegalArgument(
         IllegalArgumentException ex,
         HttpServletRequest request) {

      log.warn("Argumento inválido: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Solicitud inválida",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   /**
    * Estado inválido → 409 CONFLICT
    * Usado en validaciones de estado (ej: "Usuario ya eliminado")
    */
   @ExceptionHandler(IllegalStateException.class)
   public ResponseEntity<ErrorResponse> handleIllegalState(
         IllegalStateException ex,
         HttpServletRequest request) {

      log.warn("Estado inválido: {}", ex.getMessage());

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Estado inválido",
            ex.getMessage(),
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
   }

   /**
    * Validación de campos (Bean Validation) → 400 BAD REQUEST
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
         MethodArgumentNotValidException ex,
         HttpServletRequest request) {

      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach(error -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });

      log.warn("Errores de validación en {}: {}", request.getRequestURI(), errors);

      ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Error de validación",
            errors,
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
   }

   /**
    * Cualquier otra excepción → 500 INTERNAL SERVER ERROR
    */
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleGenericException(
         Exception ex,
         HttpServletRequest request) {

      log.error("Error inesperado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);

      ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error interno del servidor",
            "Ha ocurrido un error inesperado. Por favor, contacte al administrador.",
            request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
   }

   // ========== RECORDS PARA RESPUESTAS DE ERROR ==========

   /**
    * Respuesta de error estándar.
    *
    * @param timestamp Fecha/hora del error
    * @param status Código HTTP
    * @param error Título del error
    * @param message Mensaje detallado
    * @param path Ruta del endpoint que falló
    */
   public record ErrorResponse(
         LocalDateTime timestamp,
         int status,
         String error,
         String message,
         String path
   ) {}

   /**
    * Respuesta de error con detalles de validación.
    *
    * @param timestamp Fecha/hora del error
    * @param status Código HTTP
    * @param error Título del error
    * @param validationErrors Mapa de errores por campo
    * @param path Ruta del endpoint que falló
    */
   public record ValidationErrorResponse(
         LocalDateTime timestamp,
         int status,
         String error,
         Map<String, String> validationErrors,
         String path
   ) {}
}