// src/app/views/parking-service/payments/payment-detail/payment-detail.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import {
  CardComponent,
  CardBodyComponent,
  CardHeaderComponent,
  ColComponent,
  RowComponent,
  ButtonDirective,
  BadgeComponent,
  SpinnerComponent,
  AlertComponent
} from '@coreui/angular';

import { TransactionService } from '../../../../core/services/parking/transaction.service';
import { TransactionDetailResponse } from '../../../../core/models/parking/transaction.model';

@Component({
  selector: 'app-payment-detail',
  standalone: true,
  imports: [
    CommonModule,
    CardComponent,
    CardBodyComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent,
    ButtonDirective,
    BadgeComponent,
    SpinnerComponent,
    AlertComponent
  ],
  templateUrl: './payment-detail.component.html',
  styleUrls: ['./payment-detail.component.css']
})
export class PaymentDetailComponent implements OnInit {
  
  transaction: TransactionDetailResponse | null = null;
  loading = false;
  errorMessage = '';
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private transactionService: TransactionService
  ) {}
  
  ngOnInit(): void {
    const transactionId = this.route.snapshot.params['id'];
    if (transactionId) {
      this.loadPayment(+transactionId);
    }
  }
  
  // ========================= CARGA DE DATOS =========================
  
  loadPayment(transactionId: number): void {
    this.loading = true;
    this.transactionService.getById(transactionId).subscribe({
      next: (transaction) => {
        if (!transaction.payment) {
          this.errorMessage = 'Esta transacción no tiene un pago registrado';
        } else {
          this.transaction = transaction;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading payment:', error);
        this.errorMessage = 'No se pudo cargar la información del pago';
        this.loading = false;
      }
    });
  }
  
  // ========================= NAVEGACIÓN =========================
  
  goBack(): void {
    this.router.navigate(['/payments']);
  }
  
  goToTransaction(): void {
    if (this.transaction) {
      this.router.navigate(['/transactions', this.transaction.id]);
    }
  }
  
  // ========================= HELPERS =========================
  
  getPaymentStatusBadge(status: string): string {
    switch (status) {
      case 'COMPLETED': return 'success';
      case 'REFUNDED': return 'warning';
      case 'CANCELLED': return 'danger';
      default: return 'secondary';
    }
  }
  
  getPaymentStatusText(status: string): string {
    switch (status) {
      case 'COMPLETED': return 'Completado';
      case 'REFUNDED': return 'Reembolsado';
      case 'CANCELLED': return 'Cancelado';
      default: return status;
    }
  }
}