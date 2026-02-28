// ============================================================
// parking-list/parking-list.component.ts
// ============================================================
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import {
  CardModule,
  TableModule,
  ButtonModule,
  FormModule,
  BadgeModule,
  DropdownModule,
  PaginationModule,
  SpinnerModule,
  AlertModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { ParkingService } from '../../../../core/services/parking/parking.service';
import { Parking } from '../../../../core/models/parking/parking.model';
import { debounceTime, Subject } from 'rxjs';

@Component({
  selector: 'app-parking-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    CardModule,
    TableModule,
    ButtonModule,
    FormModule,
    BadgeModule,
    DropdownModule,
    PaginationModule,
    SpinnerModule,
    AlertModule,
    IconModule,
  ],
  templateUrl: './parking-list.component.html',
  styleUrl: './parking-list.component.css',
})
export class ParkingListComponent implements OnInit {
  parkings: Parking[] = [];
  
  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Filtros
  searchTerm = '';
  statusFilter = '';
  private searchSubject = new Subject<string>();

  // Estado
  loading = false;
  errorMessage = '';

  constructor(private parkingService: ParkingService) {
    // Debounce para el buscador
    this.searchSubject.pipe(debounceTime(400)).subscribe(term => {
      this.searchTerm = term;
      this.currentPage = 0;
      this.loadParkings();
    });
  }

  ngOnInit(): void {
    this.loadParkings();
  }

  // ==================== CARGA DE DATOS ====================

  loadParkings(): void {
    this.loading = true;
    this.errorMessage = '';

    this.parkingService
      .getAll(this.currentPage, this.pageSize, this.searchTerm, this.statusFilter)
      .subscribe({
        next: (response) => {
          this.parkings = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (err) => {
          this.errorMessage = 'Error al cargar los parqueos. Intente nuevamente.';
          this.loading = false;
          console.error(err);
        },
      });
  }

  // ==================== FILTROS ====================

  onSearchChange(term: string): void {
    this.searchSubject.next(term);
  }

  onStatusChange(): void {
    this.currentPage = 0;
    this.loadParkings();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.statusFilter = '';
    this.currentPage = 0;
    this.loadParkings();
  }

  // ==================== PAGINACIÓN ====================

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadParkings();
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  // ==================== ACCIONES ====================

  onToggleStatus(parking: Parking): void {
    if (!parking.id) return;

    this.parkingService.toggleStatus(parking.id).subscribe({
      next: () => {
        this.loadParkings();
      },
      error: (err) => {
        this.errorMessage = `Error al cambiar el estado del parqueo "${parking.name}".`;
        console.error(err);
      },
    });
  }

  onDelete(parking: Parking): void {
    if (!parking.id) return;

    const confirmed = confirm(
      `¿Estás seguro de eliminar el parqueo "${parking.name}"?\n\nEsta acción no se puede deshacer.`
    );

    if (!confirmed) return;

    this.parkingService.delete(parking.id).subscribe({
      next: () => {
        this.loadParkings();
      },
      error: (err) => {
        this.errorMessage = `Error al eliminar el parqueo "${parking.name}".`;
        console.error(err);
      },
    });
  }

  // ==================== UTILIDADES ====================

  getStatusBadge(status: string): string {
    const badges: Record<string, string> = {
      ACTIVE: 'success',
      INACTIVE: 'secondary',
      MAINTENANCE: 'warning',
      OUT_OF_SERVICE: 'danger',
    };
    return badges[status] || 'secondary';
  }

  getStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      ACTIVE: 'Activo',
      INACTIVE: 'Inactivo',
      MAINTENANCE: 'Mantenimiento',
      OUT_OF_SERVICE: 'Fuera de Servicio',
    };
    return labels[status] || status;
  }

  getOccupancyColor(percentage: number): string {
    if (percentage >= 90) return 'danger';
    if (percentage >= 70) return 'warning';
    if (percentage >= 50) return 'info';
    return 'success';
  }
}