// src/app/views/parking-service/rates/rate-create/rate-create.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

// Services
import { RateService } from '../../../../core/services/parking/rate.service';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-rate-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AlertMessageComponent
  ],
  templateUrl: './rate-create.component.html',
  styleUrls: ['./rate-create.component.css']
})
export class RateCreateComponent implements OnInit {

  rateForm!: FormGroup;
  isSubmitting = false;
  errorMessage: string | null = null;

  currencies = [
    { code: 'PEN', name: 'Soles Peruanos (S/.)', symbol: 'S/.' },
    { code: 'USD', name: 'Dólares Americanos ($)', symbol: '$' },
    { code: 'EUR', name: 'Euros (€)', symbol: '€' }
  ];

  constructor(
    private fb: FormBuilder,
    private rateService: RateService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    this.rateForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: [''],
      amount: ['', [Validators.required, Validators.min(0.01)]],
      currency: ['PEN', Validators.required],
      status: [true]
    });
  }

  onSubmit(): void {
    if (this.rateForm.invalid) {
      this.markFormGroupTouched(this.rateForm);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;

    this.rateService.create(this.rateForm.value).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.router.navigate(['/rates'], {
          state: { successMessage: `Tarifa "${response.name}" creada exitosamente` }
        });
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Error al crear la tarifa';
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/rates']);
  }

  // Utilidades
  isFieldInvalid(fieldName: string): boolean {
    const field = this.rateForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.rateForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return 'Este campo es obligatorio';
      if (field.errors['maxLength']) return `Máximo ${field.errors['maxLength'].requiredLength} caracteres`;
      if (field.errors['min']) return `El monto debe ser mayor a ${field.errors['min'].min}`;
    }
    return '';
  }

  getCurrencySymbol(): string {
    const currency = this.rateForm.get('currency')?.value;
    return this.currencies.find(c => c.code === currency)?.symbol || '';
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