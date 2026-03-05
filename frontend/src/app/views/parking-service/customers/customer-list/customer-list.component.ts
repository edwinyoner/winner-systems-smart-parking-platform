// src/app/views/parking-service/customers/customer-list/customer-list.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  FormControlDirective,
  FormDirective,
  FormLabelDirective,
  FormSelectDirective,
  RowComponent,
  TableDirective,
  BadgeComponent,
  AlertComponent, 
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';
import { Customer, CustomerFilters } from '../../../../core/models/parking/customer.model';
import { CustomerService } from '../../../../core/services/parking/customer.service';
import { ParkingPagedResponse } from '../../../../core/models/pagination.model';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FormsModule,
    CardComponent,
    CardHeaderComponent,
    CardBodyComponent,
    RowComponent,
    ColComponent,
    TableDirective,
    ButtonDirective,
    IconDirective,
    FormDirective,
    FormLabelDirective,
    FormControlDirective,
    FormSelectDirective,
    BadgeComponent,
    AlertComponent, 
  ],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css'],
})
export class CustomerListComponent implements OnInit {
  // ... resto del código sin cambios
  customers: Customer[] = [];
  loading = false;
  error = '';

  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Filtros
  filters: CustomerFilters = {
    pageNumber: 0,
    pageSize: 10,
    search: '',
    status: 'ACTIVE',
  };

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.loading = true;
    this.error = '';

    this.filters.pageNumber = this.currentPage;
    this.filters.pageSize = this.pageSize;

    this.customerService.getAll(this.filters).subscribe({
      next: (response: ParkingPagedResponse<Customer>) => {
        this.customers = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar clientes';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadCustomers();
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.loadCustomers();
  }

  clearFilters(): void {
    this.filters = {
      pageNumber: 0,
      pageSize: 10,
      search: '',
      status: 'ACTIVE',
    };
    this.currentPage = 0;
    this.loadCustomers();
  }

  // Paginación
  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadCustomers();
  }

  goToFirstPage(): void {
    this.onPageChange(0);
  }

  goToPreviousPage(): void {
    if (this.currentPage > 0) {
      this.onPageChange(this.currentPage - 1);
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.onPageChange(this.currentPage + 1);
    }
  }

  goToLastPage(): void {
    this.onPageChange(this.totalPages - 1);
  }

  // Utilidades
  getStatusBadgeColor(customer: Customer): string {
    if (customer.deletedAt) return 'danger';
    return customer.isRecurrent ? 'success' : 'info';
  }

  getStatusText(customer: Customer): string {
    if (customer.deletedAt) return 'Eliminado';
    return customer.isRecurrent ? 'Recurrente' : 'Nuevo';
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('es-PE');
  }
}