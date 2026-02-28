package com.winnersystems.smartparking.parking.application.service.rate;

import com.winnersystems.smartparking.parking.application.dto.command.CreateRateCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateRateCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.RateDto;
import com.winnersystems.smartparking.parking.application.port.input.rate.*;
import com.winnersystems.smartparking.parking.application.port.output.RatePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Rate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de tarifas.
 * Implementa todos los casos de uso relacionados con Rate.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateService implements
      CreateRateUseCase,
      UpdateRateUseCase,
      DeleteRateUseCase,
      GetRateUseCase,
      ListRatesUseCase,
      ToggleRateStatusUseCase {

   private final RatePersistencePort ratePersistencePort;

   // ========================= CREATE =========================

   @Override
   @Transactional
   public RateDto createRate(CreateRateCommand command) {
      log.info("Creando tarifa: {}", command.getName());

      // Validar nombre único
      if (ratePersistencePort.existsByName(command.getName())) {
         throw new IllegalArgumentException(
               "Ya existe una tarifa con el nombre: " + command.getName()
         );
      }

      // Crear entidad de dominio usando constructor
      Rate rate = new Rate(
            command.getName(),
            command.getAmount(),
            command.getDescription()
      );

      // Configurar moneda si viene en el comando
      if (command.getCurrency() != null && !command.getCurrency().isBlank()) {
         rate.setCurrency(command.getCurrency());
      }

      // Configurar estado inicial
//      if (Boolean.FALSE.equals(command.getStatus())) {
//         rate.deactivate();
//      } else {
//         rate.activate();  // Por defecto activa
//      }

      // Persistir
      Rate savedRate = ratePersistencePort.save(rate);

      log.info("Tarifa creada con ID: {}", savedRate.getId());
      return mapToDto(savedRate);
   }

   // ========================= UPDATE =========================

   @Override
   @Transactional
   public RateDto updateRate(Long rateId, UpdateRateCommand command) {
      log.info("Actualizando tarifa ID: {}", rateId);

      // Buscar entidad existente
      Rate rate = ratePersistencePort.findById(rateId)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tarifa no encontrada con ID: " + rateId
            ));

      // Validar nombre único excluyendo el actual
      if (ratePersistencePort.existsByNameAndIdNot(command.getName(), rateId)) {
         throw new IllegalArgumentException(
               "Ya existe otra tarifa con el nombre: " + command.getName()
         );
      }

      // Actualizar información básica usando método de dominio
      rate.updateBasicInfo(command.getName(), command.getDescription());

      // Actualizar monto usando método de dominio
      if (command.getAmount() != null) {
         rate.updateAmount(command.getAmount());
      }

      // Actualizar moneda
      if (command.getCurrency() != null && !command.getCurrency().isBlank()) {
         rate.setCurrency(command.getCurrency());
      }

      // Actualizar estado
      if (command.getStatus() != null) {
         if (command.getStatus()) {
            rate.activate();
         } else {
            rate.deactivate();
         }
      }

      // Persistir cambios
      Rate updatedRate = ratePersistencePort.save(rate);

      log.info("Tarifa actualizada");
      return mapToDto(updatedRate);
   }

   // ========================= DELETE =========================

   @Override
   @Transactional
   public void deleteRate(Long id) {
      log.info("Eliminando tarifa ID: {}", id);

      // Buscar entidad existente
      Rate rate = ratePersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tarifa no encontrada con ID: " + id
            ));

      // Soft delete (marcar como eliminado)
      rate.markAsDeleted(null);  // TODO: Obtener userId del contexto de seguridad

      // Persistir cambios
      ratePersistencePort.save(rate);

      log.info("Tarifa eliminada (soft delete)");
   }

   // ========================= GET =========================

   @Override
   @Transactional(readOnly = true)
   public RateDto getRateById(Long id) {
      log.debug("Buscando tarifa ID: {}", id);

      Rate rate = ratePersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tarifa no encontrada con ID: " + id
            ));

      return mapToDto(rate);
   }

   // ========================= LIST =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<RateDto> listAllRates(int page, int size, String search, Boolean status) {
      log.debug("Listando tarifas - página: {}, tamaño: {}, search: {}, status: {}", page, size, search, status);

      PageRequest pageRequest = PageRequest.of(page, size, "id", "ASC");
      PageResult<Rate> pageResult = ratePersistencePort.findAll(pageRequest, search, status);

      return pageResult.toPagedResponse(this::mapToDto);
   }

   @Override
   @Transactional(readOnly = true)
   public List<RateDto> listAllActiveRates() {
      log.debug("Listando tarifas activas");

      List<Rate> activeRates = ratePersistencePort.findAllActive();

      return activeRates.stream()
            .map(this::mapToDto)
            .toList();
   }

   // ========================= TOGGLE STATUS =========================

   @Override
   @Transactional
   public RateDto toggleRateStatus(Long id) {
      log.info("Cambiando estado de tarifa ID: {}", id);

      Rate rate = ratePersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tarifa no encontrada con ID: " + id
            ));

      // Toggle usando lógica de dominio
      if (rate.isActive()) {
         rate.deactivate();
      } else {
         rate.activate();
      }

      Rate updatedRate = ratePersistencePort.save(rate);

      log.info("Estado cambiado a: {}", updatedRate.getStatus() ? "ACTIVA" : "INACTIVA");
      return mapToDto(updatedRate);
   }

   @Override
   @Transactional
   public RateDto activateRate(Long id) {
      log.info("Activando tarifa ID: {}", id);

      Rate rate = ratePersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tarifa no encontrada con ID: " + id
            ));

      rate.activate();
      Rate updatedRate = ratePersistencePort.save(rate);

      log.info("Tarifa activada");
      return mapToDto(updatedRate);
   }

   @Override
   @Transactional
   public RateDto deactivateRate(Long id) {
      log.info("Desactivando tarifa ID: {}", id);

      Rate rate = ratePersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tarifa no encontrada con ID: " + id
            ));

      rate.deactivate();
      Rate updatedRate = ratePersistencePort.save(rate);

      log.info("Tarifa desactivada");
      return mapToDto(updatedRate);
   }

   // ========================= MAPPER =========================

   /**
    * Mapea una entidad de dominio Rate a RateDto.
    *
    * @param rate entidad de dominio
    * @return DTO para capa de presentación
    */
   private RateDto mapToDto(Rate rate) {
      return new RateDto(
            rate.getId(),
            rate.getName(),
            rate.getDescription(),
            rate.getAmount(),
            rate.getCurrency(),
            rate.getStatus(),
            rate.getCreatedAt(),
            rate.getUpdatedAt()
      );
   }
}