import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertMessageComponent } from '../../../shared/components/alert-message/alert-message.component';
import { environment } from '../../../../environments/environment';

/**
 * Componente de Login
 * 
 * Funcionalidades:
 * - Formulario de login con validación
 * - Selector de rol (para usuarios con múltiples roles)
 * - reCAPTCHA v3
 * - Integración con AuthService
 * - Manejo de errores
 * - Recordarme (remember me)
 * - Redirección después del login
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    AlertMessageComponent
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  
  loginForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  showPassword = false;
  returnUrl = '/dashboard';

  // Feature flags del environment
  showRememberMe = environment.features.enableRememberMe;
  showRegistration = environment.features.enableRegistration;
  enableCaptcha = environment.features.enableRecaptcha; // CORREGIDO

  // Roles disponibles para seleccionar
  availableRoles = [
    { value: 'ADMIN', label: 'Admin', icon: 'fas fa-user-shield' },
    { value: 'AUTORIDAD', label: 'Autoridad', icon: 'fas fa-user-tie' },
    { value: 'OPERADOR', label: 'Operador', icon: 'fas fa-user-cog' }
  ];

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
    this.createForm();
    
    // Obtener returnUrl de query params (si existe)
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
    
    // Verificar si viene de sesión expirada
    const sessionExpired = this.route.snapshot.queryParams['sessionExpired'];
    if (sessionExpired) {
      this.errorMessage = 'Tu sesión ha expirado. Por favor, inicia sesión nuevamente.';
    }
  }

  /**
   * Crea el formulario reactivo con validaciones
   */
  private createForm(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      selectedRole: ['', [Validators.required]],
      rememberMe: [false]
    });

    // Auto-completar en desarrollo (solo si está habilitado en environment)
    // if (!environment.production && environment.features.showDebugInfo) {
    //   this.loginForm.patchValue({
    //     email: 'admin@smartparking.com',
    //     password: 'Admin123!',
    //     selectedRole: 'ADMIN'
    //   });
    // }
  }

  /**
   * Maneja el envío del formulario
   */
  onSubmit(): void {
    // Limpiar mensaje de error
    this.errorMessage = '';

    // Validar formulario
    if (this.loginForm.invalid) {
      this.markFormGroupTouched(this.loginForm);
      return;
    }

    // Iniciar loading
    this.isLoading = true;

    // Preparar datos del login
    const loginRequest = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password,
      selectedRole: this.loginForm.value.selectedRole,
      rememberMe: this.loginForm.value.rememberMe,
      captchaToken: undefined // TODO: Implementar reCAPTCHA token
    };

    // Llamar al servicio de autenticación
    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        // Login exitoso
        if (environment.features.showDebugInfo) {
          console.log('Login exitoso:', {
            email: response.user.email,
            selectedRole: loginRequest.selectedRole,
            roles: response.user.roles
          });
        }

        // Redirigir a la URL de retorno o dashboard
        this.router.navigate([this.returnUrl]);
      },
      error: (error) => {
        // Login fallido
        this.isLoading = false;
        this.errorMessage = error.message || 'Email, contraseña o rol incorrectos';
        
        if (environment.features.showDebugInfo) {
          console.error('❌ Error en login:', error);
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
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  /**
   * Obtiene el icono del rol seleccionado
   */
  getSelectedRoleIcon(): string {
    const selectedRole = this.loginForm.get('selectedRole')?.value;
    const role = this.availableRoles.find(r => r.value === selectedRole);
    return role?.icon || 'fas fa-user';
  }

  /**
   * Marca todos los campos del formulario como touched para mostrar errores
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
    const control = this.loginForm.get(field);
    return !!(control?.hasError(error) && control?.touched);
  }

  /**
   * Obtiene el mensaje de error de un campo
   */
  getErrorMessage(field: string): string {
    const control = this.loginForm.get(field);

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

    return '';
  }

  /**
   * Cierra el mensaje de error
   */
  dismissError(): void {
    this.errorMessage = '';
  }

  /**
   * Ejecuta reCAPTCHA v3 (si está habilitado)
   */
  private async executeCaptcha(): Promise<string | undefined> {
    if (!this.enableCaptcha || !environment.recaptchaSiteKey) {
      return undefined;
    }

    // TODO: Implementar reCAPTCHA v3
    // return await grecaptcha.execute(environment.recaptchaSiteKey, { action: 'login' });
    return undefined;
  }
}