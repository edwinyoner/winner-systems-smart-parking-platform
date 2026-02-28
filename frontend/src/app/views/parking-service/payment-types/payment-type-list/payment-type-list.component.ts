import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { PaymentTypeService } from '../../../../core/services/parking/payment-type.service';
import { PaymentType } from '../../../../core/models/parking/payment-type.model';
import { ParkingPagedResponse } from '../../../../core/models/pagination.model';
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-payment-type-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, AlertMessageComponent],
  templateUrl: './payment-type-list.component.html',
  styleUrls: ['./payment-type-list.component.css']
})
export class PaymentTypeListComponent implements OnInit {

  filterForm!: FormGroup;
  paymentTypes: PaymentType[] = [];
  isLoading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  currentPage = 1;
  pageSize = 10;
  totalItems = 0;
  totalPages = 0;
  pageSizeOptions = [5, 10, 15, 20, 25, 50];

  showDeleteConfirm = false;
  paymentTypeToDelete: PaymentType | null = null;

  Math = Math;

  constructor(
    private fb: FormBuilder,
    private paymentTypeService: PaymentTypeService,
    private router: Router
  ) {
    const state = window.history.state;
    if (state?.['successMessage']) {
      this.successMessage = state['successMessage'];
      setTimeout(() => (this.successMessage = null), 5000);
    }
  }

  ngOnInit(): void {
    this.initFilterForm();
    this.loadPaymentTypes();
    this.setupFilterListeners();
  }

  private initFilterForm(): void {
    this.filterForm = this.fb.group({ search: [''], status: [''] });
  }

  private setupFilterListeners(): void {
    this.filterForm.get('search')?.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => { this.currentPage = 1; this.loadPaymentTypes(); });

    this.filterForm.get('status')?.valueChanges
      .subscribe(() => { this.currentPage = 1; this.loadPaymentTypes(); });
  }

  loadPaymentTypes(): void {
    this.isLoading = true;
    const search = this.filterForm.get('search')?.value ?? '';
    const status = this.filterForm.get('status')?.value ?? '';

    this.paymentTypeService.getAll(this.currentPage - 1, this.pageSize, search, status).subscribe({
      next: (response: ParkingPagedResponse<PaymentType>) => {
        this.paymentTypes = response.content;
        this.totalItems   = response.totalElements;
        this.totalPages   = response.totalPages;
        this.currentPage  = response.pageNumber + 1;
        this.pageSize     = response.pageSize;
        this.isLoading    = false;
      },
      error: (error) => {
        this.isLoading    = false;
        this.errorMessage = 'Error al cargar los tipos de pago';
        console.error(error);
      }
    });
  }

  clearFilters(): void {
    this.filterForm.reset({ search: '', status: '' }, { emitEvent: false });
    this.currentPage = 1;
    this.loadPaymentTypes();
  }

  onPageSizeChange(event: any): void {
    this.pageSize = Number(event.target.value);
    this.currentPage = 1;
    this.loadPaymentTypes();
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) { this.currentPage = page; this.loadPaymentTypes(); }
  }

  getPages(): number[] {
    const pages = [];
    const max = 5;
    let start = Math.max(1, this.currentPage - Math.floor(max / 2));
    let end   = Math.min(this.totalPages, start + max - 1);
    if (end - start + 1 < max) start = Math.max(1, end - max + 1);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }

  createPaymentType(): void   { this.router.navigate(['/payment-types/create']); }
  viewPaymentType(id: number): void { this.router.navigate(['/payment-types', id]); }
  editPaymentType(id: number): void { this.router.navigate(['/payment-types', id, 'edit']); }

  confirmDelete(pt: PaymentType): void { this.paymentTypeToDelete = pt; this.showDeleteConfirm = true; }
  cancelDelete(): void { this.showDeleteConfirm = false; this.paymentTypeToDelete = null; }

  deletePaymentType(): void {
    if (!this.paymentTypeToDelete?.id) return;
    this.isLoading = true;

    this.paymentTypeService.delete(this.paymentTypeToDelete.id).subscribe({
      next: () => {
        this.isLoading      = false;
        this.successMessage = `Tipo de pago "${this.paymentTypeToDelete?.name}" eliminado exitosamente`;
        this.showDeleteConfirm  = false;
        this.paymentTypeToDelete = null;
        this.loadPaymentTypes();
      },
      error: (error) => {
        this.isLoading    = false;
        this.errorMessage = error.error?.message || 'No se pudo eliminar el tipo de pago';
        this.showDeleteConfirm = false;
      }
    });
  }

  dismissSuccess(): void { this.successMessage = null; }
  dismissError(): void   { this.errorMessage = null; }
}