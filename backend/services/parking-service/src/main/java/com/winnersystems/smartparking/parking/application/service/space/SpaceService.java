
package com.winnersystems.smartparking.parking.application.service.space;

import com.winnersystems.smartparking.parking.application.dto.command.CreateSpaceCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateSpaceCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;
import com.winnersystems.smartparking.parking.application.port.input.space.*;
import com.winnersystems.smartparking.parking.application.port.output.SpacePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Space;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de espacios de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class SpaceService implements
      CreateSpaceUseCase,
      UpdateSpaceUseCase,
      DeleteSpaceUseCase,
      GetSpaceUseCase,
      ListSpacesUseCase,
      ToggleSpaceStatusUseCase {

   private final SpacePersistencePort spacePersistencePort;

   public SpaceService(SpacePersistencePort spacePersistencePort) {
      this.spacePersistencePort = spacePersistencePort;
   }

   // ========================= CREATE =========================

   @Override
   public SpaceDto createSpace(CreateSpaceCommand command) {
      if (spacePersistencePort.existsByCode(command.getCode())) {
         throw new IllegalArgumentException(
               "Ya existe un espacio con el código: " + command.getCode()
         );
      }

      Space space = new Space(
            command.getZoneId(),
            command.getType(),
            command.getCode(),
            command.getDescription()
      );

      if (command.getWidth() != null && command.getLength() != null) {
         space.updateDimensions(command.getWidth(), command.getLength());
      }

      if (Boolean.TRUE.equals(command.getHasSensor()) && command.getSensorId() != null) {
         space.configureSensor(command.getSensorId());
      }

      if (Boolean.TRUE.equals(command.getHasCameraCoverage())) {
         space.enableCameraCoverage();
      }

      space.validate();

      return mapToDto(spacePersistencePort.save(space));
   }

   // ========================= UPDATE =========================

   @Override
   public SpaceDto updateSpace(Long spaceId, UpdateSpaceCommand command) {
      Space space = findSpaceOrThrow(spaceId);

      if (command.getType() != null) {
         space.updateSpaceType(command.getType());
      }

      if (command.getDescription() != null) {
         space.updateDescription(command.getDescription());
      }

      if (command.getWidth() != null || command.getLength() != null) {
         Double w = command.getWidth()  != null ? command.getWidth()  : space.getWidth();
         Double l = command.getLength() != null ? command.getLength() : space.getLength();
         space.updateDimensions(w, l);
      }

      if (command.getHasSensor() != null) {
         if (Boolean.TRUE.equals(command.getHasSensor()) && command.getSensorId() != null) {
            space.configureSensor(command.getSensorId());
         } else if (Boolean.FALSE.equals(command.getHasSensor())) {
            space.removeSensor();
         }
      }

      if (command.getHasCameraCoverage() != null) {
         if (command.getHasCameraCoverage()) {
            space.enableCameraCoverage();
         } else {
            space.disableCameraCoverage();
         }
      }

      // Status es String en Space: AVAILABLE, OCCUPIED, MAINTENANCE, OUT_OF_SERVICE
      if (command.getStatus() != null) {
         switch (command.getStatus()) {
            case Space.STATUS_AVAILABLE      -> space.markAsAvailable();
            case Space.STATUS_OCCUPIED       -> space.markAsOccupied();
            case Space.STATUS_MAINTENANCE    -> space.setInMaintenance();
            case Space.STATUS_OUT_OF_SERVICE -> space.setOutOfService();
         }
      }

      space.validate();

      return mapToDto(spacePersistencePort.save(space));
   }

   // ========================= DELETE =========================

   @Override
   public void deleteSpace(Long spaceId) {
      Space space = findSpaceOrThrow(spaceId);
      space.markAsDeleted(null); // TODO: userId desde SecurityContext
      spacePersistencePort.save(space);
   }

   // ========================= GET =========================

   @Override
   @Transactional(readOnly = true)
   public SpaceDto getSpaceById(Long spaceId) {
      return mapToDto(findSpaceOrThrow(spaceId));
   }

   // ========================= LIST =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<SpaceDto> listAllSpaces(int pageNumber, int pageSize, String search, String status) {
      PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, "code", "ASC");
      PageResult<Space> pageResult = spacePersistencePort.findAll(pageRequest, search, status);
      return pageResult.toPagedResponse(this::mapToDto);
   }

   @Override
   @Transactional(readOnly = true)
   public List<SpaceDto> listAllActiveSpaces() {
      return spacePersistencePort.findAllActive()
            .stream()
            .map(this::mapToDto)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public List<SpaceDto> listSpacesByZone(Long zoneId) {
      return spacePersistencePort.findByZoneId(zoneId)
            .stream()
            .map(this::mapToDto)
            .toList();
   }

   // ========================= TOGGLE STATUS =========================

   @Override
   public SpaceDto toggleSpaceStatus(Long spaceId) {
      Space space = findSpaceOrThrow(spaceId);
      if (space.isAvailable()) {
         space.setOutOfService();
      } else if (space.isOutOfService() || space.isInMaintenance()) {
         space.markAsAvailable();
      }
      // Si está OCCUPIED no se puede cambiar manualmente — solo por transacción
      return mapToDto(spacePersistencePort.save(space));
   }

   @Override
   public SpaceDto markAsOccupied(Long spaceId) {
      Space space = findSpaceOrThrow(spaceId);
      if (!space.markAsOccupied()) {
         throw new IllegalStateException(
               "El espacio no puede ser ocupado en su estado actual: " + space.getStatus()
         );
      }
      return mapToDto(spacePersistencePort.save(space));
   }

   @Override
   public SpaceDto markAsAvailable(Long spaceId) {
      Space space = findSpaceOrThrow(spaceId);
      space.markAsAvailable();
      return mapToDto(spacePersistencePort.save(space));
   }

   @Override
   public SpaceDto setInMaintenance(Long spaceId) {
      Space space = findSpaceOrThrow(spaceId);
      space.setInMaintenance();
      return mapToDto(spacePersistencePort.save(space));
   }

   @Override
   public SpaceDto setOutOfService(Long spaceId) {
      Space space = findSpaceOrThrow(spaceId);
      space.setOutOfService();
      return mapToDto(spacePersistencePort.save(space));
   }

   // ========================= HELPERS PRIVADOS =========================

   private Space findSpaceOrThrow(Long spaceId) {
      return spacePersistencePort.findById(spaceId)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Espacio no encontrado con ID: " + spaceId
            ));
   }

   // ========================= MAPPER =========================

   private SpaceDto mapToDto(Space space) {
      return new SpaceDto(
            space.getId(),
            space.getZoneId(),
            space.getType(),
            space.getCode(),
            space.getDescription(),
            space.getWidth(),
            space.getLength(),
            space.getHasSensor(),
            space.getSensorId(),
            space.getHasCameraCoverage(),
            space.getStatus(),
            space.getCreatedAt(),
            space.getUpdatedAt()
      );
   }
}