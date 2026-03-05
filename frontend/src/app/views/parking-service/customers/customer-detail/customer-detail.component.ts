// src/app/views/parking-service/customers/customer-detail/customer-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
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
import { Customer } from '../../../../core/models/parking/customer.model';
import { CustomerService } from '../../../../core/services/parking/customer.service';
import { CustomerVehicle } from '../../../../core/models/parking/customer-vehicle.model';
import { CustomerVehicleService } from '../../../../core/services/parking/customer-vehicle.service';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
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
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer-detail.component.css'],
})
export class CustomerDetailComponent implements OnInit {
  customer: Customer | null = null;
  vehicles: CustomerVehicle[] = [];
  loading = false;
  error = '';
  customerId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService,
    private customerVehicleService: CustomerVehicleService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.customerId = +id;
      this.loadCustomer();
      this.loadVehicles();
    }
  }

  loadCustomer(): void {
    this.loading = true;
    this.error = '';

    this.customerService.getById(this.customerId).subscribe({
      next: (customer) => {
        this.customer = customer;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el cliente';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  loadVehicles(): void {
    this.customerVehicleService.getVehiclesByCustomer(this.customerId).subscribe({
      next: (vehicles) => {
        this.vehicles = vehicles;
      },
      error: (err) => {
        console.error('Error al cargar vehículos:', err);
      },
    });
  }

  onDelete(): void {
    if (!this.customer) return;

    const confirmDelete = confirm(
      `¿Está seguro que desea eliminar el cliente ${this.customer.fullName}?`
    );

    if (confirmDelete) {
      this.customerService.delete(this.customerId).subscribe({
        next: () => {
          alert('Cliente eliminado exitosamente');
          this.router.navigate(['/parking-service/customers']);
        },
        error: (err) => {
          alert('Error al eliminar el cliente');
          console.error('Error:', err);
        },
      });
    }
  }

  onRestore(): void {
    if (!this.customer) return;

    const confirmRestore = confirm(
      `¿Está seguro que desea restaurar el cliente ${this.customer.fullName}?`
    );

    if (confirmRestore) {
      this.customerService.restore(this.customerId).subscribe({
        next: () => {
          alert('Cliente restaurado exitosamente');
          this.loadCustomer();
        },
        error: (err) => {
          alert('Error al restaurar el cliente');
          console.error('Error:', err);
        },
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/parking-service/customers']);
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
    if (!this.customer) return 'secondary';
    if (this.customer.deletedAt) return 'danger';
    return this.customer.isRecurrent ? 'success' : 'info';
  }

  getStatusText(): string {
    if (!this.customer) return '';
    if (this.customer.deletedAt) return 'Eliminado';
    return this.customer.isRecurrent ? 'Recurrente' : 'Nuevo';
  }
}