// src/app/views/parking-service/transactions/active-transactions/active-transactions.component.ts

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  CardComponent,
  CardBodyComponent,
  CardHeaderComponent,
  ColComponent,
  RowComponent,
  ModalComponent,
  ModalHeaderComponent,
  ModalTitleDirective,
  ModalBodyComponent,
  ModalFooterComponent,
  ButtonDirective,
  BadgeComponent,
  SpinnerComponent
} from '@coreui/angular';

import { ParkingService } from '../../../../core/services/parking/parking.service';
import { ZoneService } from '../../../../core/services/parking/zone.service';
import { SpaceService } from '../../../../core/services/parking/space.service';
import { TransactionService } from '../../../../core/services/parking/transaction.service';

import { Parking } from '../../../../core/models/parking/parking.model';
import { Zone } from '../../../../core/models/parking/zone.model';
import { Space } from '../../../../core/models/parking/space.model';
import { TransactionDetailResponse, ActiveTransactionDto } from '../../../../core/models/parking/transaction.model';

interface SpaceWithTransaction extends Space {
  transaction?: TransactionDetailResponse;
  elapsedTime?: string;
}

@Component({
  selector: 'app-active-transactions',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardComponent,
    CardBodyComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent,
    ModalComponent,
    ModalHeaderComponent,
    ModalTitleDirective,
    ModalBodyComponent,
    ModalFooterComponent,
    ButtonDirective,
    BadgeComponent,
    SpinnerComponent
  ],
  templateUrl: './active-transactions.component.html',
  styleUrls: ['./active-transactions.component.css']
})
export class ActiveTransactionsComponent implements OnInit, OnDestroy {
  
  // ========================= DATOS DE FILTROS =========================
  parkings: Parking[] = [];
  zones: Zone[] = [];
  
  selectedParkingId: number | null = null;
  selectedZoneId: number | null = null;
  
  // ========================= DATOS DE ESPACIOS =========================
  spaces: SpaceWithTransaction[] = [];
  
  // ========================= ESTADÍSTICAS =========================
  stats = {
    total: 0,
    available: 0,
    occupied: 0,
    maintenance: 0,
    outOfService: 0,
    occupancyPercentage: 0
  };
  
  // ========================= MODAL =========================
  selectedSpace: SpaceWithTransaction | null = null;
  isModalVisible = false;
  
  // ========================= ESTADO DE CARGA =========================
  loading = false;
  loadingZones = false;
  
  // ========================= AUTO-REFRESH =========================
  private refreshInterval: any;
  
  constructor(
    private parkingService: ParkingService,
    private zoneService: ZoneService,
    private spaceService: SpaceService,
    private transactionService: TransactionService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.loadParkings();
    this.startAutoRefresh();
  }
  
  ngOnDestroy(): void {
    this.stopAutoRefresh();
  }
  
  // ========================= CARGA INICIAL =========================
  
  loadParkings(): void {
    this.parkingService.getActive().subscribe({
      next: (parkings) => {
        this.parkings = parkings;
      },
      error: (error) => {
        console.error('Error loading parkings:', error);
      }
    });
  }
  
  // ========================= EVENTOS DE FILTROS =========================
  
  onParkingChange(): void {
    this.selectedZoneId = null;
    this.zones = [];
    this.spaces = [];
    this.resetStats();
    
    if (this.selectedParkingId) {
      this.loadZones();
    }
  }
  
  onZoneChange(): void {
    if (this.selectedZoneId) {
      this.loadSpaces();
    } else {
      this.spaces = [];
      this.resetStats();
    }
  }
  
  loadZones(): void {
    if (!this.selectedParkingId) return;
    
    this.loadingZones = true;
    this.zoneService.getByParkingId(this.selectedParkingId).subscribe({
      next: (zones) => {
        this.zones = zones.filter(z => z.status === 'ACTIVE');
        this.loadingZones = false;
      },
      error: (error) => {
        console.error('Error loading zones:', error);
        this.loadingZones = false;
      }
    });
  }
  
  // ========================= CARGA DE ESPACIOS Y TRANSACCIONES =========================
  
  loadSpaces(): void {
    if (!this.selectedZoneId) return;
    
    this.loading = true;
    this.spaceService.getByZoneId(this.selectedZoneId).subscribe({
      next: (spaces) => {
        this.spaces = spaces;
        this.loadActiveTransactions();
      },
      error: (error) => {
        console.error('Error loading spaces:', error);
        this.loading = false;
      }
    });
  }
  
  loadActiveTransactions(): void {
    if (!this.selectedZoneId) return;
    
    this.transactionService.getActiveByZone(this.selectedZoneId, 0, 500).subscribe({
      next: (response) => {
        const activeTransactions = response.content;
        
        // Mapear transacciones a espacios
        this.spaces = this.spaces.map(space => {
          const transaction = activeTransactions.find(t => t.spaceId === space.id);
          
          return {
            ...space,
            transaction: transaction ? this.convertToDetailResponse(transaction) : undefined,
            elapsedTime: transaction ? transaction.elapsedFormatted : undefined
          };
        });
        
        this.calculateStats();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading active transactions:', error);
        this.calculateStats();
        this.loading = false;
      }
    });
  }
  
  // Convertir ActiveTransactionDto a TransactionDetailResponse
  private convertToDetailResponse(activeTransaction: ActiveTransactionDto): TransactionDetailResponse {
    return {
      id: activeTransaction.id,
      status: 'ACTIVE',
      paymentStatus: 'PENDING',
      vehicle: {
        id: activeTransaction.vehicleId,
        plateNumber: activeTransaction.plateNumber
      },
      customer: {
        id: activeTransaction.customerId,
        documentNumber: activeTransaction.documentNumber,
        name: activeTransaction.customerName,
        phone: activeTransaction.customerPhone,
        email: activeTransaction.customerEmail
      },
      parking: {
        id: activeTransaction.parkingId,
        name: activeTransaction.parkingName,
        code: ''
      },
      zone: {
        id: activeTransaction.zoneId,
        name: activeTransaction.zoneName,
        code: ''
      },
      space: {
        id: activeTransaction.spaceId,
        code: activeTransaction.spaceCode
      },
      entryTime: activeTransaction.entryTime,
      durationMinutes: activeTransaction.elapsedMinutes,
      durationFormatted: activeTransaction.elapsedFormatted,
      entryDocument: {
        number: activeTransaction.documentNumber
      },
      rate: {
        id: 0,
        name: '',
        hourlyRate: activeTransaction.hourlyRate
      },
      calculatedAmount: activeTransaction.currentAmount,
      discountAmount: 0,
      totalAmount: activeTransaction.currentAmount,
      currency: activeTransaction.currency,
      receiptSent: false,
      createdAt: activeTransaction.createdAt,
      updatedAt: activeTransaction.createdAt,
      entryPhotoUrl: activeTransaction.entryPhotoUrl,
      entryPlateConfidence: activeTransaction.plateConfidence,
      notes: activeTransaction.notes
    };
  }
  
  // ========================= CÁLCULO DE ESTADÍSTICAS =========================
  
  calculateStats(): void {
    this.stats.total = this.spaces.length;
    this.stats.available = this.spaces.filter(s => s.status === 'AVAILABLE').length;
    this.stats.occupied = this.spaces.filter(s => s.status === 'OCCUPIED').length;
    this.stats.maintenance = this.spaces.filter(s => s.status === 'MAINTENANCE').length;
    this.stats.outOfService = this.spaces.filter(s => s.status === 'OUT_OF_SERVICE').length;
    
    this.stats.occupancyPercentage = this.stats.total > 0
      ? Math.round((this.stats.occupied / this.stats.total) * 100)
      : 0;
  }
  
  resetStats(): void {
    this.stats = {
      total: 0,
      available: 0,
      occupied: 0,
      maintenance: 0,
      outOfService: 0,
      occupancyPercentage: 0
    };
  }
  
  // ========================= EVENTOS DE CLICK =========================
  
  onSpaceClick(space: SpaceWithTransaction): void {
    this.selectedSpace = space;
    this.isModalVisible = true;
  }
  
  closeModal(): void {
    this.isModalVisible = false;
    this.selectedSpace = null;
  }
  
  goToTransactionDetail(): void {
    if (this.selectedSpace?.transaction?.id) {
      this.router.navigate(['/transactions', this.selectedSpace.transaction.id]);
      this.closeModal();
    }
  }
  
  goToProcessPayment(): void {
    if (this.selectedSpace?.transaction?.id) {
      this.router.navigate(['/transactions/payment'], {
        queryParams: { transactionId: this.selectedSpace.transaction.id }
      });
      this.closeModal();
    }
  }
  
  goToRegisterExit(): void {
    if (this.selectedSpace?.transaction?.vehicle.plateNumber) {
      this.router.navigate(['/transactions/exit'], {
        queryParams: { plateNumber: this.selectedSpace.transaction.vehicle.plateNumber }
      });
      this.closeModal();
    }
  }
  
  // ========================= HELPERS =========================
  
  getSpaceStatusBadge(status: string): string {
    switch (status) {
      case 'AVAILABLE': return 'success';
      case 'OCCUPIED': return 'danger';
      case 'MAINTENANCE': return 'warning';
      case 'OUT_OF_SERVICE': return 'secondary';
      default: return 'secondary';
    }
  }
  
  getSpaceStatusText(status: string): string {
    switch (status) {
      case 'AVAILABLE': return 'Disponible';
      case 'OCCUPIED': return 'Ocupado';
      case 'MAINTENANCE': return 'Mantenimiento';
      case 'OUT_OF_SERVICE': return 'Fuera de Servicio';
      default: return status;
    }
  }
  
  getSpaceTypeText(type: string): string {
    switch (type) {
      case 'PARALLEL': return 'Paralelo';
      case 'DIAGONAL': return 'Diagonal';
      case 'PERPENDICULAR': return 'Perpendicular';
      default: return type;
    }
  }
  
  // ========================= AUTO-REFRESH =========================
  
  startAutoRefresh(): void {
    // Refrescar cada 30 segundos
    this.refreshInterval = setInterval(() => {
      if (this.selectedZoneId && !this.loading) {
        this.loadSpaces();
      }
    }, 30000);
  }
  
  stopAutoRefresh(): void {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  }
  
  manualRefresh(): void {
    if (this.selectedZoneId) {
      this.loadSpaces();
    }
  }
}