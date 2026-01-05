import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertMessageComponent } from '../../../shared/components/alert-message/alert-message.component';
import { environment } from '../../../../environments/environment';

/**
 * Componente de Recuperación de Contraseña
 * 
 * Funcionalidades:
 * - Formulario para solicitar recuperación de contraseña
 * - Envío de email con token de recuperación
 * - reCAPTCHA v3 (opcional)
 * - Mensaje de confirmación
 */
@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    AlertMessageComponent
  ],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  
  forgotForm!: FormGroup;
  isLoading = false;
  successMessage = '';
  errorMessage = '';
  emailSent = false;

  // Feature flags del environment
  enableCaptcha = environment.features.enableRecaptcha;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // Si ya está autenticado, redirigir al dashboard
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  ngOnInit(): void {
    this.createForm();
  }

  /**
   * Crea el formulario reactivo con validaciones
   */
  private createForm(): void {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  /**
   * Maneja el envío del formulario
   */
  onSubmit(): void {
    // Limpiar mensajes
    this.errorMessage = '';
    this.successMessage = '';

    // Validar formulario
    if (this.forgotForm.invalid) {
      this.markFormGroupTouched(this.forgotForm);
      return;
    }

    // Iniciar loading
    this.isLoading = true;

    // Preparar datos del request
    const forgotPasswordRequest = {
      email: this.forgotForm.value.email.trim().toLowerCase(),
      captchaToken: undefined // TODO: Implementar reCAPTCHA token
    };

    // Llamar al servicio de recuperación
    this.authService.forgotPassword(forgotPasswordRequest).subscribe({
      next: (response) => {
        // Email enviado exitosamente
        if (environment.features.showDebugInfo) {
          console.log('✅ Email de recuperación enviado:', response);
        }

        // Mostrar mensaje de éxito
        this.successMessage = 'Se ha enviado un email con las instrucciones para restablecer tu contraseña. Por favor, revisa tu bandeja de entrada.';
        this.emailSent = true;

        // Resetear formulario
        this.forgotForm.reset();
      },
      error: (error) => {
        // Error al enviar email
        this.isLoading = false;
        this.errorMessage = error.message || 'Error al enviar el email. Por favor, intenta nuevamente.';
        
        if (environment.features.showDebugInfo) {
          console.error('❌ Error en forgot password:', error);
        }
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  /**
   * Reenvía el email de recuperación
   */
  resendEmail(): void {
    this.emailSent = false;
    this.successMessage = '';
    this.errorMessage = '';
  }

  /**
   * Marca todos los campos del formulario como touched
   */
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  /**
   * Verifica si un campo tiene error y ha sido tocado
   */
  hasError(field: string, error: string): boolean {
    const control = this.forgotForm.get(field);
    return !!(control?.hasError(error) && control?.touched);
  }

  /**
   * Obtiene el mensaje de error de un campo
   */
  getErrorMessage(field: string): string {
    const control = this.forgotForm.get(field);

    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }

    if (control?.hasError('email')) {
      return 'Ingresa un email válido';
    }

    return '';
  }

  /**
   * Cierra los mensajes
   */
  dismissSuccess(): void {
    this.successMessage = '';
  }

  dismissError(): void {
    this.errorMessage = '';
  }
}