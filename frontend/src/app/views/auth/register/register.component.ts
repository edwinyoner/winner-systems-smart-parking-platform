import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertMessageComponent } from '../../../shared/components/alert-message/alert-message.component';
import { environment } from '../../../../environments/environment';

/**
 * Componente de Registro
 * 
 * Funcionalidades:
 * - Formulario de registro para ciudadanos
 * - Validación de contraseñas coincidentes
 * - Validación de términos y condiciones
 * - reCAPTCHA v3 (opcional)
 * - Envío de email de verificación
 */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    AlertMessageComponent
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  
  registerForm!: FormGroup;
  isLoading = false;
  successMessage = '';
  errorMessage = '';
  showPassword = false;
  showConfirmPassword = false;

  // Feature flags del environment
  enableEmailVerification = environment.features.enableEmailVerification;
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
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.pattern(/^[0-9]{9}$/)]],
      password: ['', [
        Validators.required, 
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]/)
      ]],
      confirmPassword: ['', [Validators.required]],
      acceptTerms: [false, [Validators.requiredTrue]]
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

    // Validar formulario
    if (this.registerForm.invalid) {
      this.markFormGroupTouched(this.registerForm);
      return;
    }

    // Iniciar loading
    this.isLoading = true;

    // Preparar datos del registro
    const registerRequest = {
      firstName: this.registerForm.value.firstName.trim(),
      lastName: this.registerForm.value.lastName.trim(),
      email: this.registerForm.value.email.trim().toLowerCase(),
      phoneNumber: this.registerForm.value.phoneNumber?.trim() || undefined,
      password: this.registerForm.value.password,
      confirmPassword: this.registerForm.value.confirmPassword,
      acceptTerms: this.registerForm.value.acceptTerms,
      captchaToken: undefined // TODO: Implementar reCAPTCHA token
    };

    // Llamar al servicio de registro
    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        // Registro exitoso
        if (environment.features.showDebugInfo) {
          console.log('✅ Registro exitoso:', response);
        }

        // Mostrar mensaje de éxito
        if (this.enableEmailVerification) {
          this.successMessage = 'Cuenta creada exitosamente. Por favor, verifica tu email para activar tu cuenta.';
        } else {
          this.successMessage = 'Cuenta creada exitosamente. Redirigiendo al login...';
        }

        // Resetear formulario
        this.registerForm.reset();

        // Redirigir al login después de 3 segundos
        setTimeout(() => {
          this.router.navigate(['/login'], {
            queryParams: { 
              registered: 'true',
              email: registerRequest.email
            }
          });
        }, 3000);
      },
      error: (error) => {
        // Registro fallido
        this.isLoading = false;
        this.errorMessage = error.message || 'Error al crear la cuenta. Por favor, intenta nuevamente.';
        
        if (environment.features.showDebugInfo) {
          console.error('❌ Error en registro:', error);
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
    const control = this.registerForm.get(field);
    return !!(control?.hasError(error) && control?.touched);
  }

  /**
   * Verifica si las contraseñas no coinciden
   */
  hasPasswordMismatch(): boolean {
    const confirmPassword = this.registerForm.get('confirmPassword');
    return !!(
      this.registerForm.hasError('passwordMismatch') && 
      confirmPassword?.touched && 
      confirmPassword?.value !== ''
    );
  }

  /**
   * Obtiene el mensaje de error de un campo
   */
  getErrorMessage(field: string): string {
    const control = this.registerForm.get(field);

    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }

    if (control?.hasError('email')) {
      return 'Ingresa un email válido';
    }

    if (control?.hasError('minlength')) {
      const minLength = control.errors?.['minlength'].requiredLength;
      return `Mínimo ${minLength} caracteres`;
    }

    if (control?.hasError('maxlength')) {
      const maxLength = control.errors?.['maxlength'].requiredLength;
      return `Máximo ${maxLength} caracteres`;
    }

    if (control?.hasError('pattern')) {
      if (field === 'phoneNumber') {
        return 'Ingresa un número de 9 dígitos';
      }
      if (field === 'password') {
        return 'La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales';
      }
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