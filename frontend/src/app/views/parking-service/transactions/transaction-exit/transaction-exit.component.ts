// src/app/views/parking-service/transactions/transaction-exit/transaction-exit.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
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
  AlertComponent,
  BadgeComponent
} from '@coreui/angular';

import { TransactionService } from '../../../../core/services/parking/transaction.service';
import { DocumentTypeService } from '../../../../core/services/parking/document-type.service';

import { TransactionDetailResponse, TransactionExitRequest } from '../../../../core/models/parking/transaction.model';
import { DocumentType } from '../../../../core/models/parking/document-type.model';

@Component({
  selector: 'app-transaction-exit',
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
    AlertComponent,
    BadgeComponent
  ],
  templateUrl: './transaction-exit.component.html',
  styleUrls: ['./transaction-exit.component.css']
})
export class TransactionExitComponent implements OnInit {
  
  searchForm!: FormGroup;
  exitForm!: FormGroup;
  
  // ========================= DATOS =========================
  transaction: TransactionDetailResponse | null = null;
  documentTypes: DocumentType[] = [];
  
  // ========================= ESTADOS =========================
  searching = false;
  submitting = false;
  loadingDocumentTypes = false;
  
  // ========================= MENSAJES =========================
  errorMessage = '';
  searchErrorMessage = '';
  
  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private documentTypeService: DocumentTypeService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  ngOnInit(): void {
    this.buildForms();
    this.loadDocumentTypes();
    this.checkQueryParams();
  }
  
  // ========================= CONSTRUCCIÓN DE FORMULARIOS =========================
  
  buildForms(): void {
    // Formulario de búsqueda
    this.searchForm = this.fb.group({
      plateNumber: ['', [Validators.required, Validators.minLength(6)]]
    });
    
    // Formulario de salida
    this.exitForm = this.fb.group({
      exitDocumentTypeId: [null, Validators.required],
      exitDocumentNumber: ['', [Validators.required, Validators.minLength(8)]],
      notes: ['']
    });
  }
  
  checkQueryParams(): void {
    // Si viene plateNumber por queryParams, buscarlo automáticamente
    this.route.queryParams.subscribe(params => {
      if (params['plateNumber']) {
        this.searchForm.patchValue({ plateNumber: params['plateNumber'] });
        this.searchByPlate();
      }
    });
  }
  
  // ========================= CARGA DE DATOS =========================
  
  loadDocumentTypes(): void {
    this.loadingDocumentTypes = true;
    this.documentTypeService.getActive().subscribe({
      next: (documentTypes) => {
        this.documentTypes = documentTypes;
        this.loadingDocumentTypes = false;
      },
      error: (error) => {
        console.error('Error loading document types:', error);
        this.loadingDocumentTypes = false;
      }
    });
  }
  
  // ========================= BÚSQUEDA DE TRANSACCIÓN =========================
  
  searchByPlate(): void {
    if (this.searchForm.invalid) {
      this.markFormGroupTouched(this.searchForm);
      return;
    }
    
    this.searching = true;
    this.searchErrorMessage = '';
    this.transaction = null;
    
    const plateNumber = this.searchForm.value.plateNumber.toUpperCase();
    
    this.transactionService.getActiveByPlate(plateNumber).subscribe({
      next: (transaction) => {
        if (transaction) {
          this.transaction = transaction;
          this.searching = false;
        } else {
          this.searchErrorMessage = 'No se encontró una transacción activa para esta placa';
          this.searching = false;
        }
      },
      error: (error) => {
        console.error('Error searching transaction:', error);
        this.searchErrorMessage = error.error?.message || 'Error al buscar la transacción';
        this.searching = false;
      }
    });
  }
  
  clearSearch(): void {
    this.searchForm.reset();
    this.transaction = null;
    this.searchErrorMessage = '';
    this.exitForm.reset();
  }
  
  // ========================= REGISTRO DE SALIDA =========================
  
  onSubmit(): void {
    if (this.exitForm.invalid || !this.transaction) {
      this.markFormGroupTouched(this.exitForm);
      return;
    }
    
    this.submitting = true;
    this.errorMessage = '';
    
    const operatorId = this.getLoggedUserId();
    
    const request: TransactionExitRequest = {
      transactionId: this.transaction.id,
      exitDocumentTypeId: this.exitForm.value.exitDocumentTypeId,
      exitDocumentNumber: this.exitForm.value.exitDocumentNumber,
      operatorId,
      exitMethod: 'MANUAL',
      notes: this.exitForm.value.notes || undefined
    };
    
    this.transactionService.recordExit(request).subscribe({
      next: (response) => {
        // Navegar al detalle de la transacción
        this.router.navigate(['/transactions', response.id]);
      },
      error: (error) => {
        console.error('Error recording exit:', error);
        this.errorMessage = error.error?.message || 'Error al registrar la salida';
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
  
  isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    const field = formGroup.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
  
  cancel(): void {
    this.router.navigate(['/transactions/active']);
  }
  
  getPaymentStatusBadge(status: string): string {
    switch (status) {
      case 'PAID': return 'success';
      case 'PENDING': return 'warning';
      case 'OVERDUE': return 'danger';
      default: return 'secondary';
    }
  }
  
  getPaymentStatusText(status: string): string {
    switch (status) {
      case 'PAID': return 'Pagado';
      case 'PENDING': return 'Pendiente';
      case 'OVERDUE': return 'Vencido';
      default: return status;
    }
  }
}