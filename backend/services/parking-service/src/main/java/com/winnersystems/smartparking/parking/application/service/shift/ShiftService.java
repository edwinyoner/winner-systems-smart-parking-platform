package com.winnersystems.smartparking.parking.application.service.shift;

import com.winnersystems.smartparking.parking.application.dto.command.CreateShiftCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateShiftCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;
import com.winnersystems.smartparking.parking.application.port.input.shift.*;
import com.winnersystems.smartparking.parking.application.port.output.ShiftPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Shift;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de turnos horarios.
 * Implementa todos los casos de uso relacionados con Shift.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@Service
@Transactional
public class ShiftService implements
      CreateShiftUseCase,
      UpdateShiftUseCase,
      DeleteShiftUseCase,
      GetShiftUseCase,
      ListShiftsUseCase,
      ToggleShiftStatusUseCase {

   private final ShiftPersistencePort shiftPersistencePort;

   public ShiftService(ShiftPersistencePort shiftPersistencePort) {
      this.shiftPersistencePort = shiftPersistencePort;
   }

   // ========================= CREATE =========================

   @Override
   public ShiftDto createShift(CreateShiftCommand command) {
      log.info("➕ Creando turno: {}", command.getName());

      // Validar código único
      if (shiftPersistencePort.existsByCode(command.getCode())) {
         throw new IllegalArgumentException(
               "Ya existe un turno con el código: " + command.getCode()
         );
      }

      // Crear entidad de dominio usando constructor
      Shift shift = new Shift(
            command.getName(),
            command.getCode(),
            command.getStartTime(),
            command.getEndTime(),
            command.getDescription()
      );

      // Configurar estado inicial
//      if (Boolean.TRUE.equals(command.getStatus())) {
//         shift.activate();
//      } // else: permanece inactivo (valor por defecto)

      // Persistir
      Shift savedShift = shiftPersistencePort.save(shift);

      log.info("Turno creado con ID: {}", savedShift.getId());
      return mapToDto(savedShift);
   }

   // ========================= UPDATE =========================

   @Override
   public ShiftDto updateShift(Long shiftId, UpdateShiftCommand command) {
      log.info("Actualizando turno ID: {}", shiftId);

      // Buscar entidad existente
      Shift shift = shiftPersistencePort.findById(shiftId)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Turno no encontrado con ID: " + shiftId
            ));

      // Actualizar información básica
      shift.updateBasicInfo(command.getName(), command.getDescription());

      // Actualizar horario
      if (command.getStartTime() != null && command.getEndTime() != null) {
         shift.updateSchedule(command.getStartTime(), command.getEndTime());
      }

      // Actualizar estado
      if (command.getStatus() != null) {
         if (command.getStatus()) {
            shift.activate();
         } else {
            shift.deactivate();
         }
      }

      // Persistir cambios
      Shift updatedShift = shiftPersistencePort.save(shift);

      log.info("Turno actualizado");
      return mapToDto(updatedShift);
   }

   // ========================= DELETE =========================

   @Override
   public void deleteShift(Long id) {
      log.info("Eliminando turno ID: {}", id);

      // Buscar entidad existente
      Shift shift = shiftPersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Turno no encontrado con ID: " + id
            ));

      // Soft delete (marcar como eliminado)
      shift.markAsDeleted(null);  // TODO: Obtener userId del contexto de seguridad

      // Persistir cambios
      shiftPersistencePort.save(shift);

      log.info("Turno eliminado (soft delete)");
   }

   // ========================= GET =========================

   @Override
   @Transactional(readOnly = true)
   public ShiftDto getShiftById(Long id) {
      log.debug("🔍 Buscando turno ID: {}", id);

      Shift shift = shiftPersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Turno no encontrado con ID: " + id
            ));

      return mapToDto(shift);
   }

   // ========================= LIST =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ShiftDto> listAllShifts(int page, int size, String search, Boolean status) {
      log.debug("Listando turnos - página: {}, tamaño: {}, search: {}, status: {}", page, size, search, status);

      PageRequest pageRequest = PageRequest.of(page, size, "id", "ASC");
      PageResult<Shift> pageResult = shiftPersistencePort.findAll(pageRequest, search, status);

      return pageResult.toPagedResponse(this::mapToDto);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ShiftDto> listAllActiveShifts() {
      log.debug("Listando turnos activos");

      List<Shift> activeShifts = shiftPersistencePort.findAllActive();

      return activeShifts.stream()
            .map(this::mapToDto)
            .toList();
   }

   // ========================= TOGGLE STATUS =========================

   @Override
   public ShiftDto toggleShiftStatus(Long id) {
      log.info("Cambiando estado del turno ID: {}", id);

      Shift shift = shiftPersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Turno no encontrado con ID: " + id
            ));

      // Toggle usando lógica de dominio
      if (shift.isActive()) {
         shift.deactivate();
      } else {
         shift.activate();
      }

      Shift updatedShift = shiftPersistencePort.save(shift);

      log.info("Estado cambiado a: {}", updatedShift.getStatus() ? "ACTIVO" : "INACTIVO");
      return mapToDto(updatedShift);
   }

   @Override
   public ShiftDto activateShift(Long id) {
      log.info("Activando turno ID: {}", id);

      Shift shift = shiftPersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Turno no encontrado con ID: " + id
            ));

      shift.activate();
      Shift updatedShift = shiftPersistencePort.save(shift);

      log.info("Turno activado");
      return mapToDto(updatedShift);
   }

   @Override
   public ShiftDto deactivateShift(Long id) {
      log.info("Desactivando turno ID: {}", id);

      Shift shift = shiftPersistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Turno no encontrado con ID: " + id
            ));

      shift.deactivate();
      Shift updatedShift = shiftPersistencePort.save(shift);

      log.info("Turno desactivado");
      return mapToDto(updatedShift);
   }

   // ========================= MAPPER =========================

   /**
    * Mapea una entidad de dominio Shift a ShiftDto.
    *
    * @param shift entidad de dominio
    * @return DTO para capa de presentación
    */
   private ShiftDto mapToDto(Shift shift) {
      return new ShiftDto(
            shift.getId(),
            shift.getName(),
            shift.getCode(),
            shift.getDescription(),
            shift.getStartTime(),
            shift.getEndTime(),
            shift.getStatus(),
            shift.getCreatedAt(),
            shift.getUpdatedAt()
      );
   }
}