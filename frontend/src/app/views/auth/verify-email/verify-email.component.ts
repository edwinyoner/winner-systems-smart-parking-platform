//src/app/views/auth/verify-email/verify-email.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertMessageComponent } from '../../../shared/components/alert-message/alert-message.component';
import { environment } from '../../../../environments/environment';

/**
 * Componente de Verificación de Email
 * 
 * Funcionalidades:
 * - Verificación automática de email con token
 * - Estados de éxito y error
 * - Reenvío de email de verificación
 * - Redirección al login después del éxito
 */
@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    AlertMessageComponent
  ],
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {
  
  resendForm!: FormGroup;
  isLoading = false;
  isVerifying = true;
  verificationSuccess = false;
  verificationError = false;
  errorMessage = '';
  successMessage = '';
  token: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Obtener token de query params
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] || '';
      
      if (this.token) {
        // Si hay token, verificar automáticamente
        this.verifyEmail();
      } else {
        // Si no hay token, mostrar formulario de reenvío
        this.isVerifying = false;
        this.verificationError = true;
        this.errorMessage = 'Token de verificación no encontrado. Ingresa tu email para recibir un nuevo link.';
      }
    });

    this.createForm();
  }

  /**
   * Crea el formulario para reenviar email
   */
  private createForm(): void {
    this.resendForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  /**
   * Verifica el email con el token
   */
  private verifyEmail(): void {
    this.isVerifying = true;

    this.authService.verifyEmail(this.token).subscribe({
      next: (response) => {
        // Verificación exitosa
        this.isVerifying = false;
        this.verificationSuccess = true;
        this.successMessage = 'Tu email ha sido verificado exitosamente. Ya puedes iniciar sesión.';

        if (environment.features.showDebugInfo) {
          console.log('✅ Email verificado:', response);
        }

        // Redirigir al login después de 3 segundos
        setTimeout(() => {
          this.router.navigate(['/login'], {
            queryParams: { 
              emailVerified: 'true'
            }
          });
        }, 3000);
      },
      error: (error) => {
        // Error en verificación
        this.isVerifying = false;
        this.verificationError = true;

        if (error.status === 400 || error.status === 404) {
          this.errorMessage = 'Token inválido o expirado. Por favor, solicita un nuevo link de verificación.';
        } else if (error.status === 409) {
          this.errorMessage = 'Este email ya ha sido verificado. Puedes iniciar sesión.';
        } else {
          this.errorMessage = error.message || 'Error al verificar el email. Por favor, intenta nuevamente.';
        }

        if (environment.features.showDebugInfo) {
          console.error('❌ Error en verify email:', error);
        }
      }
    });
  }

  /**
   * Reenvía el email de verificación
   */
  onResend(): void {
    // Limpiar mensajes
    this.errorMessage = '';
    this.successMessage = '';

    // Validar formulario
    if (this.resendForm.invalid) {
      this.markFormGroupTouched(this.resendForm);
      return;
    }

    // Iniciar loading
    this.isLoading = true;

    // Preparar datos del request
    const resendRequest = {
      email: this.resendForm.value.email.trim().toLowerCase()
    };

    // Llamar al servicio de reenvío
    this.authService.resendVerification(resendRequest).subscribe({
      next: (response) => {
        // Email reenviado exitosamente
        this.isLoading = false;
        this.successMessage = 'Email de verificación enviado. Por favor, revisa tu bandeja de entrada.';

        if (environment.features.showDebugInfo) {
          console.log('✅ Email reenviado:', response);
        }

        // Resetear formulario
        this.resendForm.reset();
      },
      error: (error) => {
        // Error al reenviar
        this.isLoading = false;
        this.errorMessage = error.message || 'Error al enviar el email. Por favor, intenta nuevamente.';

        if (environment.features.showDebugInfo) {
          console.error('❌ Error en resend verification:', error);
        }
      }
    });
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
    const control = this.resendForm.get(field);
    return !!(control?.hasError(error) && control?.touched);
  }

  /**
   * Obtiene el mensaje de error de un campo
   */
  getErrorMessage(field: string): string {
    const control = this.resendForm.get(field);

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