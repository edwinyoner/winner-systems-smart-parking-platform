package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para CustomerEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

   /**
    * Busca un cliente por tipo y número de documento.
    */
   @Query("SELECT c FROM CustomerEntity c WHERE c.documentTypeId = :documentTypeId AND c.documentNumber = :documentNumber")
   Optional<CustomerEntity> findByDocument(@Param("documentTypeId") Long documentTypeId,
                                           @Param("documentNumber") String documentNumber);

   /**
    * Busca un cliente por email.
    */
   Optional<CustomerEntity> findByEmail(String email);

   /**
    * Busca un cliente por teléfono.
    */
   Optional<CustomerEntity> findByPhone(String phone);
}