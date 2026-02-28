package com.winnersystems.smartparking.parking.infrastructure.config;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.entity.DocumentTypeEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.repository.DocumentTypeRepository;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.entity.PaymentTypeEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.repository.PaymentTypeRepository;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.entity.RateEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.repository.RateRepository;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.entity.ShiftEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.repository.ShiftRepository;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.entity.ZoneEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.repository.ZoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * Cargador de datos iniciales para el sistema de parking.
 *
 * Datos basados en la Ordenanza Municipal N° 011-MPH de Huaraz.
 *
 * Carga:
 * - Tipos de documento (DNI, CE, Pasaporte, RUC)
 * - Tipos de pago (Efectivo, Tarjeta, QR, Yape, POS)
 * - Turnos (Mañana, Tarde, Noche)
 * - Tarifas (Estándar, Nocturna, Preferencial, Cortesía, Mensual)
 * - Relación Zona-Turno-Tarifa
 *
 * NOTA: Parking, Zones y Spaces se crean mediante el STEPPER de configuración.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@Slf4j
public class DataSeeder implements CommandLineRunner {

   private final DocumentTypeRepository documentTypeRepository;
   private final PaymentTypeRepository paymentTypeRepository;
   private final ShiftRepository shiftRepository;
   private final RateRepository rateRepository;
   private final ZoneRepository zoneRepository;

   public DataSeeder(
         DocumentTypeRepository documentTypeRepository,
         PaymentTypeRepository paymentTypeRepository,
         ShiftRepository shiftRepository,
         RateRepository rateRepository,
         ZoneRepository zoneRepository) {
      this.documentTypeRepository = documentTypeRepository;
      this.paymentTypeRepository = paymentTypeRepository;
      this.shiftRepository = shiftRepository;
      this.rateRepository = rateRepository;
      this.zoneRepository = zoneRepository;
   }

   @Override
   public void run(String... args) {
      log.info("========================================");
      log.info("INICIANDO CARGA DE DATOS INICIALES");
      log.info("========================================");

      seedDocumentTypes();
      seedPaymentTypes();
      seedShifts();
      seedRates();

      log.info("========================================");
      log.info("CARGA DE DATOS BASE COMPLETADA");
      log.info("========================================");
      log.info("NOTA: Parking, Zones y Spaces se crean");
      log.info("      mediante el STEPPER de configuración");
   }

   // ========================= TIPOS DE DOCUMENTO =========================

   private void seedDocumentTypes() {
      if (documentTypeRepository.count() > 0) {
         log.info("✓ Tipos de documento ya existen, saltando...");
         return;
      }

      log.info("→ Cargando tipos de documento...");

      documentTypeRepository.save(DocumentTypeEntity.builder()
            .code("DNI")
            .name("Documento Nacional de Identidad")
            .description("Documento de identidad peruano para ciudadanos (8 dígitos)")
            .status(true)
            .build());

      documentTypeRepository.save(DocumentTypeEntity.builder()
            .code("CE")
            .name("Carné de Extranjería")
            .description("Documento para extranjeros residentes en Perú (9 dígitos)")
            .status(true)
            .build());

      documentTypeRepository.save(DocumentTypeEntity.builder()
            .code("PASSPORT")
            .name("Pasaporte")
            .description("Documento de viaje internacional (formato variable)")
            .status(true)
            .build());

      documentTypeRepository.save(DocumentTypeEntity.builder()
            .code("RUC")
            .name("Registro Único de Contribuyentes")
            .description("Número de identificación tributaria (11 dígitos)")
            .status(true)
            .build());

      log.info("✓ 4 tipos de documento cargados");
   }

   // ========================= TIPOS DE PAGO =========================

   private void seedPaymentTypes() {
      if (paymentTypeRepository.count() > 0) {
         log.info("✓ Tipos de pago ya existen, saltando...");
         return;
      }

      log.info("→ Cargando tipos de pago...");

      paymentTypeRepository.save(PaymentTypeEntity.builder()
            .code("CASH")
            .name("Efectivo")
            .description("Pago en efectivo (billetes y monedas)")
            .status(true)
            .build());

      paymentTypeRepository.save(PaymentTypeEntity.builder()
            .code("CARD")
            .name("Tarjeta de Crédito/Débito")
            .description("Pago con tarjeta Visa, Mastercard, etc.")
            .status(true)
            .build());

      paymentTypeRepository.save(PaymentTypeEntity.builder()
            .code("QR")
            .name("Código QR")
            .description("Pago mediante código QR")
            .status(true)
            .build());

      paymentTypeRepository.save(PaymentTypeEntity.builder()
            .code("YAPE")
            .name("Yape")
            .description("Pago con app Yape del BCP")
            .status(true)
            .build());

      paymentTypeRepository.save(PaymentTypeEntity.builder()
            .code("POS")
            .name("POS (Terminal)")
            .description("Pago con terminal punto de venta")
            .status(true)
            .build());

      log.info("✓ 5 tipos de pago cargados");
   }

   // ========================= TURNOS =========================

   private void seedShifts() {
      if (shiftRepository.count() > 0) {
         log.info("✓ Turnos ya existen, saltando...");
         return;
      }

      log.info("→ Cargando turnos...");

      shiftRepository.save(ShiftEntity.builder()
            .code("MORNING")
            .name("Mañana")
            .startTime(LocalTime.of(6, 0))
            .endTime(LocalTime.of(14, 0))
            .status(true)
            .description("Turno matutino - 6:00 AM a 2:00 PM")
            .build());

      shiftRepository.save(ShiftEntity.builder()
            .code("AFTERNOON")
            .name("Tarde")
            .startTime(LocalTime.of(14, 0))
            .endTime(LocalTime.of(22, 0))
            .status(true)
            .description("Turno vespertino - 2:00 PM a 10:00 PM")
            .build());

      shiftRepository.save(ShiftEntity.builder()
            .code("NIGHT")
            .name("Noche")
            .startTime(LocalTime.of(22, 0))
            .endTime(LocalTime.of(6, 0))
            .status(true)
            .description("Turno nocturno - 10:00 PM a 6:00 AM")
            .build());

      log.info("✓ 3 turnos cargados");
   }

   // ========================= TARIFAS =========================

   private void seedRates() {
      if (rateRepository.count() > 0) {
         log.info("✓ Tarifas ya existen, saltando...");
         return;
      }

      log.info("→ Cargando tarifas...");

      rateRepository.save(RateEntity.builder()
            .name("Tarifa Estándar")
            .description("Tarifa estándar por hora según Ordenanza Municipal N° 011-MPH de Huaraz")
            .amount(new BigDecimal("5.00"))
            .currency("PEN")
            .status(true)
            .build());

      rateRepository.save(RateEntity.builder()
            .name("Tarifa Nocturna")
            .description("Tarifa reducida para el turno nocturno (10:00 PM - 6:00 AM)")
            .amount(new BigDecimal("3.00"))
            .currency("PEN")
            .status(true)
            .build());

      rateRepository.save(RateEntity.builder()
            .name("Tarifa Preferencial")
            .description("Tarifa con descuento para personas con discapacidad y adultos mayores (50%)")
            .amount(new BigDecimal("2.50"))
            .currency("PEN")
            .status(true)
            .build());

      rateRepository.save(RateEntity.builder()
            .name("Tarifa Cortesía")
            .description("Aplicable a vehículos de carga y descarga rápida (hasta 15 minutos sin costo)")
            .amount(new BigDecimal("0.00"))
            .currency("PEN")
            .status(true)
            .build());

      rateRepository.save(RateEntity.builder()
            .name("Tarifa Mensual")
            .description("Abono mensual fijo para residentes y comerciantes de la zona")
            .amount(new BigDecimal("150.00"))
            .currency("PEN")
            .status(true)
            .build());

      log.info("✓ 5 tarifas cargadas");
   }

}