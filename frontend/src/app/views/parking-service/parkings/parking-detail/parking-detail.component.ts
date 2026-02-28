// ============================================================
// parking-detail/parking-detail.component.ts
// ============================================================
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import {
  CardModule,
  TableModule,
  ButtonModule,
  BadgeModule,
  DropdownModule,
  SpinnerModule,
  AlertModule,
  GridModule,
  ProgressModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { ParkingService } from '../../../../core/services/parking/parking.service';
import { ZoneService } from '../../../../core/services/parking/zone.service';
import { SpaceService } from '../../../../core/services/parking/space.service';
import { Parking } from '../../../../core/models/parking/parking.model';
import { Zone } from '../../../../core/models/parking/zone.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-parking-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    CardModule,
    TableModule,
    ButtonModule,
    BadgeModule,
    DropdownModule,
    SpinnerModule,
    AlertModule,
    GridModule,
    ProgressModule,
    IconModule,
  ],
  templateUrl: './parking-detail.component.html',
  styleUrl: './parking-detail.component.css',
})
export class ParkingDetailComponent implements OnInit {
  parking: Parking | null = null;
  zones: Zone[] = [];
  
  loading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private parkingService: ParkingService,
    private zoneService: ZoneService,
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadParkingDetail(+id);
    }
  }

  // ==================== CARGA DE DATOS ====================

  loadParkingDetail(id: number): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      parking: this.parkingService.getById(id),
      zones: this.zoneService.getByParkingId(id),
    }).subscribe({
      next: ({ parking, zones }) => {
        this.parking = parking;
        this.zones = zones;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar los detalles del parqueo.';
        this.loading = false;
        console.error(err);
      },
    });
  }

  // ==================== ACCIONES ====================

  onToggleStatus(): void {
    if (!this.parking?.id) return;

    this.parkingService.toggleStatus(this.parking.id).subscribe({
      next: (updated) => {
        this.parking = updated;
      },
      error: (err) => {
        this.errorMessage = 'Error al cambiar el estado del parqueo.';
        console.error(err);
      },
    });
  }

  onDelete(): void {
    if (!this.parking?.id) return;

    const confirmed = confirm(
      `¿Estás seguro de eliminar el parqueo "${this.parking.name}"?\n\nEsta acción eliminará también todas sus zonas y espacios.`
    );

    if (!confirmed) return;

    this.parkingService.delete(this.parking.id).subscribe({
      next: () => {
        this.router.navigate(['/parkings']);
      },
      error: (err) => {
        this.errorMessage = 'Error al eliminar el parqueo.';
        console.error(err);
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/parkings']);
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

  getSpaceTypeLabel(type: string): string {
    const labels: Record<string, string> = {
      PARALLEL: 'Paralelo',
      DIAGONAL: 'Diagonal',
      PERPENDICULAR: 'Perpendicular',
    };
    return labels[type] || type;
  }
}