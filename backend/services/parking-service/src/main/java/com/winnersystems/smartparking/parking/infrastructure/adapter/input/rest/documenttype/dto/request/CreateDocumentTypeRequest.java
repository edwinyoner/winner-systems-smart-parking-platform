package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentTypeRequest {

   @NotBlank(message = "El código es obligatorio")
   @Size(max = 20, message = "El código no puede exceder 20 caracteres")
   private String code;

   @NotBlank(message = "El nombre es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
   private String description;

   private Boolean status;
}