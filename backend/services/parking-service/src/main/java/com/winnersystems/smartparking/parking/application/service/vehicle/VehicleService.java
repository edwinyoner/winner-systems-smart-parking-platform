package com.winnersystems.smartparking.parking.application.service.vehicle;

import com.winnersystems.smartparking.parking.application.dto.command.CreateVehicleCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateVehicleCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.vehicle.*;
import com.winnersystems.smartparking.parking.application.port.output.VehiclePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de vehículos.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class VehicleService implements
      CreateVehicleUseCase,
      GetVehicleUseCase,
      ListVehiclesUseCase,
      UpdateVehicleUseCase,
      DeleteVehicleUseCase,
      RestoreVehicleUseCase {

   // ========================= PUERTOS DE SALIDA =========================

   private final VehiclePersistencePort vehiclePersistencePort;

   // ========================= CONSTRUCTOR =========================

   public VehicleService(VehiclePersistencePort vehiclePersistencePort) {
      this.vehiclePersistencePort = vehiclePersistencePort;
   }

   // ========================= CreateVehicleUseCase =========================

   @Override
   public VehicleDto createVehicle(CreateVehicleCommand command) {
      // 1. Normalizar placa
      String normalizedPlate = command.licensePlate().trim().toUpperCase();

      // 2. Validar que no exista vehículo con esa placa
      if (vehiclePersistencePort.existsByPlateNumber(normalizedPlate)) {
         throw new IllegalArgumentException(
               "Ya existe un vehículo con la placa: " + normalizedPlate
         );
      }

      // 3. Crear vehículo
      Vehicle vehicle = new Vehicle(normalizedPlate);
      vehicle.normalizeLicensePlate();

      Vehicle saved = vehiclePersistencePort.save(vehicle);

      return buildVehicleDto(saved);
   }

   // ========================= GetVehicleUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public VehicleDto getVehicleById(Long vehicleId) {
      Vehicle vehicle = vehiclePersistencePort.findById(vehicleId)
            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

      return buildVehicleDto(vehicle);
   }

   @Override
   @Transactional(readOnly = true)
   public VehicleDto getVehicleByPlate(String licensePlate) {
      String normalizedPlate = licensePlate.trim().toUpperCase();

      return vehiclePersistencePort.findByPlateNumber(normalizedPlate)
            .map(this::buildVehicleDto)
            .orElse(null);
   }

   // ========================= ListVehiclesUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<VehicleDto> listAllVehicles(int pageNumber, int pageSize,
                                                    String search, String status) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "createdAt", "DESC");
      PageResult<Vehicle> result = vehiclePersistencePort.findAll(req, search, status);
      return toVehiclePagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public List<VehicleDto> listAllActiveVehicles() {
      List<Vehicle> vehicles = vehiclePersistencePort.findAllActive();
      return vehicles.stream()
            .map(this::buildVehicleDto)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<VehicleDto> listRecurrentVehicles(int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "totalVisits", "DESC");
      PageResult<Vehicle> result = vehiclePersistencePort.findRecurrent(req);
      return toVehiclePagedResponse(result);
   }

   // ========================= UpdateVehicleUseCase =========================

   @Override
   public VehicleDto updateVehicle(Long vehicleId, UpdateVehicleCommand command) {
      Vehicle vehicle = vehiclePersistencePort.findById(vehicleId)
            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

      if (vehicle.isDeleted()) {
         throw new IllegalStateException("No se puede actualizar un vehículo eliminado");
      }

      Vehicle updated = vehiclePersistencePort.save(vehicle);

      return buildVehicleDto(updated);
   }

   // ========================= DeleteVehicleUseCase =========================

   @Override
   public void deleteVehicle(Long vehicleId, Long deletedBy) {
      Vehicle vehicle = vehiclePersistencePort.findById(vehicleId)
            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

      vehicle.markAsDeleted(deletedBy);
      vehiclePersistencePort.save(vehicle);
   }

   // ========================= RestoreVehicleUseCase =========================

   @Override
   public VehicleDto restoreVehicle(Long vehicleId) {
      Vehicle vehicle = vehiclePersistencePort.findById(vehicleId)
            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

      if (!vehicle.isDeleted()) {
         throw new IllegalStateException("El vehículo no está eliminado");
      }

      vehicle.restore();
      Vehicle restored = vehiclePersistencePort.save(vehicle);

      return buildVehicleDto(restored);
   }

   // ========================= BUILDER PRIVADO — RECORD =========================

   /**
    * Construye VehicleDto (record) con todos los datos.
    */
   private VehicleDto buildVehicleDto(Vehicle v) {
      return new VehicleDto(
            v.getId(),
            v.getLicensePlate(),
            v.getDisplayName(),
            v.getFirstSeenDate(),
            v.getLastSeenDate(),
            v.getTotalVisits(),
            v.isRecurrent(),
            v.getCreatedAt(),
            v.getUpdatedAt()
      );
   }

   // ========================= HELPER DE PAGINACIÓN =========================

   private PagedResponse<VehicleDto> toVehiclePagedResponse(PageResult<Vehicle> result) {
      List<VehicleDto> content = result.content().stream()
            .map(this::buildVehicleDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }
}