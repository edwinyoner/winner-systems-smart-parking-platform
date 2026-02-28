
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  ProgressModule,
  ButtonModule,
  CardModule,
  BadgeModule,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { Zone } from '../../../../core/models/parking/zone.model';
import { Step1InfoComponent } from '../steps/step1-info/step1-info.component';
import { Step2ZonesComponent } from '../steps/step2-zones/step2-zones.component';
import { Step3SpacesComponent } from '../steps/step3-spaces/step3-spaces.component';
import { Step4ConfigComponent } from '../steps/step4-config/step4-config.component';
import { Step5OperatorsComponent } from '../steps/step5-operators/step5-operators.component';

interface StepConfig {
  number: number;
  label: string;
  icon: string;
}

@Component({
  selector: 'app-parking-create',
  standalone: true,
  imports: [
    CommonModule,
    ProgressModule,
    ButtonModule,
    CardModule,
    BadgeModule,
    IconModule,
    Step1InfoComponent,
    Step2ZonesComponent,
    Step3SpacesComponent,
    Step4ConfigComponent,
    Step5OperatorsComponent,
  ],
  templateUrl: './parking-create.component.html',
  styleUrl: './parking-create.component.css',
})
export class ParkingCreateComponent {

  // ========================= ESTADO DEL STEPPER =========================

  currentStep = 1;
  totalSteps  = 5;

  // IDs acumulados entre pasos
  parkingId: number | null = null;
  createdZones: Zone[]    = [];

  // ========================= CONFIGURACIÓN DE PASOS =========================

  steps: StepConfig[] = [
    { number: 1, label: 'Información',  icon: 'cilInfo'        },
    { number: 2, label: 'Zonas',        icon: 'cilMap'         },
    { number: 3, label: 'Espacios',     icon: 'cilGrid'        },
    { number: 4, label: 'Configuración',icon: 'cilSettings'    },
    { number: 5, label: 'Operadores',   icon: 'cilPeople'      },
  ];

  constructor(private router: Router) {}

  // Getter para extraer solo los IDs de las zonas creadas
  get zoneIds(): number[] {
    return this.createdZones
      .filter(z => z.id != null)
      .map(z => z.id!);
  }

  // ========================= NAVEGACIÓN =========================

  get progressValue(): number {
    return ((this.currentStep - 1) / (this.totalSteps - 1)) * 100;
  }

  isStepCompleted(step: number): boolean {
    return step < this.currentStep;
  }

  isStepActive(step: number): boolean {
    return step === this.currentStep;
  }

  // ========================= CALLBACKS DE CADA PASO =========================

  // Step 1 → parking creado, recibe su ID
  onParkingCreated(parkingId: number): void {
    this.parkingId = parkingId;
    this.currentStep = 2;
  }

  // Step 2 → zonas creadas, recibe lista completa de zonas
  onZonesCreated(zones: Zone[]): void {
    this.createdZones = zones;
    this.currentStep = 3;
  }

  // Step 3 → espacios generados, avanza al paso 4
  onSpacesCreated(): void {
    this.currentStep = 4;
  }

  // Step 4 → turnos/tarifas configurados, avanza al paso 5
  onConfigSaved(): void {
    this.currentStep = 5;
  }

  // Step 5 → todo completo, navega al listado
  onCompleted(): void {
    this.router.navigate(['/parking-service/parkings']);
  }

  // ========================= CANCELAR =========================

  onCancel(): void {
    this.router.navigate(['/parking-service/parkings']);
  }
}