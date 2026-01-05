import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertMessageComponent } from '../../../shared/components/alert-message/alert-message.component';
import { environment } from '../../../../environments/environment';

/**
 * Componente de Restablecer Contraseña
 * 
 * Funcionalidades:
 * - Formulario para restablecer contraseña con token
 * - Validación de contraseñas coincidentes
 * - Validación de fortaleza de contraseña
 * - Redirección al login después del éxito
 */
@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    AlertMessageComponent
  ],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  
  resetForm!: FormGroup;
  isLoading = false;
  successMessage = '';
  errorMessage = '';
  showPassword = false;
  showConfirmPassword = false;
  token: string = '';
  tokenValid = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Si ya está autenticado, redirigir al dashboard
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  ngOnInit(): void {
    // Obtener token de query params
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] || '';
      
      if (!this.token) {
        this.tokenValid = false;
        this.errorMessage = 'Token inválido o expirado. Por favor, solicita un nuevo link de recuperación.';
      }
    });

    this.createForm();
  }

  /**
   * Crea el formulario reactivo con validaciones
   */
  private createForm(): void {
    this.resetForm = this.fb.group({
      password: ['', [
        Validators.required, 
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]/)
      ]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  /**
   * Validador personalizado: Las contraseñas deben coincidir
   */
  private passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');

    if (!password || !confirmPassword) {
      return null;
    }

    if (confirmPassword.value === '') {
      return null;
    }

    return password.value === confirmPassword.value ? null : { passwordMismatch: true };
  }

  /**
   * Maneja el envío del formulario
   */
  onSubmit(): void {
    // Limpiar mensajes
    this.errorMessage = '';
    this.successMessage = '';

    // Validar token
    if (!this.token) {
      this.errorMessage = 'Token inválido o expirado.';
      return;
    }

    // Validar formulario
    if (this.resetForm.invalid) {
      this.markFormGroupTouched(this.resetForm);
      return;
    }

    // Iniciar loading
    this.isLoading = true;

    // Preparar datos del request
    const resetPasswordRequest = {
      token: this.token,
      newPassword: this.resetForm.value.password,
      confirmPassword: this.resetForm.value.confirmPassword
    };

    // Llamar al servicio de reset password
    this.authService.resetPassword(resetPasswordRequest).subscribe({
      next: (response) => {
        // Contraseña restablecida exitosamente
        if (environment.features.showDebugInfo) {
          console.log('✅ Contraseña restablecida:', response);
        }

        // Mostrar mensaje de éxito
        this.successMessage = 'Tu contraseña ha sido restablecida exitosamente. Redirigiendo al login...';

        // Resetear formulario
        this.resetForm.reset();

        // Redirigir al login después de 3 segundos
        setTimeout(() => {
          this.router.navigate(['/login'], {
            queryParams: { 
              passwordReset: 'true'
            }
          });
        }, 3000);
      },
      error: (error) => {
        // Error al restablecer contraseña
        this.isLoading = false;
        
        if (error.status === 400 || error.status === 404) {
          this.errorMessage = 'Token inválido o expirado. Por favor, solicita un nuevo link de recuperación.';
          this.tokenValid = false;
        } else {
          this.errorMessage = error.message || 'Error al restablecer la contraseña. Por favor, intenta nuevamente.';
        }
        
        if (environment.features.showDebugInfo) {
          console.error('❌ Error en reset password:', error);
        }
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  /**
   * Alterna la visibilidad de la contraseña
   */
  togglePasswordVisibility(field: 'password' | 'confirmPassword'): void {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
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
    const control = this.resetForm.get(field);
    return !!(control?.hasError(error) && control?.touched);
  }

  /**
   * Verifica si las contraseñas no coinciden
   */
  hasPasswordMismatch(): boolean {
    const confirmPassword = this.resetForm.get('confirmPassword');
    return !!(
      this.resetForm.hasError('passwordMismatch') && 
      confirmPassword?.touched && 
      confirmPassword?.value !== ''
    );
  }

  /**
   * Obtiene el mensaje de error de un campo
   */
  getErrorMessage(field: string): string {
    const control = this.resetForm.get(field);

    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }

    if (control?.hasError('minlength')) {
      const minLength = control.errors?.['minlength'].requiredLength;
      return `Mínimo ${minLength} caracteres`;
    }

    if (control?.hasError('pattern')) {
      return 'La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales';
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