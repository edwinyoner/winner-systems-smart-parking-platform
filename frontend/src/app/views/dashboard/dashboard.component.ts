import { NgStyle, DatePipe, DecimalPipe, CommonModule } from '@angular/common';
import { Component, DestroyRef, effect, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
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
  BadgeComponent,
  SpinnerComponent
} from '@coreui/angular';
import { ChartjsComponent } from '@coreui/angular-chartjs';
import { IconDirective } from '@coreui/icons-angular';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faCar, 
  faParking, 
  faCheckCircle,
  faClock,
  faExclamationTriangle,
  faChartLine,
  faMapMarkerAlt,
  faMoneyBill,
  faTools,
  faBan,
  faUsers,
  faCarSide,
  faTicketAlt,
  faTachometerAlt,
  faMapMarked,
  faSquareParking
} from '@fortawesome/free-solid-svg-icons';

import { DashboardChartsData, IChartProps } from './dashboard-charts-data';
import { ParkingService } from '../../core/services/parking/parking.service';
import { ZoneService } from '../../core/services/parking/zone.service';
import { SpaceService } from '../../core/services/parking/space.service';
import { TransactionService } from '../../core/services/parking/transaction.service';
import { CustomerService } from '../../core/services/parking/customer.service';
import { VehicleService } from '../../core/services/parking/vehicle.service';
import { InfractionService } from '../../core/services/parking/infraction.service';
import { PaymentService } from '../../core/services/parking/payment.service';

import { Parking } from '../../core/models/parking/parking.model';
import { Zone } from '../../core/models/parking/zone.model';
import { Space } from '../../core/models/parking/space.model';
import { ActiveTransactionDto, TransactionDto } from '../../core/models/parking/transaction.model';
import { Customer } from '../../core/models/parking/customer.model';
import { Vehicle } from '../../core/models/parking/vehicle.model';
import { Infraction } from '../../core/models/parking/infraction.model';
import { PaymentDto } from '../../core/models/parking/payment.model';

import { interval, Subscription, forkJoin, Observable } from 'rxjs';
import { switchMap, startWith, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { Router } from '@angular/router';

/**
 * Datos de respuesta paginados
 */
interface PaginatedResponse<T> {
  items: T[];
  totalElements: number;
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}

/**
 * Estadísticas completas del sistema
 */
interface SystemStats {
  // Parkings
  totalParkings: number;
  activeParkings: number;
  inactiveParkings: number;
  maintenanceParkings: number;
  
  // Zones
  totalZones: number;
  activeZones: number;
  
  // Spaces
  totalSpaces: number;
  availableSpaces: number;
  occupiedSpaces: number;
  maintenanceSpaces: number;
  outOfServiceSpaces: number;
  occupancyRate: number;
  
  // Transactions
  activeTransactions: number;
  todayTransactions: number;
  todayCompletedTransactions: number;
  
  // Financial
  todayRevenue: number;
  monthRevenue: number;
  averageTicket: number;
  
  // Customers & Vehicles
  totalCustomers: number;
  recurrentCustomers: number;
  totalVehicles: number;
  
  // Infractions
  pendingInfractions: number;
  todayInfractions: number;
  
  // Averages
  averageDuration: string;
  averageOccupancyRate: number;
}

/**
 * Estadísticas por parking individual
 */
interface ParkingStats extends Parking {
  zonesCount: number;
  activeZonesCount: number;
  activeTransactionsCount: number;
}

/**
 * Estadísticas por zona
 */
interface ZoneStats extends Zone {
  spacesCount: number;
  occupiedCount: number;
  availableCount: number;
  occupancyRate: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: 'dashboard.component.html',
  styleUrls: ['dashboard.component.css'],
  imports: [
    CommonModule,
    DatePipe,
    DecimalPipe,
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
    FontAwesomeModule,
    SpinnerComponent
  ]
})
export class DashboardComponent implements OnInit {

  readonly #destroyRef: DestroyRef = inject(DestroyRef);
  readonly #chartsData: DashboardChartsData = inject(DashboardChartsData);
  readonly #router = inject(Router);
  
  // Services
  readonly #parkingService = inject(ParkingService);
  readonly #zoneService = inject(ZoneService);
  readonly #spaceService = inject(SpaceService);
  readonly #transactionService = inject(TransactionService);
  readonly #customerService = inject(CustomerService);
  readonly #vehicleService = inject(VehicleService);
  readonly #infractionService = inject(InfractionService);
  readonly #paymentService = inject(PaymentService);

  // ========================= ICONOS =========================
  icons = {
    car: faCar,
    parking: faParking,
    available: faCheckCircle,
    occupied: faClock,
    warning: faExclamationTriangle,
    chart: faChartLine,
    location: faMapMarkerAlt,
    money: faMoneyBill,
    maintenance: faTools,
    blocked: faBan,
    users: faUsers,
    vehicle: faCarSide,
    infraction: faTicketAlt,
    dashboard: faTachometerAlt,
    zone: faMapMarked,
    space: faSquareParking
  };

  // ========================= ESTADO =========================
  loading = signal<boolean>(true);
  error = signal<string | null>(null);
  lastUpdate = signal<Date>(new Date());
  
  // Estadísticas principales
  systemStats = signal<SystemStats>({
    totalParkings: 0,
    activeParkings: 0,
    inactiveParkings: 0,
    maintenanceParkings: 0,
    totalZones: 0,
    activeZones: 0,
    totalSpaces: 0,
    availableSpaces: 0,
    occupiedSpaces: 0,
    maintenanceSpaces: 0,
    outOfServiceSpaces: 0,
    occupancyRate: 0,
    activeTransactions: 0,
    todayTransactions: 0,
    todayCompletedTransactions: 0,
    todayRevenue: 0,
    monthRevenue: 0,
    averageTicket: 0,
    totalCustomers: 0,
    recurrentCustomers: 0,
    totalVehicles: 0,
    pendingInfractions: 0,
    todayInfractions: 0,
    averageDuration: '0h 0min',
    averageOccupancyRate: 0
  });

  // Datos detallados
  parkings = signal<ParkingStats[]>([]);
  topZones = signal<ZoneStats[]>([]);
  activeTransactions = signal<ActiveTransactionDto[]>([]);
  recentTransactions = signal<TransactionDto[]>([]);
  recentPayments = signal<PaymentDto[]>([]);
  topCustomers = signal<Customer[]>([]);
  topVehicles = signal<Vehicle[]>([]);
  recentInfractions = signal<Infraction[]>([]);

  // ========================= GRÁFICOS =========================
  public mainChart: IChartProps = { type: 'line' };
  public mainChartRef: WritableSignal<any> = signal(undefined);
  
  #mainChartRefEffect = effect(() => {
    if (this.mainChartRef()) {
      this.setChartStyles();
    }
  });

  public trafficRadioGroup = new FormGroup({
    trafficRadio: new FormControl('Mes')
  });

  // ========================= AUTO-REFRESH =========================
  private refreshSubscription?: Subscription;
  private readonly REFRESH_INTERVAL = 30000; // 30 segundos

  // ========================= LIFECYCLE =========================

  ngOnInit(): void {
    this.initCharts();
    this.loadAllData();
    this.startAutoRefresh();
  }

  ngOnDestroy(): void {
    this.stopAutoRefresh();
  }

  // ========================= CARGAR DATOS =========================

  /**
   * Carga TODOS los datos del sistema
   */
  private loadAllData(): void {
    this.loading.set(true);
    this.error.set(null);

    forkJoin({
      parkings: this.#parkingService.getAll(0, 100).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      zones: this.#zoneService.getAll(0, 100).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      spaces: this.#spaceService.getAll(0, 100).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      activeTransactions: this.#transactionService.getActiveTransactions(0, 10).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      recentTransactions: this.#transactionService.getAll(0, 10).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      customers: this.#customerService.getAll({ pageNumber: 0, pageSize: 10 }).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      recurrentCustomers: this.#customerService.getRecurrent(0, 10).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      vehicles: this.#vehicleService.getAll({ pageNumber: 0, pageSize: 10 }).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }))),
      payments: of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 }),
      infractions: this.#infractionService.getAll(0, 10).pipe(catchError(() => of({ items: [], totalElements: 0, totalPages: 0, pageNumber: 0, pageSize: 0 })))
    }).subscribe({
      next: (data) => {
        this.processDashboardData(data);
        this.loading.set(false);
        this.lastUpdate.set(new Date());
      },
      error: (err) => {
        console.error('Error cargando datos del dashboard:', err);
        this.error.set('Error al cargar datos del sistema');
        this.loading.set(false);
      }
    });
  }

  /**
   * Procesa y calcula estadísticas
   */
  private processDashboardData(data: any): void {
    const parkings = data.parkings.items || [];
    const zones = data.zones.items || [];
    const spaces = data.spaces.items || [];
    const activeTransactions = data.activeTransactions.items || [];
    const recentTransactions = data.recentTransactions.items || [];
    const payments = data.payments.items || [];
    const infractions = data.infractions.items || [];

    // ========== CALCULAR ESTADÍSTICAS DEL SISTEMA ==========
    const stats: SystemStats = {
      // Parkings
      totalParkings: parkings.length,
      activeParkings: parkings.filter((p: Parking) => p.status === 'ACTIVE').length,
      inactiveParkings: parkings.filter((p: Parking) => p.status === 'INACTIVE').length,
      maintenanceParkings: parkings.filter((p: Parking) => p.status === 'MAINTENANCE').length,
      
      // Zones
      totalZones: zones.length,
      activeZones: zones.filter((z: Zone) => z.status === 'ACTIVE').length,
      
      // Spaces
      totalSpaces: spaces.length,
      availableSpaces: spaces.filter((s: Space) => s.status === 'AVAILABLE').length,
      occupiedSpaces: spaces.filter((s: Space) => s.status === 'OCCUPIED').length,
      maintenanceSpaces: spaces.filter((s: Space) => s.status === 'MAINTENANCE').length,
      outOfServiceSpaces: spaces.filter((s: Space) => s.status === 'OUT_OF_SERVICE').length,
      occupancyRate: 0,
      
      // Transactions
      activeTransactions: activeTransactions.length,
      todayTransactions: recentTransactions.length,
      todayCompletedTransactions: recentTransactions.filter((t: TransactionDto) => t.status === 'COMPLETED').length,
      
      // Financial
      todayRevenue: activeTransactions.reduce((sum: number, t: ActiveTransactionDto) => sum + (t.currentAmount || 0), 0),
      monthRevenue: 0,
      averageTicket: 0,
      
      // Customers & Vehicles
      totalCustomers: data.customers.totalElements || 0,
      recurrentCustomers: data.recurrentCustomers.totalElements || 0,
      totalVehicles: data.vehicles.totalElements || 0,
      
      // Infractions
      pendingInfractions: infractions.filter((i: Infraction) => i.status === 'PENDING').length,
      todayInfractions: infractions.length,
      
      // Averages
      averageDuration: '2h 30min',
      averageOccupancyRate: 0
    };

    // Calcular tasa de ocupación
    if (stats.totalSpaces > 0) {
      stats.occupancyRate = Math.round((stats.occupiedSpaces / stats.totalSpaces) * 100);
    }

    // Calcular ticket promedio
    if (payments.length > 0) {
      const totalPayments = payments.reduce((sum: number, p: PaymentDto) => sum + (p.amount || 0), 0);
      stats.averageTicket = totalPayments / payments.length;
    }

    // Calcular ingresos del mes (suma de todos los pagos)
    stats.monthRevenue = payments.reduce((sum: number, p: PaymentDto) => sum + (p.amount || 0), 0);

    this.systemStats.set(stats);

    // ========== PROCESAR PARKINGS CON DETALLES ==========
    const parkingStats: ParkingStats[] = parkings.map((p: Parking) => {
      const parkingZones = zones.filter((z: Zone) => z.parkingId === p.id);
      return {
        ...p,
        zonesCount: parkingZones.length,
        activeZonesCount: parkingZones.filter((z: Zone) => z.status === 'ACTIVE').length,
        activeTransactionsCount: activeTransactions.filter((t: ActiveTransactionDto) => t.parkingId === p.id).length
      };
    });
    this.parkings.set(parkingStats);

    // ========== PROCESAR TOP ZONAS ==========
    const zoneStats: ZoneStats[] = zones.map((z: Zone) => {
      const zoneSpaces = spaces.filter((s: Space) => s.zoneId === z.id);
      const occupied = zoneSpaces.filter((s: Space) => s.status === 'OCCUPIED').length;
      const available = zoneSpaces.filter((s: Space) => s.status === 'AVAILABLE').length;
      
      return {
        ...z,
        spacesCount: zoneSpaces.length,
        occupiedCount: occupied,
        availableCount: available,
        occupancyRate: zoneSpaces.length > 0 ? Math.round((occupied / zoneSpaces.length) * 100) : 0
      };
    }).sort((a: ZoneStats, b: ZoneStats) => b.occupancyRate - a.occupancyRate).slice(0, 5);
    
    this.topZones.set(zoneStats);

    // ========== ASIGNAR DATOS ==========
    this.activeTransactions.set(activeTransactions);
    this.recentTransactions.set(recentTransactions);
    this.recentPayments.set(payments);
    this.topCustomers.set(data.recurrentCustomers.items || []);
    this.topVehicles.set(data.vehicles.items || []);
    this.recentInfractions.set(infractions);
  }

  /**
   * Inicia actualización automática
   */
  private startAutoRefresh(): void {
    this.refreshSubscription = interval(this.REFRESH_INTERVAL)
      .subscribe(() => {
        this.loadAllData();
      });
  }

  /**
   * Detiene actualización automática
   */
  private stopAutoRefresh(): void {
    this.refreshSubscription?.unsubscribe();
  }

  /**
   * Refresco manual
   */
  refreshData(): void {
    this.loadAllData();
  }

  // ========================= NAVEGACIÓN =========================

  navigateTo(route: string): void {
    this.#router.navigate([route]);
  }

  // ========================= GRÁFICOS =========================

  initCharts(): void {
    this.mainChartRef()?.stop();
    this.mainChart = this.#chartsData.mainChart;
  }

  setTrafficPeriod(value: string): void {
    this.trafficRadioGroup.setValue({ trafficRadio: value });
    this.#chartsData.initMainChart(value);
    this.initCharts();
  }

  handleChartRef($chartRef: any): void {
    if ($chartRef) {
      this.mainChartRef.set($chartRef);
    }
  }

  setChartStyles(): void {
    if (this.mainChartRef()) {
      setTimeout(() => {
        const options = { ...this.mainChart.options };
        const scales = this.#chartsData.getScales();
        this.mainChartRef().options.scales = { ...options.scales, ...scales };
        this.mainChartRef().update();
      });
    }
  }

  // ========================= UTILIDADES =========================

  getPercentage(value: number, total: number): number {
    return total > 0 ? Math.round((value / total) * 100) : 0;
  }

  getStatusColor(status: string): string {
    const colorMap: Record<string, string> = {
      'ACTIVE': 'success',
      'OCCUPIED': 'danger',
      'AVAILABLE': 'success',
      'MAINTENANCE': 'warning',
      'COMPLETED': 'info',
      'CANCELLED': 'secondary',
      'INACTIVE': 'secondary',
      'OUT_OF_SERVICE': 'dark',
      'PENDING': 'warning',
      'PAID': 'success'
    };
    return colorMap[status] || 'secondary';
  }

  getTransactionBadge(transaction: ActiveTransactionDto): { text: string; color: string } {
    if (transaction.isOverdue) {
      return { text: 'SOBREPASADO', color: 'danger' };
    }
    if (transaction.requiresAttention) {
      return { text: 'ATENCIÓN', color: 'warning' };
    }
    return { text: 'ACTIVO', color: 'success' };
  }

  getParkingStatusText(status: string): string {
    const statusMap: Record<string, string> = {
      'ACTIVE': 'Activo',
      'INACTIVE': 'Inactivo',
      'MAINTENANCE': 'Mantenimiento',
      'OUT_OF_SERVICE': 'Fuera de Servicio'
    };
    return statusMap[status] || status;
  }
}