// ============================================================
// steps/step2-zones/step2-zones.component.ts
// ============================================================
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  CardModule,
  ButtonModule,
  FormModule,
  GridModule,
  AlertModule,
  BadgeModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { ZoneService } from '../../../../../core/services/parking/zone.service';
import { Zone, ZoneRequest } from '../../../../../core/models/parking/zone.model';
import { forkJoin, Observable } from 'rxjs';

@Component({
  selector: 'app-step2-zones',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    FormModule,
    GridModule,
    AlertModule,
    BadgeModule,
    IconModule,
  ],
  templateUrl: './step2-zones.component.html',
  styleUrl: './step2-zones.component.css',
})
export class Step2ZonesComponent implements OnInit {

  @Input() parkingId!: number;
  @Output() zonesCreated = new EventEmitter<Zone[]>();

  form!: FormGroup;
  loading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private zoneService: ZoneService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      zones: this.fb.array([this.buildZoneGroup()]),
    });
  }

  // ========================= FORM ARRAY =========================

  get zonesArray(): FormArray {
    return this.form.get('zones') as FormArray;
  }

  buildZoneGroup(): FormGroup {
    return this.fb.group({
      name:        ['', [Validators.required, Validators.maxLength(100)]],
      code:        ['', [Validators.required, Validators.maxLength(50),
                         Validators.pattern('^[A-Z0-9_-]+$')]],
      address:     ['', [Validators.required, Validators.maxLength(200)]],
      description: ['', Validators.maxLength(500)],
      totalSpaces: [0,  [Validators.required, Validators.min(1), Validators.max(9999)]],
    });
  }

  addZone(): void {
    this.zonesArray.push(this.buildZoneGroup());
  }

  removeZone(index: number): void {
    if (this.zonesArray.length > 1) {
      this.zonesArray.removeAt(index);
    }
  }

  // ========================= VALIDACIÓN =========================

  isInvalid(index: number, field: string): boolean {
    const ctrl = this.zonesArray.at(index).get(field);
    return !!(ctrl && ctrl.invalid && ctrl.touched);
  }

  getError(index: number, field: string): string {
    const ctrl = this.zonesArray.at(index).get(field);
    if (!ctrl || !ctrl.errors || !ctrl.touched) return '';
    if (ctrl.errors['required'])  return 'Este campo es obligatorio.';
    if (ctrl.errors['maxlength']) return `Máximo ${ctrl.errors['maxlength'].requiredLength} caracteres.`;
    if (ctrl.errors['pattern'])   return 'Solo mayúsculas, números, guiones y guiones bajos.';
    if (ctrl.errors['min'])       return 'Mínimo 1 espacio.';
    if (ctrl.errors['max'])       return 'Máximo 9999 espacios.';
    return 'Campo inválido.';
  }

  onCodeInput(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    const upper = input.value.toUpperCase();
    this.zonesArray.at(index).get('code')?.setValue(upper, { emitEvent: false });
    input.value = upper;
  }

  // Auto-genera código basado en el nombre y el índice
  autoGenerateCode(index: number): void {
    const name: string = this.zonesArray.at(index).get('name')?.value || '';
    if (!name.trim()) return;
    const prefix = name.trim().toUpperCase()
      .replace(/[^A-Z0-9]/g, '-')
      .substring(0, 8);
    const num = String(index + 1).padStart(2, '0');
    const generated = `${prefix}-Z${num}`;
    this.zonesArray.at(index).get('code')?.setValue(generated);
  }

  // Suma total de espacios de todas las zonas del formulario
  get totalSpaces(): number {
    return this.zonesArray.controls.reduce((sum, ctrl) => {
      return sum + (ctrl.get('totalSpaces')?.value || 0);
    }, 0);
  }

  // ========================= SUBMIT =========================

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const requests: Observable<Zone>[] = this.zonesArray.controls.map(ctrl => {
      const req: ZoneRequest = {
        parkingId:   this.parkingId,
        name:        ctrl.value.name.trim(),
        code:        ctrl.value.code.trim(),
        address:     ctrl.value.address.trim(),
        description: ctrl.value.description?.trim() || undefined,
        totalSpaces: ctrl.value.totalSpaces,
      };
      return this.zoneService.create(req);
    });

    // Crea todas las zonas en paralelo
    forkJoin(requests).subscribe({
      next: zones => {
        this.loading = false;
        this.zonesCreated.emit(zones);
      },
      error: err => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Error al crear las zonas. Intente nuevamente.';
      },
    });
  }
}