// src/app/views/parking-service/rates/rate-edit/rate-edit.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

// Services
import { RateService } from '../../../../core/services/parking/rate.service';

// Models
import { Rate } from '../../../../core/models/parking/rate.model';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-rate-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AlertMessageComponent
  ],
  templateUrl: './rate-edit.component.html',
  styleUrls: ['./rate-edit.component.css']
})
export class RateEditComponent implements OnInit {

  rateId!: number;
  rateForm!: FormGroup;
  isLoading = true;
  isSubmitting = false;
  errorMessage: string | null = null;
  currentRate: Rate | null = null;

  currencies = [
    { code: 'PEN', name: 'Soles Peruanos (S/.)', symbol: 'S/.' },
    { code: 'USD', name: 'Dólares Americanos ($)', symbol: '$' },
    { code: 'EUR', name: 'Euros (€)', symbol: '€' }
  ];

  constructor(
    private fb: FormBuilder,
    private rateService: RateService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.rateId = Number(this.route.snapshot.paramMap.get('id'));
    this.initForm();
    this.loadRate();
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

  private loadRate(): void {
    this.isLoading = true;
    
    this.rateService.getById(this.rateId).subscribe({
      next: (rate: Rate) => {
        this.currentRate = rate;
        this.rateForm.patchValue({
          name: rate.name,
          description: rate.description,
          amount: rate.amount,
          currency: rate.currency,
          status: rate.status
        });
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar la tarifa';
        console.error(error);
      }
    });
  }

  onSubmit(): void {
    if (this.rateForm.invalid) {
      this.markFormGroupTouched(this.rateForm);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;

    this.rateService.update(this.rateId, this.rateForm.value).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.router.navigate(['/rates'], {
          state: { successMessage: `Tarifa "${response.name}" actualizada exitosamente` }
        });
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Error al actualizar la tarifa';
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    });
  }

  toggleStatus(): void {
    const currentStatus = this.rateForm.get('status')?.value;
    this.rateForm.patchValue({ status: !currentStatus });
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