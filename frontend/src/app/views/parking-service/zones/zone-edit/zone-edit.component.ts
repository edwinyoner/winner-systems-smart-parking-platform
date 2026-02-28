// src/app/views/parking-service/zones/zone-edit/zone-edit.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

// Services
import { ZoneService } from '../../../../core/services/parking/zone.service';

// Models
import { Zone } from '../../../../core/models/parking/zone.model';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-zone-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AlertMessageComponent
  ],
  templateUrl: './zone-edit.component.html',
  styleUrls: ['./zone-edit.component.css']
})
export class ZoneEditComponent implements OnInit {

  zoneId!: number;
  zoneForm!: FormGroup;
  isLoading = true;
  isSubmitting = false;
  errorMessage: string | null = null;
  currentZone: Zone | null = null;

  constructor(
    private fb: FormBuilder,
    private zoneService: ZoneService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.zoneId = Number(this.route.snapshot.paramMap.get('id'));
    this.initForm();
    this.loadZone();
  }

  private initForm(): void {
    this.zoneForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      address: ['', [Validators.required, Validators.maxLength(255)]],
      description: [''],
      totalSpaces: ['', [Validators.required, Validators.min(1)]],
      latitude: ['', [Validators.min(-90), Validators.max(90)]],
      longitude: ['', [Validators.min(-180), Validators.max(180)]],
      hasCamera: [false],
      cameraIds: this.fb.array([]),
      status: [true]
    });
  }

  get cameraIds(): FormArray {
    return this.zoneForm.get('cameraIds') as FormArray;
  }

  private loadZone(): void {
    this.isLoading = true;
    
    this.zoneService.getById(this.zoneId).subscribe({
      next: (zone: Zone) => {
        this.currentZone = zone;
        
        // Poblar cameraIds array
        if (zone.cameraIds && zone.cameraIds.length > 0) {
          zone.cameraIds.forEach(cameraId => {
            this.cameraIds.push(this.fb.control(cameraId, Validators.required));
          });
        }

        this.zoneForm.patchValue({
          name: zone.name,
          address: zone.address,
          description: zone.description,
          totalSpaces: zone.totalSpaces,
          latitude: zone.latitude,
          longitude: zone.longitude,
          hasCamera: zone.hasCamera,
          status: zone.status === 'ACTIVE'
        });
        
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar la zona';
        console.error(error);
      }
    });
  }

  addCamera(): void {
    this.cameraIds.push(this.fb.control('', Validators.required));
  }

  removeCamera(index: number): void {
    this.cameraIds.removeAt(index);
  }

  onHasCameraChange(): void {
    const hasCamera = this.zoneForm.get('hasCamera')?.value;
    if (hasCamera && this.cameraIds.length === 0) {
      this.addCamera();
    } else if (!hasCamera) {
      this.cameraIds.clear();
    }
  }

  onSubmit(): void {
    if (this.zoneForm.invalid) {
      this.markFormGroupTouched(this.zoneForm);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;

    const formValue = this.zoneForm.value;
    const request = {
      ...formValue,
      latitude: formValue.latitude ? parseFloat(formValue.latitude) : null,
      longitude: formValue.longitude ? parseFloat(formValue.longitude) : null,
      cameraIds: formValue.hasCamera ? formValue.cameraIds.filter((id: string) => id.trim()) : []
    };

    this.zoneService.update(this.zoneId, request).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.router.navigate(['/zones'], {
          state: { successMessage: `Zona "${response.name}" actualizada exitosamente` }
        });
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Error al actualizar la zona';
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    });
  }

  toggleStatus(): void {
    const currentStatus = this.zoneForm.get('status')?.value;
    this.zoneForm.patchValue({ status: !currentStatus });
  }

  cancel(): void {
    this.router.navigate(['/zones']);
  }

  // Utilidades
  isFieldInvalid(fieldName: string): boolean {
    const field = this.zoneForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.zoneForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return 'Este campo es obligatorio';
      if (field.errors['maxLength']) return `Máximo ${field.errors['maxLength'].requiredLength} caracteres`;
      if (field.errors['min']) return `El valor mínimo es ${field.errors['min'].min}`;
      if (field.errors['max']) return `El valor máximo es ${field.errors['max'].max}`;
    }
    return '';
  }

  getStatusLabel(): string {
    return this.zoneForm.get('status')?.value ? 'Activa' : 'Inactiva';
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  dismissError(): void {
    this.errorMessage = null;
  }
}