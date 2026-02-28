// ============================================================
// steps/step5-operators/step5-operators.component.ts
// ============================================================
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  CardModule,
  ButtonModule,
  FormModule,
  GridModule,
  AlertModule,
  BadgeModule,
  TableModule,
  SpinnerModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { UserService } from '../../../../../core/services/user.service';
import { ShiftService } from '../../../../../core/services/parking/shift.service';
import { ZoneService } from '../../../../../core/services/parking/zone.service';
import { User } from '../../../../../core/models/user.model';
import { Shift } from '../../../../../core/models/parking/shift.model';
import { forkJoin } from 'rxjs';

interface OperatorOption {
  id: number;
  name: string;
  email: string;
}

interface AssignmentRow {
  operatorId: number;
  operatorName: string;
  zoneId: number;
  shiftId: number;
  daysOfWeek: string[];
}

const ALL_DAYS = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

@Component({
  selector: 'app-step5-operators',
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
    SpinnerModule,
    IconModule,
  ],
  templateUrl: './step5-operators.component.html',
  styleUrl: './step5-operators.component.css',
})
export class Step5OperatorsComponent implements OnInit {

  @Input() zoneIds: number[] = [];
  @Output() completed = new EventEmitter<void>();

  // Catálogos
  operators: OperatorOption[] = [];
  shifts: Shift[]             = [];
  zones: { id: number; name: string; code: string }[] = [];

  // Asignaciones confirmadas
  assignments: AssignmentRow[] = [];

  // Formulario de nueva asignación
  form!: FormGroup;
  selectedDays = new Set<string>();
  allDays      = ALL_DAYS;

  // Estado
  loadingCatalogs = true;
  loadingSave     = false;
  errorMessage    = '';
  catalogError    = '';

  constructor(
    private fb:          FormBuilder,
    private userService: UserService,
    private shiftService: ShiftService,
    private zoneService:  ZoneService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      operatorId: [null, Validators.required],
      zoneId:     [null, Validators.required],
      shiftId:    [null, Validators.required],
    });
    this.loadCatalogs();
  }

  // ========================= CATÁLOGOS =========================

  loadCatalogs(): void {
    this.loadingCatalogs = true;
    this.catalogError    = '';

    forkJoin({
      users:  this.userService.getByRole('OPERADOR'),
      shifts: this.shiftService.getActive(),
    }).subscribe({
      next: ({ users, shifts }) => {
        this.operators = users.map((u: User) => ({
          id:    u.id!,
          name:  `${u.firstName} ${u.lastName}`,
          email: u.email,
        }));
        this.shifts = shifts;

        // Cargar nombres de zonas para mostrar en la tabla
        forkJoin(this.zoneIds.map(id => this.zoneService.getById(id))).subscribe({
          next: zones => {
            this.zones = zones.map(z => ({ id: z.id!, name: z.name, code: z.code }));
            this.loadingCatalogs = false;
          },
          error: () => {
            // Si falla, usamos solo los IDs
            this.zones = this.zoneIds.map(id => ({ id, name: `Zona ${id}`, code: '' }));
            this.loadingCatalogs = false;
          },
        });
      },
      error: () => {
        this.loadingCatalogs = false;
        this.catalogError = 'Error al cargar operadores y turnos. Intente recargar.';
      },
    });
  }

  // ========================= DÍAS =========================

  toggleDay(day: string): void {
    this.selectedDays.has(day)
      ? this.selectedDays.delete(day)
      : this.selectedDays.add(day);
  }

  isDaySelected(day: string): boolean {
    return this.selectedDays.has(day);
  }

  getDayLabel(day: string): string {
    return day.charAt(0) + day.slice(1, 3).toLowerCase();
  }

  // ========================= AGREGAR ASIGNACIÓN =========================

  addAssignment(): void {
    if (this.form.invalid || this.selectedDays.size === 0) {
      this.form.markAllAsTouched();
      return;
    }

    const { operatorId, zoneId, shiftId } = this.form.value;

    // Verificar duplicado
    const exists = this.assignments.some(
      a => a.operatorId === operatorId && a.zoneId === zoneId && a.shiftId === shiftId
    );
    if (exists) {
      this.errorMessage = 'Esta combinación operador/zona/turno ya fue agregada.';
      return;
    }

    const op   = this.operators.find(o => o.id === +operatorId);
    const zone = this.zones.find(z => z.id === +zoneId);

    this.assignments.push({
      operatorId,
      operatorName: op ? op.name : `Operador ${operatorId}`,
      zoneId:       +zoneId,
      shiftId:      +shiftId,
      daysOfWeek:   [...this.selectedDays],
    });

    // Resetear formulario
    this.form.reset();
    this.selectedDays.clear();
    this.errorMessage = '';
  }

  removeAssignment(index: number): void {
    this.assignments.splice(index, 1);
  }

  getZoneName(id: number): string {
    return this.zones.find(z => z.id === id)?.code ?? `Zona ${id}`;
  }

  getShiftName(id: number): string {
    return this.shifts.find(s => s.id === id)?.name ?? `Turno ${id}`;
  }

  // ========================= FINALIZAR =========================

  onFinish(): void {
    // Las asignaciones son opcionales — se puede finalizar sin ellas
    this.completed.emit();
  }
}