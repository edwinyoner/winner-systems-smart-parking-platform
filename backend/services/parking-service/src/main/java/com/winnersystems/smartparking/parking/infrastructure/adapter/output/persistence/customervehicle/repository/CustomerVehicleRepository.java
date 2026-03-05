package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.entity.CustomerVehicleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para CustomerVehicleEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface CustomerVehicleRepository extends JpaRepository<CustomerVehicleEntity, Long> {

   // ========================= FIND ÚNICO =========================

   /**
    * Busca relación específica por customerId + vehicleId.
    * UK: CUSTOMER_ID + VEHICLE_ID
    */
   Optional<CustomerVehicleEntity> findByCustomerIdAndVehicleId(Long customerId, Long vehicleId);

   // ========================= EXISTS =========================

   boolean existsByCustomerIdAndVehicleId(Long customerId, Long vehicleId);

   // ========================= LIST (por customer) =========================

   /**
    * Lista todos los vehículos usados por un cliente.
    */
   List<CustomerVehicleEntity> findByCustomerId(Long customerId);

   // ========================= LIST (por vehicle) =========================

   /**
    * Lista todos los clientes que han usado un vehículo.
    */
   List<CustomerVehicleEntity> findByVehicleId(Long vehicleId);

   // ========================= LIST (combinaciones frecuentes - paginado) =========================

   /**
    * Lista combinaciones frecuentes (usageCount >= minUsageCount).
    */
   @Query("""
      SELECT cv FROM CustomerVehicleEntity cv
      WHERE cv.usageCount >= :minUsageCount
      ORDER BY cv.usageCount DESC
      """)
   Page<CustomerVehicleEntity> findFrequentCombinations(
         @Param("minUsageCount") int minUsageCount,
         Pageable pageable
   );

   // ========================= COUNT =========================

   long countByCustomerId(Long customerId);

   long countByVehicleId(Long vehicleId);
}