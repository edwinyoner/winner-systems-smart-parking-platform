// src/app/views/parking-service/customers/customer-edit/customer-edit.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  FormControlDirective,
  FormFeedbackComponent,
  FormLabelDirective,
  RowComponent,
  AlertComponent,
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';
import { Customer, CustomerUpdateRequest } from '../../../../core/models/parking/customer.model';
import { CustomerService } from '../../../../core/services/parking/customer.service';

@Component({
  selector: 'app-customer-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardComponent,
    CardHeaderComponent,
    CardBodyComponent,
    RowComponent,
    ColComponent,
    FormLabelDirective,
    FormControlDirective,
    FormFeedbackComponent,
    ButtonDirective,
    IconDirective,
    AlertComponent,
  ],
  templateUrl: './customer-edit.component.html',
  styleUrls: ['./customer-edit.component.css'],
})
export class CustomerEditComponent implements OnInit {
  customerForm!: FormGroup;
  customer: Customer | null = null;
  loading = false;
  error = '';
  customerId!: number;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.customerId = +id;
      this.loadCustomer();
    }
  }

  initForm(): void {
    this.customerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      phone: ['', [Validators.pattern(/^\d{9}$/)]],
      email: ['', [Validators.email]],
      address: [''],
    });
  }

  loadCustomer(): void {
    this.loading = true;
    this.error = '';

    this.customerService.getById(this.customerId).subscribe({
      next: (customer) => {
        this.customer = customer;
        this.populateForm(customer);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el cliente';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  populateForm(customer: Customer): void {
    this.customerForm.patchValue({
      firstName: customer.firstName,
      lastName: customer.lastName,
      phone: customer.phone || '',
      email: customer.email || '',
      address: customer.address || '',
    });
  }

  onSubmit(): void {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    const updateRequest: CustomerUpdateRequest = {
      firstName: this.customerForm.value.firstName.trim(),
      lastName: this.customerForm.value.lastName.trim(),
      phone: this.customerForm.value.phone?.trim() || undefined,
      email: this.customerForm.value.email?.trim() || undefined,
      address: this.customerForm.value.address?.trim() || undefined,
    };

    this.customerService.update(this.customerId, updateRequest).subscribe({
      next: () => {
        alert('Cliente actualizado exitosamente');
        this.router.navigate(['/parking-service/customers', this.customerId]);
      },
      error: (err) => {
        this.error = 'Error al actualizar el cliente';
        console.error('Error:', err);
        this.loading = false;
      },
    });
  }

  onCancel(): void {
    this.router.navigate(['/parking-service/customers', this.customerId]);
  }

  // Helpers de validación
  isFieldInvalid(fieldName: string): boolean {
    const field = this.customerForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getErrorMessage(fieldName: string): string {
    const field = this.customerForm.get(fieldName);
    if (!field || !field.errors) return '';

    if (field.errors['required']) return 'Este campo es requerido';
    if (field.errors['minlength']) return `Mínimo ${field.errors['minlength'].requiredLength} caracteres`;
    if (field.errors['email']) return 'Email inválido';
    if (field.errors['pattern']) return 'Formato inválido (debe tener 9 dígitos)';

    return 'Campo inválido';
  }
}