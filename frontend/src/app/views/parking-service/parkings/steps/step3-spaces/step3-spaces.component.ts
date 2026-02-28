// ============================================================
// steps/step3-spaces/step3-spaces.component.ts
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
  TableModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { forkJoin, Observable } from 'rxjs';
import { SpaceService } from '../../../../../core/services/parking/space.service';
import { Zone } from '../../../../../core/models/parking/zone.model';
import { Space, SpaceRequest } from '../../../../../core/models/parking/space.model';

interface SpacePreview {
  code: string;
  type: 'PARALLEL' | 'DIAGONAL' | 'PERPENDICULAR';
}

@Component({
  selector: 'app-step3-spaces',
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
    TableModule,
    IconModule,
  ],
  templateUrl: './step3-spaces.component.html',
  styleUrl: './step3-spaces.component.css',
})
export class Step3SpacesComponent implements OnInit {

  @Input() zones: Zone[] = [];
  @Output() spacesCreated = new EventEmitter<void>();

  form!: FormGroup;
  loading = false;
  errorMessage = '';

  // Preview de códigos generados por zona
  previews: Map<number, SpacePreview[]> = new Map();

  readonly spaceTypes: { value: 'PARALLEL' | 'DIAGONAL' | 'PERPENDICULAR'; label: string }[] = [
    { value: 'PARALLEL',     label: 'Paralelo'      },
    { value: 'DIAGONAL',     label: 'Diagonal'      },
    { value: 'PERPENDICULAR',label: 'Perpendicular' },
  ];

  constructor(
    private fb: FormBuilder,
    private spaceService: SpaceService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      zoneConfigs: this.fb.array(
        this.zones.map(zone => this.buildZoneConfigGroup(zone))
      ),
    });

    // Generar preview inicial para todas las zonas
    this.zones.forEach((_, i) => this.updatePreview(i));
  }

  // ========================= FORM ARRAY =========================

  get zoneConfigs(): FormArray {
    return this.form.get('zoneConfigs') as FormArray;
  }

  buildZoneConfigGroup(zone: Zone): FormGroup {
    return this.fb.group({
      zoneId:      [zone.id],
      zoneCode:    [zone.code],
      zoneName:    [zone.name],
      totalSpaces: [zone.totalSpaces ?? 1, [Validators.required, Validators.min(1), Validators.max(9999)]],
      spaceType:   ['PERPENDICULAR', Validators.required],
    });
  }

  // ========================= PREVIEW =========================

  updatePreview(index: number): void {
    const cfg = this.zoneConfigs.at(index).value;
    const total: number = cfg.totalSpaces || 0;
    const type: 'PARALLEL' | 'DIAGONAL' | 'PERPENDICULAR' = cfg.spaceType;
    const zoneCode: string = cfg.zoneCode;

    const preview: SpacePreview[] = Array.from({ length: Math.min(total, 5) }, (_, i) => ({
      code: `${zoneCode}-${String(i + 1).padStart(3, '0')}`,
      type,
    }));

    if (total > 5) {
      preview.push({ code: `... (${total - 5} más)`, type });
    }

    this.previews.set(index, preview);
  }

  getPreview(index: number): SpacePreview[] {
    return this.previews.get(index) ?? [];
  }

  // ========================= GENERACIÓN DE REQUESTS =========================

  private buildRequests(index: number): SpaceRequest[] {
    const cfg = this.zoneConfigs.at(index).value;
    return Array.from({ length: cfg.totalSpaces }, (_, i) => ({
      zoneId: cfg.zoneId,
      type:   cfg.spaceType,
      code:   `${cfg.zoneCode}-${String(i + 1).padStart(3, '0')}`,
    }));
  }

  get totalSpacesToCreate(): number {
    return this.zoneConfigs.controls.reduce(
      (sum, ctrl) => sum + (ctrl.get('totalSpaces')?.value || 0), 0
    );
  }

  // ========================= VALIDACIÓN =========================

  isInvalid(index: number, field: string): boolean {
    const ctrl = this.zoneConfigs.at(index).get(field);
    return !!(ctrl && ctrl.invalid && ctrl.touched);
  }

  getError(index: number, field: string): string {
    const ctrl = this.zoneConfigs.at(index).get(field);
    if (!ctrl || !ctrl.errors || !ctrl.touched) return '';
    if (ctrl.errors['required']) return 'Este campo es obligatorio.';
    if (ctrl.errors['min'])      return 'Mínimo 1 espacio.';
    if (ctrl.errors['max'])      return 'Máximo 9999 espacios.';
    return 'Campo inválido.';
  }

  // ========================= SUBMIT =========================

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    // Crea los espacios de todas las zonas en paralelo (batch por zona)
    const batchRequests: Observable<Space[]>[] = this.zones.map((_, i) =>
      this.spaceService.createBatch(this.buildRequests(i))
    );

    forkJoin(batchRequests).subscribe({
      next: () => {
        this.loading = false;
        this.spacesCreated.emit();
      },
      error: err => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Error al generar los espacios. Intente nuevamente.';
      },
    });
  }
}