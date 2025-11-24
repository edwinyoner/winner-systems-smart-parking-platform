package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.advice;

import com.winnersystems.smartparking.auth.domain.exception.*;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones.
 *
 * Captura TODAS las excepciones lanzadas por los controllers y las convierte
 * en respuestas HTTP apropiadas.
 *
 * Sin esto, el cliente recibiría mensajes de error genéricos del servidor.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

   /**
    * Usuario no encontrado → 404 NOT FOUND
    */
   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Usuario no encontrado",
            ex.getMessage()
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
   }

   /**
    * Credenciales inválidas → 401 UNAUTHORIZED
    */
   @ExceptionHandler(InvalidCredentialsException.class)
   public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Credenciales inválidas",
            ex.getMessage()
      );
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
   }

   /**
    * Email ya existe → 409 CONFLICT
    */
   @ExceptionHandler(EmailAlreadyExistsException.class)
   public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Email ya registrado",
            ex.getMessage()
      );
      return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
   }

   /**
    * Token expirado → 401 UNAUTHORIZED
    */
   @ExceptionHandler(TokenExpiredException.class)
   public ResponseEntity<ErrorResponse> handleTokenExpired(TokenExpiredException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Token expirado",
            ex.getMessage()
      );
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
   }

   /**
    * CAPTCHA inválido → 400 BAD REQUEST
    */
   @ExceptionHandler(InvalidCaptchaException.class)
   public ResponseEntity<ErrorResponse> handleInvalidCaptcha(InvalidCaptchaException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Verificación CAPTCHA fallida",
            ex.getMessage()
      );
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   /**
    * Rol no asignado → 403 FORBIDDEN
    */
   @ExceptionHandler(RoleNotAssignedException.class)
   public ResponseEntity<ErrorResponse> handleRoleNotAssigned(RoleNotAssignedException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Rol no asignado",
            ex.getMessage()
      );
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
   }

   /**
    * Validación de campos (Bean Validation) → 400 BAD REQUEST
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
         MethodArgumentNotValidException ex) {

      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach(error -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });

      ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error de validación",
            errors
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
   }

   /**
    * Cualquier otra excepción → 500 INTERNAL SERVER ERROR
    */
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error interno del servidor",
            "Ha ocurrido un error inesperado"
      );

      // Log del error para debugging
      ex.printStackTrace();

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
   }

   // ========== RECORDS PARA RESPUESTAS DE ERROR ==========

   /**
    * Respuesta de error estándar
    */
   public record ErrorResponse(
         int status,
         String error,
         String message
   ) {}

   /**
    * Respuesta de error con detalles de validación
    */
   public record ValidationErrorResponse(
         int status,
         String error,
         Map<String, String> validationErrors
   ) {}
}