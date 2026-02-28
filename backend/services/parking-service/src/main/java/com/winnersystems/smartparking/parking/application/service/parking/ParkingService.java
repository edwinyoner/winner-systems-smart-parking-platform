package com.winnersystems.smartparking.parking.application.service.parking;

import com.winnersystems.smartparking.parking.application.dto.command.CreateParkingCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateParkingCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;
import com.winnersystems.smartparking.parking.application.port.input.parking.*;
import com.winnersystems.smartparking.parking.application.port.output.ParkingPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Parking;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de Parkings.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class ParkingService implements
      CreateParkingUseCase,
      UpdateParkingUseCase,
      DeleteParkingUseCase,
      GetParkingUseCase,
      ListParkingsUseCase,
      ToggleParkingStatusUseCase {

   private final ParkingPersistencePort parkingPersistencePort;

   public ParkingService(ParkingPersistencePort parkingPersistencePort) {
      this.parkingPersistencePort = parkingPersistencePort;
   }

   // ========================= CREATE =========================

   @Override
   public ParkingDto createParking(CreateParkingCommand command) {
      if (parkingPersistencePort.existsByCode(command.getCode())) {
         throw new IllegalArgumentException(
               "Ya existe un parking con el código: " + command.getCode()
         );
      }

      Parking parking = new Parking();
      parking.setName(command.getName());
      parking.setCode(command.getCode());
      parking.setAddress(command.getAddress());
      parking.setDescription(command.getDescription());

      if (command.getLatitude() != null && command.getLongitude() != null) {
         parking.updateLocation(command.getLatitude(), command.getLongitude());
      }

      if (command.getManagerId() != null) {
         parking.assignManager(command.getManagerId(), command.getManagerName());
      }

      parking.validate();

      return mapToDto(parkingPersistencePort.save(parking));
   }

   // ========================= UPDATE =========================

   @Override
   public ParkingDto updateParking(Long parkingId, UpdateParkingCommand command) {
      Parking parking = findParkingOrThrow(parkingId);

      parking.updateBasicInfo(
            command.getName(),
            command.getAddress(),
            command.getDescription()
      );

      if (command.getLatitude() != null && command.getLongitude() != null) {
         parking.updateLocation(command.getLatitude(), command.getLongitude());
      }

      if (command.getManagerId() != null) {
         parking.assignManager(command.getManagerId(), command.getManagerName());
      }

      // Actualizar estado si viene en el comando
      if (command.getStatus() != null) {
         switch (command.getStatus()) {
            case Parking.STATUS_ACTIVE       -> parking.activate();
            case Parking.STATUS_INACTIVE     -> parking.deactivate();
            case Parking.STATUS_MAINTENANCE  -> parking.setInMaintenance();
            case Parking.STATUS_OUT_OF_SERVICE -> parking.setOutOfService();
         }
      }

      parking.validate();

      return mapToDto(parkingPersistencePort.save(parking));
   }

   // ========================= DELETE =========================

   @Override
   public void deleteParking(Long parkingId) {
      Parking parking = findParkingOrThrow(parkingId);
      parking.markAsDeleted(null); // TODO: userId desde SecurityContext
      parkingPersistencePort.save(parking);
   }

   // ========================= GET =========================

   @Override
   @Transactional(readOnly = true)
   public ParkingDto getParkingById(Long parkingId) {
      return mapToDto(findParkingOrThrow(parkingId));
   }

   // ========================= LIST =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ParkingDto> listAllParkings(int pageNumber, int pageSize, String search, String status) {
      PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, "name", "ASC");
      PageResult<Parking> pageResult = parkingPersistencePort.findAll(pageRequest, search, status);
      return pageResult.toPagedResponse(this::mapToDto);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ParkingDto> listAllActiveParkings() {
      return parkingPersistencePort.findAllActive()
            .stream()
            .map(this::mapToDto)
            .toList();
   }

   // ========================= TOGGLE STATUS =========================

   @Override
   public ParkingDto toggleParkingStatus(Long parkingId) {
      Parking parking = findParkingOrThrow(parkingId);
      if (parking.isActive()) {
         parking.deactivate();
      } else {
         parking.activate();
      }
      return mapToDto(parkingPersistencePort.save(parking));
   }

   @Override
   public ParkingDto activateParking(Long parkingId) {
      Parking parking = findParkingOrThrow(parkingId);
      parking.activate();
      return mapToDto(parkingPersistencePort.save(parking));
   }

   @Override
   public ParkingDto deactivateParking(Long parkingId) {
      Parking parking = findParkingOrThrow(parkingId);
      parking.deactivate();
      return mapToDto(parkingPersistencePort.save(parking));
   }

   @Override
   public ParkingDto setInMaintenance(Long parkingId) {
      Parking parking = findParkingOrThrow(parkingId);
      parking.setInMaintenance();
      return mapToDto(parkingPersistencePort.save(parking));
   }

   @Override
   public ParkingDto setOutOfService(Long parkingId) {
      Parking parking = findParkingOrThrow(parkingId);
      parking.setOutOfService();
      return mapToDto(parkingPersistencePort.save(parking));
   }

   // ========================= HELPERS PRIVADOS =========================

   private Parking findParkingOrThrow(Long parkingId) {
      return parkingPersistencePort.findById(parkingId)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Parking no encontrado con ID: " + parkingId
            ));
   }

   // ========================= MAPPER =========================

   private ParkingDto mapToDto(Parking parking) {
      return new ParkingDto(
            parking.getId(),
            parking.getName(),
            parking.getCode(),
            parking.getDescription(),
            parking.getAddress(),
            parking.getLatitude(),
            parking.getLongitude(),
            parking.getManagerId(),
            parking.getManagerName(),
            parking.getTotalZones(),
            parking.getTotalSpaces(),
            parking.getAvailableSpaces(),
            parking.getOccupancyPercentage(),
            parking.getStatus(),
            parking.getCreatedAt(),
            parking.getUpdatedAt()
      );
   }
}