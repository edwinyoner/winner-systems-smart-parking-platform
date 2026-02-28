package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.advice;

import com.winnersystems.smartparking.parking.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para el servicio de parking.
 *
 * Captura y transforma excepciones en respuestas HTTP apropiadas.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

   // ========================= EXCEPCIONES DE DOMINIO =========================

   /**
    * Maneja excepción cuando un vehículo ya está dentro del estacionamiento.
    */
   @ExceptionHandler(VehicleAlreadyInsideException.class)
   public ResponseEntity<ErrorResponse> handleVehicleAlreadyInside(
         VehicleAlreadyInsideException ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.CONFLICT.value())
            .error("Conflict")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
   }

   /**
    * Maneja excepción cuando un espacio no está disponible.
    */
   @ExceptionHandler(SpaceNotAvailableException.class)
   public ResponseEntity<ErrorResponse> handleSpaceNotAvailable(
         SpaceNotAvailableException ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.CONFLICT.value())
            .error("Conflict")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
   }

   /**
    * Maneja excepción cuando una zona no está operativa.
    */
   @ExceptionHandler(ZoneNotOperationalException.class)
   public ResponseEntity<ErrorResponse> handleZoneNotOperational(
         ZoneNotOperationalException ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   /**
    * Maneja excepción cuando los documentos de entrada/salida no coinciden.
    */
   @ExceptionHandler(DocumentMismatchException.class)
   public ResponseEntity<ErrorResponse> handleDocumentMismatch(
         DocumentMismatchException ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error("Forbidden")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
   }

   /**
    * Maneja excepción cuando una transacción está en un estado inválido.
    */
   @ExceptionHandler(InvalidTransactionStateException.class)
   public ResponseEntity<ErrorResponse> handleInvalidTransactionState(
         InvalidTransactionStateException ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   /**
    * Maneja excepción base del dominio de parking.
    */
   @ExceptionHandler(ParkingDomainException.class)
   public ResponseEntity<ErrorResponse> handleParkingDomainException(
         ParkingDomainException ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   // ========================= EXCEPCIONES DE VALIDACIÓN =========================

   /**
    * Maneja errores de validación de Bean Validation (@Valid).
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
         MethodArgumentNotValidException ex, WebRequest request) {

      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach(error -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });

      ValidationErrorResponse error = ValidationErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message("Los datos enviados no son válidos")
            .path(request.getDescription(false).replace("uri=", ""))
            .errors(errors)
            .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   // ========================= EXCEPCIONES ESTÁNDAR =========================

   /**
    * Maneja excepción cuando no se encuentra un recurso.
    */
   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<ErrorResponse> handleIllegalArgument(
         IllegalArgumentException ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
   }

   /**
    * Maneja cualquier otra excepción no capturada.
    */
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleGlobalException(
         Exception ex, WebRequest request) {

      ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message("Ocurrió un error inesperado. Por favor, contacte al administrador.")
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

      // Log the exception for debugging
      ex.printStackTrace();

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
   }

   // ========================= INNER CLASSES - ERROR RESPONSES =========================

   /**
    * Clase para respuestas de error estándar.
    */
   @lombok.Data
   @lombok.Builder
   @lombok.NoArgsConstructor
   @lombok.AllArgsConstructor
   public static class ErrorResponse {
      private LocalDateTime timestamp;
      private Integer status;
      private String error;
      private String message;
      private String path;
   }

   /**
    * Clase para respuestas de error de validación.
    */
   @lombok.Data
   @lombok.Builder
   @lombok.NoArgsConstructor
   @lombok.AllArgsConstructor
   public static class ValidationErrorResponse {
      private LocalDateTime timestamp;
      private Integer status;
      private String error;
      private String message;
      private String path;
      private Map<String, String> errors;
   }
}