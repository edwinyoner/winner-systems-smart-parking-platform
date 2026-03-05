// src/app/views/parking-service/vehicles/vehicle-list/vehicle-list.component.ts
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
  RowComponent,
  TableDirective,
  BadgeComponent,
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';
import { Vehicle, VehicleFilters } from '../../../../core/models/parking/vehicle.model';
import { VehicleService } from '../../../../core/services/parking/vehicle.service';
import { ParkingPagedResponse } from '../../../../core/models/pagination.model';

@Component({
  selector: 'app-vehicle-list',
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
    BadgeComponent,
  ],
  templateUrl: './vehicle-list.component.html',
  styleUrls: ['./vehicle-list.component.css'],
})
export class VehicleListComponent implements OnInit {
  vehicles: Vehicle[] = [];
  loading = false;
  error = '';

  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Filtros
  filters: VehicleFilters = {
    pageNumber: 0,
    pageSize: 10,
    search: '',
    status: 'ACTIVE',
  };

  constructor(private vehicleService: VehicleService) {}

  ngOnInit(): void {
    this.loadVehicles();
  }

  loadVehicles(): void {
    this.loading = true;
    this.error = '';

    this.filters.pageNumber = this.currentPage;
    this.filters.pageSize = this.pageSize;

    this.vehicleService.getAll(this.filters).subscribe({
      next: (response: ParkingPagedResponse<Vehicle>) => {
        this.vehicles = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar vehículos';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadVehicles();
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.loadVehicles();
  }

  clearFilters(): void {
    this.filters = {
      pageNumber: 0,
      pageSize: 10,
      search: '',
      status: 'ACTIVE',
    };
    this.currentPage = 0;
    this.loadVehicles();
  }

  // Paginación
  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadVehicles();
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
  getStatusBadgeColor(vehicle: Vehicle): string {
    if (vehicle.deletedAt) return 'danger';
    return vehicle.isRecurrent ? 'success' : 'info';
  }

  getStatusText(vehicle: Vehicle): string {
    if (vehicle.deletedAt) return 'Eliminado';
    return vehicle.isRecurrent ? 'Recurrente' : 'Nuevo';
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('es-PE');
  }
}