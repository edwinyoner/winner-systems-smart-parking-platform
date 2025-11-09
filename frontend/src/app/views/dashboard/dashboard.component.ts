import { NgStyle } from '@angular/common';
import { Component, DestroyRef, DOCUMENT, effect, inject, OnInit, Renderer2, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ChartOptions } from 'chart.js';
import {
  ButtonDirective,
  ButtonGroupComponent,
  CardBodyComponent,
  CardComponent,
  CardFooterComponent,
  CardHeaderComponent,
  ColComponent,
  FormCheckLabelDirective,
  GutterDirective,
  ProgressComponent,
  RowComponent,
  TableDirective,
  BadgeComponent
} from '@coreui/angular';
import { ChartjsComponent } from '@coreui/angular-chartjs';
import { IconDirective } from '@coreui/icons-angular';

// Font Awesome
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faCar, 
  faParking, 
  faCheckCircle,
  faClock,
  faExclamationTriangle,
  faChartLine,
  faMapMarkerAlt,
  faMoneyBill
} from '@fortawesome/free-solid-svg-icons';

import { DashboardChartsData, IChartProps } from './dashboard-charts-data';

interface IEspacioEstacionamiento {
  numeroEspacio: string;
  zona: string;
  estado: string;
  placaVehiculo: string;
  horaEntrada: string;
  duracion: string;
  monto: string;
  colorEstado: string;
  colorBadge: string;
}

@Component({
  templateUrl: 'dashboard.component.html',
  styleUrls: ['dashboard.component.scss'],
  imports: [
    CardComponent, 
    CardBodyComponent, 
    RowComponent, 
    ColComponent, 
    ButtonDirective, 
    IconDirective, 
    ReactiveFormsModule, 
    ButtonGroupComponent, 
    FormCheckLabelDirective, 
    ChartjsComponent, 
    NgStyle, 
    CardFooterComponent, 
    GutterDirective, 
    ProgressComponent,
    CardHeaderComponent, 
    TableDirective,
    BadgeComponent,
    FontAwesomeModule
  ]
})
export class DashboardComponent implements OnInit {

  readonly #destroyRef: DestroyRef = inject(DestroyRef);
  readonly #document: Document = inject(DOCUMENT);
  readonly #renderer: Renderer2 = inject(Renderer2);
  readonly #chartsData: DashboardChartsData = inject(DashboardChartsData);

  // Iconos Font Awesome
  iconos = {
    carro: faCar,
    estacionamiento: faParking,
    disponible: faCheckCircle,
    reservado: faClock,
    advertencia: faExclamationTriangle,
    grafico: faChartLine,
    ubicacion: faMapMarkerAlt,
    dinero: faMoneyBill
  };

  // Estadísticas de Estacionamiento
  estadisticasEstacionamiento = {
    totalEspacios: 250,
    ocupados: 178,
    disponibles: 62,
    reservados: 10,
    tasaOcupacion: 71,
    ingresosDiarios: 850.50, // Convertido a soles peruanos
    duracionPromedio: '2.5'
  };

  // Actividades Recientes de Estacionamiento
  public espaciosEstacionamiento: IEspacioEstacionamiento[] = [
    {
      numeroEspacio: 'A-001',
      zona: 'Zona A',
      estado: 'Ocupado',
      placaVehiculo: 'ABC-123',
      horaEntrada: '10:30 AM',
      duracion: '2h 15m',
      monto: 'S/ 15.00',
      colorEstado: 'danger',
      colorBadge: 'danger'
    },
    {
      numeroEspacio: 'A-015',
      zona: 'Zona A',
      estado: 'Ocupado',
      placaVehiculo: 'XYZ-789',
      horaEntrada: '11:45 AM',
      duracion: '1h 05m',
      monto: 'S/ 8.50',
      colorEstado: 'danger',
      colorBadge: 'danger'
    },
    {
      numeroEspacio: 'B-032',
      zona: 'Zona B',
      estado: 'Reservado',
      placaVehiculo: 'DEF-456',
      horaEntrada: '02:00 PM',
      duracion: 'Pendiente',
      monto: 'S/ 20.00',
      colorEstado: 'warning',
      colorBadge: 'warning'
    },
    {
      numeroEspacio: 'C-078',
      zona: 'Zona C',
      estado: 'Disponible',
      placaVehiculo: '-',
      horaEntrada: '-',
      duracion: '-',
      monto: '-',
      colorEstado: 'success',
      colorBadge: 'success'
    },
    {
      numeroEspacio: 'B-045',
      zona: 'Zona B',
      estado: 'Ocupado',
      placaVehiculo: 'GHI-321',
      horaEntrada: '09:15 AM',
      duracion: '3h 45m',
      monto: 'S/ 22.50',
      colorEstado: 'danger',
      colorBadge: 'danger'
    },
    {
      numeroEspacio: 'A-089',
      zona: 'Zona A',
      estado: 'Ocupado',
      placaVehiculo: 'JKL-654',
      horaEntrada: '12:20 PM',
      duracion: '0h 50m',
      monto: 'S/ 6.00',
      colorEstado: 'danger',
      colorBadge: 'danger'
    }
  ];

  public mainChart: IChartProps = { type: 'line' };
  public mainChartRef: WritableSignal<any> = signal(undefined);
  #mainChartRefEffect = effect(() => {
    if (this.mainChartRef()) {
      this.setChartStyles();
    }
  });
  public chart: Array<IChartProps> = [];
  public trafficRadioGroup = new FormGroup({
    trafficRadio: new FormControl('Mes')
  });

  ngOnInit(): void {
    this.initCharts();
    this.updateChartOnColorModeChange();
  }

  initCharts(): void {
    this.mainChartRef()?.stop();
    this.mainChart = this.#chartsData.mainChart;
  }

  setTrafficPeriod(value: string): void {
    this.trafficRadioGroup.setValue({ trafficRadio: value });
    this.#chartsData.initMainChart(value);
    this.initCharts();
  }

  handleChartRef($chartRef: any) {
    if ($chartRef) {
      this.mainChartRef.set($chartRef);
    }
  }

  updateChartOnColorModeChange() {
    const unListen = this.#renderer.listen(this.#document.documentElement, 'ColorSchemeChange', () => {
      this.setChartStyles();
    });

    this.#destroyRef.onDestroy(() => {
      unListen();
    });
  }

  setChartStyles() {
    if (this.mainChartRef()) {
      setTimeout(() => {
        const options: ChartOptions = { ...this.mainChart.options };
        const scales = this.#chartsData.getScales();
        this.mainChartRef().options.scales = { ...options.scales, ...scales };
        this.mainChartRef().update();
      });
    }
  }

  // Calcular porcentaje
  obtenerPorcentaje(valor: number, total: number): number {
    return Math.round((valor / total) * 100);
  }
}