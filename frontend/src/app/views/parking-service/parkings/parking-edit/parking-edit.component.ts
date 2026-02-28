// ============================================================
// parking-edit/parking-edit.component.ts
// ============================================================
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  CardModule,
  ButtonModule,
  FormModule,
  GridModule,
  AlertModule,
  SpinnerModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { ParkingService } from '../../../../core/services/parking/parking.service';
import { Parking, ParkingRequest } from '../../../../core/models/parking/parking.model';

@Component({
  selector: 'app-parking-edit',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    FormModule,
    GridModule,
    AlertModule,
    SpinnerModule,
    IconModule,
  ],
  templateUrl: './parking-edit.component.html',
  styleUrl: './parking-edit.component.css',
})
export class ParkingEditComponent implements OnInit {
  form!: FormGroup;
  parking: Parking | null = null;
  parkingId!: number;

  loading = false;
  loadingData = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private parkingService: ParkingService,
  ) {}

  ngOnInit(): void {
    this.initForm();
    
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.parkingId = +id;
      this.loadParking();
    }
  }

  // ==================== FORMULARIO ====================

  initForm(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      code: ['', [Validators.required, Validators.pattern(/^[A-Z0-9_-]+$/)]],
      address: ['', [Validators.required, Validators.minLength(5)]],
      description: [''],
      latitude: [null, [Validators.min(-90), Validators.max(90)]],
      longitude: [null, [Validators.min(-180), Validators.max(180)]],
    });
  }

  loadParking(): void {
    this.loadingData = true;
    this.errorMessage = '';

    this.parkingService.getById(this.parkingId).subscribe({
      next: (parking) => {
        this.parking = parking;
        this.populateForm(parking);
        this.loadingData = false;
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar el parqueo. Intente nuevamente.';
        this.loadingData = false;
        console.error(err);
      },
    });
  }

  populateForm(parking: Parking): void {
    this.form.patchValue({
      name: parking.name,
      code: parking.code,
      address: parking.address,
      description: parking.description || '',
      latitude: parking.latitude,
      longitude: parking.longitude,
    });
  }

  // ==================== VALIDACIONES ====================

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    return !!(control && control.invalid && control.touched);
  }

  getError(field: string): string {
    const control = this.form.get(field);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;
    
    if (errors['required']) return 'Este campo es obligatorio';
    if (errors['minlength']) {
      return `Mínimo ${errors['minlength'].requiredLength} caracteres`;
    }
    if (errors['pattern']) return 'Solo mayúsculas, números y guiones (-)';
    if (errors['min']) return `Valor mínimo: ${errors['min'].min}`;
    if (errors['max']) return `Valor máximo: ${errors['max'].max}`;

    return 'Campo inválido';
  }

  // ==================== UTILIDADES ====================

  onCodeInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const uppercased = input.value.toUpperCase();
    this.form.patchValue({ code: uppercased }, { emitEvent: false });
  }

  // ==================== SUBMIT ====================

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request: ParkingRequest = {
      name: this.form.value.name.trim(),
      code: this.form.value.code.trim(),
      address: this.form.value.address.trim(),
      description: this.form.value.description?.trim() || undefined,
      latitude: this.form.value.latitude || undefined,
      longitude: this.form.value.longitude || undefined,
    };

    this.parkingService.update(this.parkingId, request).subscribe({
      next: (updated) => {
        this.successMessage = 'Parqueo actualizado correctamente';
        this.parking = updated;
        
        // Redirigir al detalle después de 1 segundo
        setTimeout(() => {
          this.router.navigate(['/parkings', this.parkingId]);
        }, 1000);
      },
      error: (err) => {
        this.loading = false;
        
        if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else if (err.status === 409) {
          this.errorMessage = 'Ya existe un parqueo con ese código';
        } else {
          this.errorMessage = 'Error al actualizar el parqueo. Intente nuevamente.';
        }
        
        console.error(err);
      },
    });
  }

  onCancel(): void {
    this.router.navigate(['/parkings', this.parkingId]);
  }
}