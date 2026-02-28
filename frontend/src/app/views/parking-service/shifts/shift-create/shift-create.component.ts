// src/app/views/parking-service/shifts/shift-create/shift-create.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

// Services
import { ShiftService } from '../../../../core/services/parking/shift.service';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-shift-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AlertMessageComponent
  ],
  templateUrl: './shift-create.component.html',
  styleUrls: ['./shift-create.component.css']
})
export class ShiftCreateComponent implements OnInit {

  shiftForm!: FormGroup;
  isSubmitting = false;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private shiftService: ShiftService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    this.shiftForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      code: ['', [Validators.required, Validators.maxLength(50)]],
      description: [''],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      status: [true]
    }, {
      validators: this.timeRangeValidator
    });
  }

  // Validador personalizado: hora fin debe ser después de hora inicio
  private timeRangeValidator(form: FormGroup): { [key: string]: boolean } | null {
    const startTime = form.get('startTime')?.value;
    const endTime = form.get('endTime')?.value;

    if (startTime && endTime && startTime >= endTime) {
      return { timeRangeInvalid: true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.shiftForm.invalid) {
      this.markFormGroupTouched(this.shiftForm);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;

    this.shiftService.create(this.shiftForm.value).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.router.navigate(['/shifts'], {
          state: { successMessage: `Turno "${response.name}" creado exitosamente` }
        });
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Error al crear el turno';
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/shifts']);
  }

  // Utilidades
  isFieldInvalid(fieldName: string): boolean {
    const field = this.shiftForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.shiftForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return 'Este campo es obligatorio';
      if (field.errors['maxLength']) return `Máximo ${field.errors['maxLength'].requiredLength} caracteres`;
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