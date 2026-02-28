// src/app/views/parking-service/zones/zone-detail/zone-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

// Services
import { ZoneService } from '../../../../core/services/parking/zone.service';

// Models
import { Zone } from '../../../../core/models/parking/zone.model';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-zone-detail',
  standalone: true,
  imports: [
    CommonModule,
    AlertMessageComponent
  ],
  templateUrl: './zone-detail.component.html',
  styleUrls: ['./zone-detail.component.css']
})
export class ZoneDetailComponent implements OnInit {

  zoneId!: number;
  zone: Zone | null = null;
  isLoading = true;
  errorMessage: string | null = null;
  showDeleteConfirm = false;

  constructor(
    private zoneService: ZoneService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.zoneId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadZone();
  }

  private loadZone(): void {
    this.isLoading = true;
    
    this.zoneService.getById(this.zoneId).subscribe({
      next: (zone: Zone) => {
        this.zone = zone;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los detalles de la zona';
        console.error(error);
      }
    });
  }

  editZone(): void {
    this.router.navigate(['/zones', this.zoneId, 'edit']);
  }

  confirmDelete(): void {
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
  }

  deleteZone(): void {
    if (!this.zone?.id) return;

    this.zoneService.delete(this.zone.id).subscribe({
      next: () => {
        this.router.navigate(['/zones'], {
          state: { successMessage: `Zona "${this.zone?.name}" eliminada exitosamente` }
        });
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'No se pudo eliminar la zona';
        this.showDeleteConfirm = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/zones']);
  }

  getStatusLabel(status: string): string {
    const labels: { [key: string]: string } = {
      'ACTIVE': 'Activa',
      'INACTIVE': 'Inactiva',
      'MAINTENANCE': 'En Mantenimiento',
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
    if (percentage >= 90) return 'text-danger';
    if (percentage >= 70) return 'text-warning';
    return 'text-success';
  }

  dismissError(): void {
    this.errorMessage = null;
  }
}