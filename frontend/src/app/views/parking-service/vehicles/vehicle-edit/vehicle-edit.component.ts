// src/app/views/parking-service/vehicles/vehicle-edit/vehicle-edit.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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
  AlertComponent,
  FormFeedbackComponent,
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';
import { Vehicle, VehicleUpdateRequest } from '../../../../core/models/parking/vehicle.model';
import { VehicleService } from '../../../../core/services/parking/vehicle.service';

@Component({
  selector: 'app-vehicle-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardComponent,
    CardHeaderComponent,
    CardBodyComponent,
    RowComponent,
    ColComponent,
    FormDirective,
    FormLabelDirective,
    FormControlDirective,
    FormFeedbackComponent,
    ButtonDirective,
    IconDirective,
    AlertComponent,
  ],
  templateUrl: './vehicle-edit.component.html',
  styleUrls: ['./vehicle-edit.component.css'],
})
export class VehicleEditComponent implements OnInit {
  vehicleForm!: FormGroup;
  vehicle: Vehicle | null = null;
  loading = false;
  error = '';
  vehicleId!: number;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private vehicleService: VehicleService
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.vehicleId = +id;
      this.loadVehicle();
    }
  }

  initForm(): void {
    this.vehicleForm = this.fb.group({
      licensePlate: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Z0-9]{3}-[A-Z0-9]{3}$/),
        ],
      ],
    });
  }

  loadVehicle(): void {
    this.loading = true;
    this.error = '';

    this.vehicleService.getById(this.vehicleId).subscribe({
      next: (vehicle) => {
        this.vehicle = vehicle;
        this.populateForm(vehicle);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el vehículo';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  populateForm(vehicle: Vehicle): void {
    this.vehicleForm.patchValue({
      licensePlate: vehicle.licensePlate,
    });
  }

  onSubmit(): void {
    if (this.vehicleForm.invalid) {
      this.vehicleForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    const updateRequest: VehicleUpdateRequest = {
      licensePlate: this.vehicleForm.value.licensePlate.trim().toUpperCase(),
    };

    this.vehicleService.update(this.vehicleId, updateRequest).subscribe({
      next: () => {
        alert('Vehículo actualizado exitosamente');
        this.router.navigate(['/parking-service/vehicles', this.vehicleId]);
      },
      error: (err) => {
        this.error = 'Error al actualizar el vehículo';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  onCancel(): void {
    this.router.navigate(['/parking-service/vehicles', this.vehicleId]);
  }

  // Helpers de validación
  isFieldInvalid(fieldName: string): boolean {
    const field = this.vehicleForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getErrorMessage(fieldName: string): string {
    const field = this.vehicleForm.get(fieldName);
    if (!field || !field.errors) return '';

    if (field.errors['required']) return 'Este campo es requerido';
    if (field.errors['pattern'])
      return 'Formato inválido. Debe ser ABC-123 o A1B-234';

    return 'Campo inválido';
  }

  // Helper para convertir a mayúsculas mientras se escribe
  onLicensePlateInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value.toUpperCase();
    this.vehicleForm.patchValue({ licensePlate: value }, { emitEvent: false });
    input.value = value;
  }
}