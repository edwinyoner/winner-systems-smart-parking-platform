package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.DocumentType;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;

import java.util.List;
import java.util.Optional;

public interface DocumentTypePersistencePort {
   DocumentType save(DocumentType documentType);
   Optional<DocumentType> findById(Long id);
   Optional<DocumentType> findByCode(String code);
   List<DocumentType> findAllActive();
   PageResult<DocumentType> findAll(PageRequest pageRequest, String search, Boolean status);
   void delete(Long id);
   boolean existsByCode(String code);
   boolean existsByCodeAndIdNot(String code, Long id);
}