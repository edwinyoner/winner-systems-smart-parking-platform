// ============================================================
// steps/step1-info/step1-info.component.ts
// ============================================================
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  CardModule,
  ButtonModule,
  FormModule,
  GridModule,
  AlertModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { ParkingService } from '../../../../../core/services/parking/parking.service';
import { ParkingRequest } from '../../../../../core/models/parking/parking.model';

@Component({
  selector: 'app-step1-info',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    FormModule,
    GridModule,
    AlertModule,
    IconModule,
  ],
  templateUrl: './step1-info.component.html',
  styleUrl: './step1-info.component.css',
})
export class Step1InfoComponent implements OnInit {

  @Output() parkingCreated = new EventEmitter<number>();

  form!: FormGroup;
  loading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private parkingService: ParkingService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name:        ['', [Validators.required, Validators.maxLength(100)]],
      code:        ['', [Validators.required, Validators.maxLength(20),
                         Validators.pattern('^[A-Z0-9_-]+$')]],
      address:     ['', [Validators.required, Validators.maxLength(255)]],
      description: ['', Validators.maxLength(500)],
      latitude:    [null, [Validators.min(-90),  Validators.max(90)]],
      longitude:   [null, [Validators.min(-180), Validators.max(180)]],
      managerId:   [null],
    });
  }

  // ========================= HELPERS DE VALIDACIÓN =========================

  isInvalid(field: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl && ctrl.invalid && ctrl.touched);
  }

  getError(field: string): string {
    const ctrl = this.form.get(field);
    if (!ctrl || !ctrl.errors || !ctrl.touched) return '';
    if (ctrl.errors['required'])    return 'Este campo es obligatorio.';
    if (ctrl.errors['maxlength'])   return `Máximo ${ctrl.errors['maxlength'].requiredLength} caracteres.`;
    if (ctrl.errors['pattern'])     return 'Solo mayúsculas, números, guiones y guiones bajos.';
    if (ctrl.errors['min'])         return 'Valor fuera de rango.';
    if (ctrl.errors['max'])         return 'Valor fuera de rango.';
    return 'Campo inválido.';
  }

  // Convierte el código a mayúsculas automáticamente
  onCodeInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const upper = input.value.toUpperCase();
    this.form.get('code')?.setValue(upper, { emitEvent: false });
    input.value = upper;
  }

  // ========================= SUBMIT =========================

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const request: ParkingRequest = {
      name:        this.form.value.name.trim(),
      code:        this.form.value.code.trim(),
      address:     this.form.value.address.trim(),
      description: this.form.value.description?.trim() || undefined,
      latitude:    this.form.value.latitude   || undefined,
      longitude:   this.form.value.longitude  || undefined,
      managerId:   this.form.value.managerId  || undefined,
    };

    this.parkingService.create(request).subscribe({
      next: parking => {
        this.loading = false;
        this.parkingCreated.emit(parking.id!);
      },
      error: err => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Error al crear el parqueo. Intente nuevamente.';
      },
    });
  }
}