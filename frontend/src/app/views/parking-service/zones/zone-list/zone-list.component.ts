// src/app/views/parking-service/zones/zone-list/zone-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

// Services
import { ZoneService } from '../../../../core/services/parking/zone.service';

// Models
import { Zone } from '../../../../core/models/parking/zone.model';
import { PaginatedResponse } from '../../../../core/models/pagination.model';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-zone-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AlertMessageComponent
  ],
  templateUrl: './zone-list.component.html',
  styleUrls: ['./zone-list.component.css']
})
export class ZoneListComponent implements OnInit {

  filterForm!: FormGroup;
  zones: Zone[] = [];
  isLoading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  // Paginación
  currentPage = 1;
  pageSize = 10;
  totalItems = 0;
  totalPages = 0;
  pageSizeOptions = [5, 10, 15, 20, 25, 50];

  // Confirmación de eliminación
  showDeleteConfirm = false;
  zoneToDelete: Zone | null = null;

  // Math para el template
  Math = Math;

  constructor(
    private fb: FormBuilder,
    private zoneService: ZoneService,
    private router: Router
  ) {
    // Capturar mensajes de éxito desde la navegación
    const state = window.history.state;
    if (state?.['successMessage']) {
      this.successMessage = state['successMessage'];
      setTimeout(() => (this.successMessage = null), 5000);
    }
  }

  ngOnInit(): void {
    this.initFilterForm();
    this.loadZones();
    this.setupFilterListeners();
  }

  private initFilterForm(): void {
    this.filterForm = this.fb.group({
      search: [''],
      status: ['']
    });
  }

  private setupFilterListeners(): void {
    // Búsqueda con debounce
    this.filterForm.get('search')?.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => {
        this.currentPage = 1;
        this.loadZones();
      });

    // Filtro de estado
    this.filterForm.get('status')?.valueChanges.subscribe(() => {
      this.currentPage = 1;
      this.loadZones();
    });
  }

  loadZones(): void {
    this.isLoading = true;

    this.zoneService.getAll(this.currentPage - 1, this.pageSize).subscribe({
      next: (response: PaginatedResponse<Zone>) => {
        this.zones = response.content;
        this.totalItems = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = response.number + 1;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar las zonas del sistema';
        console.error(error);
      }
    });
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      status: ''
    }, { emitEvent: false });
    this.currentPage = 1;
    this.loadZones();
  }

  // Paginación
  onPageSizeChange(event: any): void {
    this.pageSize = Number(event.target.value);
    this.currentPage = 1;
    this.loadZones();
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadZones();
    }
  }

  getPages(): number[] {
    const pages = [];
    const maxVisiblePages = 5;
    let startPage = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(this.totalPages, startPage + maxVisiblePages - 1);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }

  // Navegación
  createZone(): void {
    this.router.navigate(['/zones/create']);
  }

  viewZone(id: number): void {
    this.router.navigate(['/zones', id]);
  }

  editZone(id: number): void {
    this.router.navigate(['/zones', id, 'edit']);
  }

  // Eliminación
  confirmDelete(zone: Zone): void {
    this.zoneToDelete = zone;
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
    this.zoneToDelete = null;
  }

  deleteZone(): void {
    if (!this.zoneToDelete?.id) return;
    this.isLoading = true;

    this.zoneService.delete(this.zoneToDelete.id).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = `Zona "${this.zoneToDelete?.name}" eliminada exitosamente`;
        this.showDeleteConfirm = false;
        this.zoneToDelete = null;
        this.loadZones();
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'No se pudo eliminar la zona';
        this.showDeleteConfirm = false;
      }
    });
  }

  // Utilidades
  getStatusLabel(status: string): string {
    const labels: { [key: string]: string } = {
      'ACTIVE': 'Activa',
      'INACTIVE': 'Inactiva',
      'MAINTENANCE': 'Mantenimiento',
      'OUT_OF_SERVICE': 'Fuera de Servicio'
    };
    return labels[status] || status;
  }

  getStatusClass(status: string): string {
    const classes: { [key: string]: string } = {
      'ACTIVE': 'bg-success',
      'INACTIVE': 'bg-secondary',
      'MAINTENANCE': 'bg-warning text-dark',
      'OUT_OF_SERVICE': 'bg-danger'
    };
    return classes[status] || 'bg-secondary';
  }

  getOccupancyClass(percentage: number): string {
    if (percentage >= 90) return 'text-danger fw-bold';
    if (percentage >= 70) return 'text-warning fw-bold';
    return 'text-success';
  }

  dismissSuccess(): void {
    this.successMessage = null;
  }

  dismissError(): void {
    this.errorMessage = null;
  }
}