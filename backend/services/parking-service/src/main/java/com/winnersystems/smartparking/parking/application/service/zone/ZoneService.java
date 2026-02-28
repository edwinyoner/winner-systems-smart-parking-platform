
package com.winnersystems.smartparking.parking.application.service.zone;

import com.winnersystems.smartparking.parking.application.dto.command.CreateZoneCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateZoneCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.zone.*;
import com.winnersystems.smartparking.parking.application.port.output.ZonePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Zone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de zonas de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class ZoneService implements
      CreateZoneUseCase,
      UpdateZoneUseCase,
      DeleteZoneUseCase,
      GetZoneUseCase,
      ListZonesUseCase,
      ToggleZoneStatusUseCase {

   private final ZonePersistencePort zonePersistencePort;

   public ZoneService(ZonePersistencePort zonePersistencePort) {
      this.zonePersistencePort = zonePersistencePort;
   }

   // ========================= CREATE =========================

   @Override
   public ZoneDto createZone(CreateZoneCommand command) {
      if (zonePersistencePort.existsByCode(command.getCode())) {
         throw new IllegalArgumentException(
               "Ya existe una zona con el código: " + command.getCode()
         );
      }

      Zone zone = new Zone();
      zone.setParkingId(command.getParkingId());
      zone.setName(command.getName());
      zone.setCode(command.getCode());
      zone.setAddress(command.getAddress());
      zone.setDescription(command.getDescription());
      zone.setTotalSpaces(command.getTotalSpaces());
      zone.setAvailableSpaces(command.getTotalSpaces());

      if (command.getLatitude() != null && command.getLongitude() != null) {
         zone.updateLocation(command.getLatitude(), command.getLongitude());
      }

      if (Boolean.TRUE.equals(command.getHasCamera()) &&
            command.getCameraIds() != null && !command.getCameraIds().isEmpty()) {
         zone.configureCameras(command.getCameraIds());
      }

      zone.validate();

      return mapToDto(zonePersistencePort.save(zone));
   }

   // ========================= UPDATE =========================

   @Override
   public ZoneDto updateZone(Long zoneId, UpdateZoneCommand command) {
      Zone zone = findZoneOrThrow(zoneId);

      zone.updateBasicInfo(
            command.getName(),
            command.getAddress(),
            command.getDescription()
      );

      if (command.getLatitude() != null && command.getLongitude() != null) {
         zone.updateLocation(command.getLatitude(), command.getLongitude());
      }

      if (command.getTotalSpaces() != null) {
         zone.updateTotalSpaces(command.getTotalSpaces());
      }

      if (Boolean.TRUE.equals(command.getHasCamera()) &&
            command.getCameraIds() != null && !command.getCameraIds().isEmpty()) {
         zone.configureCameras(command.getCameraIds());
      } else if (Boolean.FALSE.equals(command.getHasCamera())) {
         zone.removeAllCameras();
      }

      // Status es String en Zone: ACTIVE, INACTIVE, MAINTENANCE, OUT_OF_SERVICE
      if (command.getStatus() != null) {
         switch (command.getStatus()) {
            case Zone.STATUS_ACTIVE         -> zone.activate();
            case Zone.STATUS_INACTIVE       -> zone.deactivate();
            case Zone.STATUS_MAINTENANCE    -> zone.setInMaintenance();
            case Zone.STATUS_OUT_OF_SERVICE -> zone.setOutOfService();
         }
      }

      return mapToDto(zonePersistencePort.save(zone));
   }

   // ========================= DELETE =========================

   @Override
   public void deleteZone(Long zoneId) {
      Zone zone = findZoneOrThrow(zoneId);
      zone.markAsDeleted(null); // TODO: userId desde SecurityContext
      zonePersistencePort.save(zone);
   }

   // ========================= GET =========================

   @Override
   @Transactional(readOnly = true)
   public ZoneDto getZoneById(Long zoneId) {
      return mapToDto(findZoneOrThrow(zoneId));
   }

   // ========================= LIST =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ZoneDto> listAllZones(int pageNumber, int pageSize, String search, String status) {
      PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, "name", "ASC");
      PageResult<Zone> pageResult = zonePersistencePort.findAll(pageRequest, search, status);
      return pageResult.toPagedResponse(this::mapToDto);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ZoneDto> listAllActiveZones() {
      return zonePersistencePort.findAllActive()
            .stream()
            .map(this::mapToDto)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public List<ZoneDto> listZonesByParking(Long parkingId) {
      return zonePersistencePort.findByParkingId(parkingId)
            .stream()
            .map(this::mapToDto)
            .toList();
   }

   // ========================= TOGGLE STATUS =========================

   @Override
   public ZoneDto toggleZoneStatus(Long zoneId) {
      Zone zone = findZoneOrThrow(zoneId);
      if (zone.isActive()) {
         zone.deactivate();
      } else {
         zone.activate();
      }
      return mapToDto(zonePersistencePort.save(zone));
   }

   @Override
   public ZoneDto activateZone(Long zoneId) {
      Zone zone = findZoneOrThrow(zoneId);
      zone.activate();
      return mapToDto(zonePersistencePort.save(zone));
   }

   @Override
   public ZoneDto deactivateZone(Long zoneId) {
      Zone zone = findZoneOrThrow(zoneId);
      zone.deactivate();
      return mapToDto(zonePersistencePort.save(zone));
   }

   @Override
   public ZoneDto setInMaintenance(Long zoneId) {
      Zone zone = findZoneOrThrow(zoneId);
      zone.setInMaintenance();
      return mapToDto(zonePersistencePort.save(zone));
   }

   @Override
   public ZoneDto setOutOfService(Long zoneId) {
      Zone zone = findZoneOrThrow(zoneId);
      zone.setOutOfService();
      return mapToDto(zonePersistencePort.save(zone));
   }

   // ========================= HELPERS PRIVADOS =========================

   private Zone findZoneOrThrow(Long zoneId) {
      return zonePersistencePort.findById(zoneId)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Zona no encontrada con ID: " + zoneId
            ));
   }

   // ========================= MAPPER =========================

   private ZoneDto mapToDto(Zone zone) {
      return new ZoneDto(
            zone.getId(),
            zone.getParkingId(),       // ← agregado
            zone.getName(),
            zone.getCode(),
            zone.getAddress(),
            zone.getDescription(),
            zone.getTotalSpaces(),
            zone.getAvailableSpaces(),
            zone.getOccupancyPercentage(),
            zone.getLatitude(),
            zone.getLongitude(),
            zone.getHasCamera(),
            zone.getCameraIdsList(),
            zone.getCameraCount(),
            zone.getStatus(),
            zone.getCreatedAt(),
            zone.getUpdatedAt()
      );
   }
}