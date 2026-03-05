// src/app/views/parking-service/payments/payment-list/payment-list.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  CardComponent,
  CardBodyComponent,
  CardHeaderComponent,
  ColComponent,
  RowComponent,
  TableDirective,
  FormControlDirective,
  ButtonDirective,
  BadgeComponent,
  SpinnerComponent
} from '@coreui/angular';

import { TransactionService } from '../../../../core/services/parking/transaction.service';
import { TransactionDto } from '../../../../core/models/parking/transaction.model';

interface PaymentListItem {
  paymentId: number;
  transactionId: number;
  plateNumber: string;
  customerName: string;
  parkingName: string;
  zoneName: string;
  amount: number;
  currency: string;
  paymentDate: string;
  paymentStatus: string;
  referenceNumber?: string;
}

@Component({
  selector: 'app-payment-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardComponent,
    CardBodyComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent,
    TableDirective,
    FormControlDirective,
    ButtonDirective,
    BadgeComponent,
    SpinnerComponent
  ],
  templateUrl: './payment-list.component.html',
  styleUrls: ['./payment-list.component.css']
})
export class PaymentListComponent implements OnInit {
  
  // ========================= DATOS =========================
  payments: PaymentListItem[] = [];
  
  // ========================= FILTROS =========================
  filters = {
    plateNumber: '',
    startDate: '',
    endDate: ''
  };
  
  // ========================= PAGINACIÓN =========================
  currentPage = 0;
  pageSize = 20;
  totalElements = 0;
  totalPages = 0;
  
  // ========================= ESTADO =========================
  loading = false;
  
  constructor(
    private transactionService: TransactionService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.loadPayments();
  }
  
  // ========================= CARGA DE DATOS =========================
  
  loadPayments(): void {
    this.loading = true;
    
    // Cargar transacciones con estado PAID
    if (this.filters.plateNumber.trim()) {
      this.searchByPlate();
    } else if (this.filters.startDate && this.filters.endDate) {
      this.searchByDateRange();
    } else {
      this.getByPaymentStatus();
    }
  }
  
  getByPaymentStatus(): void {
    this.transactionService.getByPaymentStatus(
      'PAID',
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (response) => {
        this.processTransactions(response.content);
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading payments:', error);
        this.loading = false;
      }
    });
  }
  
  searchByPlate(): void {
    this.transactionService.searchByPlate(
      this.filters.plateNumber.trim(),
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (response) => {
        // Filtrar solo las que tienen pago
        const paidTransactions = response.content.filter(t => t.paymentStatus === 'PAID');
        this.processTransactions(paidTransactions);
        this.totalElements = paidTransactions.length;
        this.totalPages = Math.ceil(paidTransactions.length / this.pageSize);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching by plate:', error);
        this.loading = false;
      }
    });
  }
  
  searchByDateRange(): void {
    this.transactionService.getByDateRange(
      this.filters.startDate,
      this.filters.endDate,
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (response) => {
        // Filtrar solo las que tienen pago
        const paidTransactions = response.content.filter(t => t.paymentStatus === 'PAID');
        this.processTransactions(paidTransactions);
        this.totalElements = paidTransactions.length;
        this.totalPages = Math.ceil(paidTransactions.length / this.pageSize);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching by date range:', error);
        this.loading = false;
      }
    });
  }
  
  processTransactions(transactions: TransactionDto[]): void {
    this.payments = transactions.map(t => ({
      paymentId: t.id, // Usamos el ID de la transacción como referencia
      transactionId: t.id,
      plateNumber: t.plateNumber,
      customerName: t.customerName,
      parkingName: t.parkingName,
      zoneName: t.zoneName,
      amount: t.totalAmount || 0,
      currency: 'PEN',
      paymentDate: t.createdAt,
      paymentStatus: t.paymentStatus,
      referenceNumber: undefined // Se obtendría del detalle
    }));
  }
  
  // ========================= EVENTOS DE FILTROS =========================
  
  applyFilters(): void {
    this.currentPage = 0;
    this.loadPayments();
  }
  
  clearFilters(): void {
    this.filters = {
      plateNumber: '',
      startDate: '',
      endDate: ''
    };
    this.currentPage = 0;
    this.loadPayments();
  }
  
  // ========================= PAGINACIÓN =========================
  
  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadPayments();
    }
  }
  
  // ========================= NAVEGACIÓN =========================
  
  viewDetail(transactionId: number): void {
    this.router.navigate(['/payments', transactionId]);
  }
  
  viewTransaction(transactionId: number): void {
    this.router.navigate(['/transactions', transactionId]);
  }
  
  // ========================= HELPERS =========================
  
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