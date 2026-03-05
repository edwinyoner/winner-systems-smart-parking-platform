// src/app/views/parking-service/vehicles/vehicle-detail/vehicle-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  RowComponent,
  TableDirective,
  BadgeComponent,
  AlertComponent,
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';
import { Vehicle } from '../../../../core/models/parking/vehicle.model';
import { VehicleService } from '../../../../core/services/parking/vehicle.service';
import { CustomerVehicle } from '../../../../core/models/parking/customer-vehicle.model';
import { CustomerVehicleService } from '../../../../core/services/parking/customer-vehicle.service';

@Component({
  selector: 'app-vehicle-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,    
    CardComponent,
    CardHeaderComponent,
    CardBodyComponent,
    RowComponent,   
    ColComponent,     
    TableDirective,
    ButtonDirective,
    IconDirective,
    BadgeComponent,
    AlertComponent,
  ],
  templateUrl: './vehicle-detail.component.html',
  styleUrls: ['./vehicle-detail.component.css'],
})
export class VehicleDetailComponent implements OnInit {
  vehicle: Vehicle | null = null;
  customers: CustomerVehicle[] = [];
  loading = false;
  error = '';
  vehicleId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehicleService: VehicleService,
    private customerVehicleService: CustomerVehicleService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.vehicleId = +id;
      this.loadVehicle();
      this.loadCustomers();
    }
  }

  loadVehicle(): void {
    this.loading = true;
    this.error = '';

    this.vehicleService.getById(this.vehicleId).subscribe({
      next: (vehicle) => {
        this.vehicle = vehicle;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el vehículo';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  loadCustomers(): void {
    this.customerVehicleService.getCustomersByVehicle(this.vehicleId).subscribe({
      next: (customers) => {
        this.customers = customers;
      },
      error: (err) => {
        console.error('Error al cargar clientes:', err);
      },
    });
  }

  onDelete(): void {
    if (!this.vehicle) return;

    const confirmDelete = confirm(
      `¿Está seguro que desea eliminar el vehículo ${this.vehicle.licensePlate}?`
    );

    if (confirmDelete) {
      this.vehicleService.delete(this.vehicleId).subscribe({
        next: () => {
          alert('Vehículo eliminado exitosamente');
          this.router.navigate(['/parking-service/vehicles']);
        },
        error: (err) => {
          alert('Error al eliminar el vehículo');
          console.error('Error:', err);
        },
      });
    }
  }

  onRestore(): void {
    if (!this.vehicle) return;

    const confirmRestore = confirm(
      `¿Está seguro que desea restaurar el vehículo ${this.vehicle.licensePlate}?`
    );

    if (confirmRestore) {
      this.vehicleService.restore(this.vehicleId).subscribe({
        next: () => {
          alert('Vehículo restaurado exitosamente');
          this.loadVehicle();
        },
        error: (err) => {
          alert('Error al restaurar el vehículo');
          console.error('Error:', err);
        },
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/parking-service/vehicles']);
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleString('es-PE');
  }

  formatDateShort(dateString: string | undefined): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('es-PE');
  }

  getStatusBadgeColor(): string {
    if (!this.vehicle) return 'secondary';
    if (this.vehicle.deletedAt) return 'danger';
    return this.vehicle.isRecurrent ? 'success' : 'info';
  }

  getStatusText(): string {
    if (!this.vehicle) return '';
    if (this.vehicle.deletedAt) return 'Eliminado';
    return this.vehicle.isRecurrent ? 'Recurrente' : 'Nuevo';
  }
}