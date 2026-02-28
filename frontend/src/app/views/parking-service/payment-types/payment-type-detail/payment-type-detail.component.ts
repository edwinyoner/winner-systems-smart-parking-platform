import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { PaymentTypeService } from '../../../../core/services/parking/payment-type.service';
import { PaymentType } from '../../../../core/models/parking/payment-type.model';
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-payment-type-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, AlertMessageComponent],
  templateUrl: './payment-type-detail.component.html',
  styleUrls: ['./payment-type-detail.component.css']
})
export class PaymentTypeDetailComponent implements OnInit {

  paymentTypeId!: number;
  paymentType: PaymentType | null = null;
  isLoading = true;
  errorMessage: string | null = null;

  constructor(
    private paymentTypeService: PaymentTypeService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.paymentTypeId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadPaymentType();
  }

  private loadPaymentType(): void {
    this.isLoading = true;
    this.paymentTypeService.getById(this.paymentTypeId).subscribe({
      next: (pt: PaymentType) => { this.paymentType = pt; this.isLoading = false; },
      error: (err) => {
        this.isLoading    = false;
        this.errorMessage = 'Error al cargar los detalles del tipo de pago';
        console.error(err);
      }
    });
  }

  goBack(): void { this.router.navigate(['/payment-types']); }
  dismissError(): void { this.errorMessage = null; }
}