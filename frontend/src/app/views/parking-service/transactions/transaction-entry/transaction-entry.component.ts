// src/app/views/parking-service/transactions/transaction-entry/transaction-entry.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {
  CardComponent,
  CardBodyComponent,
  CardHeaderComponent,
  ColComponent,
  RowComponent,
  FormDirective,
  FormControlDirective,
  FormTextDirective,
  ButtonDirective,
  SpinnerComponent,
  AlertComponent
} from '@coreui/angular';

import { ParkingService } from '../../../../core/services/parking/parking.service';
import { ZoneService } from '../../../../core/services/parking/zone.service';
import { SpaceService } from '../../../../core/services/parking/space.service';
import { TransactionService } from '../../../../core/services/parking/transaction.service';
import { DocumentTypeService } from '../../../../core/services/parking/document-type.service';

import { Parking } from '../../../../core/models/parking/parking.model';
import { Zone } from '../../../../core/models/parking/zone.model';
import { Space } from '../../../../core/models/parking/space.model';
import { DocumentType } from '../../../../core/models/parking/document-type.model';
import { TransactionEntryRequest } from '../../../../core/models/parking/transaction.model';

@Component({
  selector: 'app-transaction-entry',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardComponent,
    CardBodyComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent,
    FormDirective,
    FormControlDirective,
    FormTextDirective,
    ButtonDirective,
    SpinnerComponent,
    AlertComponent
  ],
  templateUrl: './transaction-entry.component.html',
  styleUrls: ['./transaction-entry.component.css']
})
export class TransactionEntryComponent implements OnInit {
  
  entryForm!: FormGroup;
  
  // ========================= DATOS DE SELECTORES =========================
  parkings: Parking[] = [];
  zones: Zone[] = [];
  spaces: Space[] = [];
  documentTypes: DocumentType[] = [];
  
  // ========================= ESTADOS DE CARGA =========================
  loading = false;
  loadingZones = false;
  loadingSpaces = false;
  submitting = false;
  
  // ========================= MENSAJES =========================
  errorMessage = '';
  
  constructor(
    private fb: FormBuilder,
    private parkingService: ParkingService,
    private zoneService: ZoneService,
    private spaceService: SpaceService,
    private transactionService: TransactionService,
    private documentTypeService: DocumentTypeService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.buildForm();
    this.loadInitialData();
    this.setupFormListeners();
  }
  
  // ========================= CONSTRUCCIÓN DEL FORMULARIO =========================
  
  buildForm(): void {
    this.entryForm = this.fb.group({
      // Ubicación
      parkingId: [null, Validators.required],
      zoneId: [null, Validators.required],
      spaceId: [null, Validators.required],
      
      // Vehículo
      plateNumber: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(8)]],
      
      // Cliente
      documentTypeId: [null, Validators.required],
      documentNumber: ['', [Validators.required, Validators.minLength(8)]],
      customerFirstName: ['', [Validators.required, Validators.minLength(2)]],
      customerLastName: ['', [Validators.required, Validators.minLength(2)]],
      customerEmail: ['', [Validators.email]],
      customerPhone: ['', [Validators.minLength(9)]],
      
      // Observaciones
      notes: ['']
    });
  }
  
  setupFormListeners(): void {
    // Cuando cambia el parking, cargar zonas
    this.entryForm.get('parkingId')?.valueChanges.subscribe(() => {
      this.onParkingChange();
    });
    
    // Cuando cambia la zona, cargar espacios
    this.entryForm.get('zoneId')?.valueChanges.subscribe(() => {
      this.onZoneChange();
    });
  }
  
  // ========================= CARGA DE DATOS INICIALES =========================
  
  loadInitialData(): void {
    this.loading = true;
    
    // Cargar parkings y document types en paralelo
    Promise.all([
      this.loadParkings(),
      this.loadDocumentTypes()
    ]).finally(() => {
      this.loading = false;
    });
  }
  
  async loadParkings(): Promise<void> {
    try {
      this.parkings = await this.parkingService.getActive().toPromise() || [];
    } catch (error) {
      console.error('Error loading parkings:', error);
    }
  }
  
  async loadDocumentTypes(): Promise<void> {
    try {
      const response = await this.documentTypeService.getActive().toPromise();
      this.documentTypes = response || [];
    } catch (error) {
      console.error('Error loading document types:', error);
    }
  }
  
  // ========================= EVENTOS DE CAMBIO =========================
  
  onParkingChange(): void {
    // Reset dependent fields
    this.entryForm.patchValue({
      zoneId: null,
      spaceId: null
    });
    this.zones = [];
    this.spaces = [];
    
    const parkingId = this.entryForm.get('parkingId')?.value;
    if (parkingId) {
      this.loadZones(parkingId);
    }
  }
  
  onZoneChange(): void {
    // Reset dependent fields
    this.entryForm.patchValue({
      spaceId: null
    });
    this.spaces = [];
    
    const zoneId = this.entryForm.get('zoneId')?.value;
    if (zoneId) {
      this.loadAvailableSpaces(zoneId);
    }
  }
  
  loadZones(parkingId: number): void {
    this.loadingZones = true;
    this.zoneService.getByParkingId(parkingId).subscribe({
      next: (zones) => {
        this.zones = zones.filter(z => z.status === 'ACTIVE');
        this.loadingZones = false;
      },
      error: (error) => {
        console.error('Error loading zones:', error);
        this.loadingZones = false;
      }
    });
  }
  
  loadAvailableSpaces(zoneId: number): void {
    this.loadingSpaces = true;
    this.spaceService.getAvailableByZoneId(zoneId).subscribe({
      next: (spaces) => {
        this.spaces = spaces;
        this.loadingSpaces = false;
        
        if (this.spaces.length === 0) {
          this.errorMessage = 'No hay espacios disponibles en esta zona';
        }
      },
      error: (error) => {
        console.error('Error loading spaces:', error);
        this.loadingSpaces = false;
      }
    });
  }
  
  // ========================= SUBMIT =========================
  
  onSubmit(): void {
    if (this.entryForm.invalid) {
      this.markFormGroupTouched(this.entryForm);
      return;
    }
    
    this.submitting = true;
    this.errorMessage = '';
    
    // Obtener el operatorId del usuario logueado (desde auth)
    const operatorId = this.getLoggedUserId();
    
    const request: TransactionEntryRequest = {
      ...this.entryForm.value,
      operatorId,
      plateNumber: this.entryForm.value.plateNumber.toUpperCase(),
      entryMethod: 'MANUAL' // Por defecto MANUAL
    };
    
    this.transactionService.recordEntry(request).subscribe({
      next: (response) => {
        // Navegar a la vista de detalle de la transacción
        this.router.navigate(['/transactions', response.id]);
      },
      error: (error) => {
        console.error('Error recording entry:', error);
        this.errorMessage = error.error?.message || 'Error al registrar la entrada';
        this.submitting = false;
      }
    });
  }
  
  // ========================= HELPERS =========================
  
  getLoggedUserId(): number {
    // TODO: Obtener del servicio de autenticación
    return 1; // Placeholder
  }
  
  markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
      
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
  
  isFieldInvalid(fieldName: string): boolean {
    const field = this.entryForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
  
  cancel(): void {
    this.router.navigate(['/transactions/active']);
  }
}