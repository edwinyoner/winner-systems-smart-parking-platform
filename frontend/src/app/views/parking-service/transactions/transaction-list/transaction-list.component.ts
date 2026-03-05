// src/app/views/parking-service/transactions/transaction-list/transaction-list.component.ts

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
  PaginationModule,
  SpinnerComponent
} from '@coreui/angular';

import { TransactionService } from '../../../../core/services/parking/transaction.service';
import { ParkingService } from '../../../../core/services/parking/parking.service';
import { ZoneService } from '../../../../core/services/parking/zone.service';

import { TransactionDto, TransactionStatus, TransactionPaymentStatus } from '../../../../core/models/parking/transaction.model';
import { Parking } from '../../../../core/models/parking/parking.model';
import { Zone } from '../../../../core/models/parking/zone.model';

@Component({
  selector: 'app-transaction-list',
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
    PaginationModule,
    SpinnerComponent
  ],
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {
  
  // ========================= DATOS =========================
  transactions: TransactionDto[] = [];
  parkings: Parking[] = [];
  zones: Zone[] = [];
  
  // ========================= FILTROS =========================
  filters = {
    parkingId: null as number | null,
    zoneId: null as number | null,
    status: '' as string,
    paymentStatus: '' as string,
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
  loadingZones = false;
  
  // ========================= CONSTANTES =========================
  transactionStatuses: TransactionStatus[] = ['ACTIVE', 'COMPLETED', 'CANCELLED'];
  paymentStatuses: TransactionPaymentStatus[] = ['PENDING', 'PAID', 'OVERDUE'];
  
  constructor(
    private transactionService: TransactionService,
    private parkingService: ParkingService,
    private zoneService: ZoneService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.loadParkings();
    this.loadTransactions();
  }
  
  // ========================= CARGA DE DATOS =========================
  
  loadParkings(): void {
    this.parkingService.getActive().subscribe({
      next: (parkings) => {
        this.parkings = parkings;
      },
      error: (error) => {
        console.error('Error loading parkings:', error);
      }
    });
  }
  
  loadZones(): void {
    if (!this.filters.parkingId) return;
    
    this.loadingZones = true;
    this.zoneService.getByParkingId(this.filters.parkingId).subscribe({
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
  
  loadTransactions(): void {
    this.loading = true;
    
    // Determinar qué endpoint usar según los filtros activos
    if (this.filters.plateNumber.trim()) {
      this.searchByPlate();
    } else if (this.filters.startDate && this.filters.endDate) {
      this.searchByDateRange();
    } else if (this.filters.status) {
      this.searchByStatus();
    } else if (this.filters.paymentStatus) {
      this.searchByPaymentStatus();
    } else if (this.filters.zoneId) {
      this.searchByZone();
    } else {
      this.getAll();
    }
  }
  
  getAll(): void {
    this.transactionService.getAll(
      this.currentPage,
      this.pageSize,
      'createdAt',
      'DESC'
    ).subscribe({
      next: (response) => {
        this.transactions = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading transactions:', error);
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
        this.transactions = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
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
        this.transactions = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching by date range:', error);
        this.loading = false;
      }
    });
  }
  
  searchByStatus(): void {
    this.transactionService.getByStatus(
      this.filters.status,
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (response) => {
        this.transactions = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching by status:', error);
        this.loading = false;
      }
    });
  }
  
  searchByPaymentStatus(): void {
    this.transactionService.getByPaymentStatus(
      this.filters.paymentStatus,
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (response) => {
        this.transactions = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching by payment status:', error);
        this.loading = false;
      }
    });
  }
  
  searchByZone(): void {
    if (!this.filters.zoneId) return;
    
    this.transactionService.getByZone(
      this.filters.zoneId,
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (response) => {
        this.transactions = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching by zone:', error);
        this.loading = false;
      }
    });
  }
  
  // ========================= EVENTOS DE FILTROS =========================
  
  onParkingChange(): void {
    this.filters.zoneId = null;
    this.zones = [];
    
    if (this.filters.parkingId) {
      this.loadZones();
    }
  }
  
  applyFilters(): void {
    this.currentPage = 0;
    this.loadTransactions();
  }
  
  clearFilters(): void {
    this.filters = {
      parkingId: null,
      zoneId: null,
      status: '',
      paymentStatus: '',
      plateNumber: '',
      startDate: '',
      endDate: ''
    };
    this.zones = [];
    this.currentPage = 0;
    this.loadTransactions();
  }
  
  // ========================= PAGINACIÓN =========================
  
  onPageChange(page: number): void {
  if (page >= 0 && page < this.totalPages) {
    this.currentPage = page;
    this.loadTransactions();
  }
}
  
  // ========================= NAVEGACIÓN =========================
  
  viewDetail(id: number): void {
    this.router.navigate(['/transactions', id]);
  }
  
  goToEntry(): void {
    this.router.navigate(['/transactions/entry']);
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