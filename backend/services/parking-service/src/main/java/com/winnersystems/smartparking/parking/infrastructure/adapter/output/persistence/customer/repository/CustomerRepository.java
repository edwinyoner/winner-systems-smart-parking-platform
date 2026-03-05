package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para CustomerEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

   // ========================= FIND ÚNICO =========================

   /**
    * Busca cliente por tipo y número de documento.
    * UK: DOCUMENT_TYPE_ID + DOCUMENT_NUMBER
    */
   Optional<CustomerEntity> findByDocumentTypeIdAndDocumentNumber(Long documentTypeId, String documentNumber);

   /**
    * Busca cliente por email.
    */
   Optional<CustomerEntity> findByEmail(String email);

   /**
    * Busca cliente por teléfono.
    */
   Optional<CustomerEntity> findByPhone(String phone);

   // ========================= EXISTS =========================

   /**
    * Verifica si existe cliente con ese documento.
    */
   @Query("""
      SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
      FROM CustomerEntity c
      WHERE c.documentTypeId = :documentTypeId
      AND c.documentNumber = :documentNumber
      """)
   boolean existsByDocument(
         @Param("documentTypeId") Long documentTypeId,
         @Param("documentNumber") String documentNumber
   );

   boolean existsByEmail(String email);

   boolean existsByPhone(String phone);

   // ========================= FIND CON FILTROS (paginado) =========================

   /**
    * Lista todos los clientes con filtros opcionales.
    * - search: busca en nombre, apellido, documento, email, phone
    * - status: "ACTIVE" (deletedAt IS NULL), "DELETED" (deletedAt IS NOT NULL), "ALL"
    */
   @Query("""
      SELECT c FROM CustomerEntity c
      WHERE (:status = 'ALL'
         OR (:status = 'ACTIVE' AND c.deletedAt IS NULL)
         OR (:status = 'DELETED' AND c.deletedAt IS NOT NULL))
      AND (:search IS NULL OR :search = ''
         OR UPPER(c.firstName) LIKE UPPER(CONCAT('%', :search, '%'))
         OR UPPER(c.lastName) LIKE UPPER(CONCAT('%', :search, '%'))
         OR UPPER(c.documentNumber) LIKE UPPER(CONCAT('%', :search, '%'))
         OR UPPER(c.email) LIKE UPPER(CONCAT('%', :search, '%'))
         OR UPPER(c.phone) LIKE UPPER(CONCAT('%', :search, '%')))
      ORDER BY c.createdAt DESC
      """)
   Page<CustomerEntity> findAllWithFilters(
         @Param("search") String search,
         @Param("status") String status,
         Pageable pageable
   );

   /**
    * Lista clientes recurrentes (totalVisits > 1).
    */
   @Query("""
      SELECT c FROM CustomerEntity c
      WHERE c.deletedAt IS NULL
      AND c.totalVisits > 1
      ORDER BY c.totalVisits DESC
      """)
   Page<CustomerEntity> findRecurrent(Pageable pageable);

   /**
    * Lista clientes con cuenta móvil vinculada (authExternalId NOT NULL).
    */
   @Query("""
      SELECT c FROM CustomerEntity c
      WHERE c.deletedAt IS NULL
      AND c.authExternalId IS NOT NULL
      ORDER BY c.createdAt DESC
      """)
   Page<CustomerEntity> findWithMobileAccount(Pageable pageable);

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todos los clientes activos (no eliminados).
    */
   @Query("""
      SELECT c FROM CustomerEntity c
      WHERE c.deletedAt IS NULL
      ORDER BY c.createdAt DESC
      """)
   List<CustomerEntity> findAllActive();

   // ========================= COUNT =========================

   @Query("""
      SELECT COUNT(c) FROM CustomerEntity c
      WHERE c.deletedAt IS NULL
      """)
   long countActive();
}