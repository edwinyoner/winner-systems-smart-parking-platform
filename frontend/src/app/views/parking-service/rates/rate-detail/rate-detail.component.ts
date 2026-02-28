// src/app/views/parking-service/rates/rate-detail/rate-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

// Services
import { RateService } from '../../../../core/services/parking/rate.service';

// Models
import { Rate } from '../../../../core/models/parking/rate.model';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-rate-detail',
  standalone: true,
  imports: [
    CommonModule,
    AlertMessageComponent
  ],
  templateUrl: './rate-detail.component.html',
  styleUrls: ['./rate-detail.component.css']
})
export class RateDetailComponent implements OnInit {

  rateId!: number;
  rate: Rate | null = null;
  isLoading = true;
  errorMessage: string | null = null;
  showDeleteConfirm = false;

  constructor(
    private rateService: RateService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.rateId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadRate();
  }

  private loadRate(): void {
    this.isLoading = true;
    
    this.rateService.getById(this.rateId).subscribe({
      next: (rate: Rate) => {
        this.rate = rate;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los detalles de la tarifa';
        console.error(error);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/rates']);
  }

  formatCurrency(amount: number, currency: string): string {
    const currencySymbol = currency === 'PEN' ? 'S/.' : currency === 'USD' ? '$' : currency === 'EUR' ? '€' : currency;
    return `${currencySymbol} ${amount.toFixed(2)}`;
  }

  getCurrencyName(code: string): string {
    const currencies: { [key: string]: string } = {
      'PEN': 'Soles Peruanos',
      'USD': 'Dólares Americanos',
      'EUR': 'Euros'
    };
    return currencies[code] || code;
  }

  dismissError(): void {
    this.errorMessage = null;
  }
}