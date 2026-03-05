// src/app/views/parking-service/transactions/transaction-payment/transaction-payment.component.ts

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
  FormLabelDirective,
  FormControlDirective,
  FormSelectDirective,
  FormCheckComponent,
  FormCheckInputDirective,
  FormCheckLabelDirective,
  ButtonDirective,
  SpinnerComponent,
  AlertComponent,
  BadgeComponent
} from '@coreui/angular';

import { TransactionService } from '../../../../core/services/parking/transaction.service';
import { PaymentTypeService } from '../../../../core/services/parking/payment-type.service';

import { TransactionDetailResponse, ProcessPaymentRequest } from '../../../../core/models/parking/transaction.model';
import { PaymentType } from '../../../../core/models/parking/payment-type.model';

@Component({
  selector: 'app-transaction-payment',
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
    FormCheckComponent,
    FormCheckInputDirective,
    FormCheckLabelDirective,
    ButtonDirective,
    SpinnerComponent,
    AlertComponent,
    BadgeComponent
  ],
  templateUrl: './transaction-payment.component.html',
  styleUrls: ['./transaction-payment.component.css']
})
export class TransactionPaymentComponent implements OnInit {
  
  searchForm!: FormGroup;
  paymentForm!: FormGroup;
  
  // ========================= DATOS =========================
  transaction: TransactionDetailResponse | null = null;
  paymentTypes: PaymentType[] = [];
  
  // ========================= ESTADOS =========================
  searching = false;
  submitting = false;
  loadingPaymentTypes = false;
  
  // ========================= MENSAJES =========================
  errorMessage = '';
  searchErrorMessage = '';
  
  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private paymentTypeService: PaymentTypeService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  ngOnInit(): void {
    this.buildForms();
    this.loadPaymentTypes();
    this.checkQueryParams();
  }
  
  // ========================= CONSTRUCCIÓN DE FORMULARIOS =========================
  
  buildForms(): void {
    // Formulario de búsqueda
    this.searchForm = this.fb.group({
      plateNumber: ['', [Validators.required, Validators.minLength(6)]]
    });
    
    // Formulario de pago
    this.paymentForm = this.fb.group({
      paymentTypeId: [null, Validators.required],
      amountPaid: [0, [Validators.required, Validators.min(0.01)]],
      referenceNumber: [''],
      sendReceipt: [true],
      notes: ['']
    });
  }
  
  checkQueryParams(): void {
    this.route.queryParams.subscribe(params => {
      if (params['transactionId']) {
        this.loadTransactionById(+params['transactionId']);
      } else if (params['plateNumber']) {
        this.searchForm.patchValue({ plateNumber: params['plateNumber'] });
        this.searchByPlate();
      }
    });
  }
  
  // ========================= CARGA DE DATOS =========================
  
  loadPaymentTypes(): void {
    this.loadingPaymentTypes = true;
    this.paymentTypeService.getActive().subscribe({
      next: (paymentTypes) => {
        this.paymentTypes = paymentTypes;
        this.loadingPaymentTypes = false;
      },
      error: (error) => {
        console.error('Error loading payment types:', error);
        this.loadingPaymentTypes = false;
      }
    });
  }
  
  loadTransactionById(id: number): void {
    this.searching = true;
    this.transactionService.getById(id).subscribe({
      next: (transaction) => {
        this.transaction = transaction;
        this.setAmountPaid(transaction.totalAmount || 0);
        this.searching = false;
      },
      error: (error) => {
        console.error('Error loading transaction:', error);
        this.searchErrorMessage = 'No se pudo cargar la transacción';
        this.searching = false;
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
          this.setAmountPaid(transaction.totalAmount || 0);
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
  
  setAmountPaid(amount: number): void {
    this.paymentForm.patchValue({ amountPaid: amount });
  }
  
  clearSearch(): void {
    this.searchForm.reset();
    this.transaction = null;
    this.searchErrorMessage = '';
    this.paymentForm.reset({ sendReceipt: true });
  }
  
  // ========================= PROCESAMIENTO DE PAGO =========================
  
  onSubmit(): void {
    if (this.paymentForm.invalid || !this.transaction) {
      this.markFormGroupTouched(this.paymentForm);
      return;
    }
    
    this.submitting = true;
    this.errorMessage = '';
    
    const operatorId = this.getLoggedUserId();
    
    const request: ProcessPaymentRequest = {
      paymentTypeId: this.paymentForm.value.paymentTypeId,
      amountPaid: this.paymentForm.value.amountPaid,
      operatorId,
      referenceNumber: this.paymentForm.value.referenceNumber || undefined,
      sendReceipt: this.paymentForm.value.sendReceipt,
      notes: this.paymentForm.value.notes || undefined
    };
    
    this.transactionService.processPayment(this.transaction.id, request).subscribe({
      next: (response) => {
        // Navegar al detalle de la transacción
        this.router.navigate(['/transactions', response.id]);
      },
      error: (error) => {
        console.error('Error processing payment:', error);
        this.errorMessage = error.error?.message || 'Error al procesar el pago';
        this.submitting = false;
      }
    });
  }
  
  // ========================= HELPERS =========================
  
  getLoggedUserId(): number {
    // TODO: Obtener del servicio de autenticación
    return 1; // Placeholder
  }
  
  calculateChange(): number {
    if (!this.transaction) return 0;
    const amountPaid = this.paymentForm.get('amountPaid')?.value || 0;
    const totalAmount = this.transaction.totalAmount || 0;
    return Math.max(0, amountPaid - totalAmount);
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