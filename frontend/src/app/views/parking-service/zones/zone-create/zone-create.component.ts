// src/app/views/parking-service/zones/zone-create/zone-create.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

// Services
import { ZoneService } from '../../../../core/services/parking/zone.service';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-zone-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AlertMessageComponent
  ],
  templateUrl: './zone-create.component.html',
  styleUrls: ['./zone-create.component.css']
})
export class ZoneCreateComponent implements OnInit {

  zoneForm!: FormGroup;
  isSubmitting = false;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private zoneService: ZoneService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    this.zoneForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      code: ['', [Validators.required, Validators.maxLength(20)]],
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

    this.zoneService.create(request).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.router.navigate(['/zones'], {
          state: { successMessage: `Zona "${response.name}" creada exitosamente` }
        });
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Error al crear la zona';
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    });
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