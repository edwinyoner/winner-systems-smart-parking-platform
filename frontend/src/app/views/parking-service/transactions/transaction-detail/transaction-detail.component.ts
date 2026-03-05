// src/app/views/parking-service/transactions/transaction-detail/transaction-detail.component.ts

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
  selector: 'app-transaction-detail',
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
  templateUrl: './transaction-detail.component.html',
  styleUrls: ['./transaction-detail.component.css']
})
export class TransactionDetailComponent implements OnInit {
  
  transaction: TransactionDetailResponse | null = null;
  loading = false;
  errorMessage = '';
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private transactionService: TransactionService
  ) {}
  
  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.loadTransaction(+id);
    }
  }
  
  // ========================= CARGA DE DATOS =========================
  
  loadTransaction(id: number): void {
    this.loading = true;
    this.transactionService.getById(id).subscribe({
      next: (transaction) => {
        this.transaction = transaction;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading transaction:', error);
        this.errorMessage = 'No se pudo cargar la transacción';
        this.loading = false;
      }
    });
  }
  
  // ========================= NAVEGACIÓN =========================
  
  goBack(): void {
    this.router.navigate(['/transactions']);
  }
  
  goToProcessPayment(): void {
    if (this.transaction && this.transaction.paymentStatus === 'PENDING') {
      this.router.navigate(['/transactions/payment'], {
        queryParams: { transactionId: this.transaction.id }
      });
    }
  }
  
  goToRegisterExit(): void {
    if (this.transaction && this.transaction.status === 'ACTIVE') {
      this.router.navigate(['/transactions/exit'], {
        queryParams: { plateNumber: this.transaction.vehicle.plateNumber }
      });
    }
  }
  
  // ========================= HELPERS =========================
  
  getStatusBadge(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'warning';
      case 'COMPLETED': return 'success';
      case 'CANCELLED': return 'danger';
      default: return 'secondary';
    }
  }
  
  getStatusText(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'Activa';
      case 'COMPLETED': return 'Completada';
      case 'CANCELLED': return 'Cancelada';
      default: return status;
    }
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