import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertMessageComponent } from '../../../shared/components/alert-message/alert-message.component';
import { environment } from '../../../../environments/environment';

/**
 * Componente de Cambio de Contraseña
 * 
 * Permite al usuario autenticado cambiar su contraseña actual
 * Requiere: contraseña actual + nueva contraseña + confirmación
 */
@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AlertMessageComponent
  ],
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
  
  changePasswordForm!: FormGroup;
  isLoading = false;
  successMessage = '';
  errorMessage = '';
  
  // Visibilidad de contraseñas
  showCurrentPassword = false;
  showNewPassword = false;
  showConfirmPassword = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.createForm();
  }

  /**
   * Crea el formulario reactivo con validaciones
   */
  private createForm(): void {
    this.changePasswordForm = this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)
      ]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  /**
   * Validador personalizado para confirmar que las contraseñas coincidan
   */
  private passwordMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const newPassword = group.get('newPassword')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    
    return newPassword === confirmPassword ? null : { passwordMismatch: true };
  }

  /**
   * Maneja el envío del formulario
   */
  onSubmit(): void {
    // Limpiar mensajes
    this.errorMessage = '';
    this.successMessage = '';

    // Validar formulario
    if (this.changePasswordForm.invalid) {
      this.markFormGroupTouched(this.changePasswordForm);
      return;
    }

    // Iniciar loading
    this.isLoading = true;

    // Preparar request
    const request = {
      currentPassword: this.changePasswordForm.value.currentPassword,
      newPassword: this.changePasswordForm.value.newPassword,
      confirmPassword: this.changePasswordForm.value.confirmPassword
    };

    // Llamar al servicio
    this.authService.changePassword(request).subscribe({
      next: (response) => {
        // Contraseña cambiada exitosamente
        this.successMessage = 'Contraseña cambiada exitosamente. Por seguridad, debes iniciar sesión nuevamente.';
        
        if (environment.features.showDebugInfo) {
          console.log('✅ Contraseña cambiada:', response);
        }

        // Resetear formulario
        this.changePasswordForm.reset();

        // Cerrar sesión después de 3 segundos
        setTimeout(() => {
          this.authService.logout();
        }, 3000);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.message || 'Error al cambiar la contraseña. Verifica tu contraseña actual.';
        
        if (environment.features.showDebugInfo) {
          console.error('❌ Error al cambiar contraseña:', error);
        }
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  /**
   * Toggle visibilidad de contraseña
   */
  togglePasswordVisibility(field: 'current' | 'new' | 'confirm'): void {
    switch (field) {
      case 'current':
        this.showCurrentPassword = !this.showCurrentPassword;
        break;
      case 'new':
        this.showNewPassword = !this.showNewPassword;
        break;
      case 'confirm':
        this.showConfirmPassword = !this.showConfirmPassword;
        break;
    }
  }

  /**
   * Verifica si un campo tiene error y ha sido tocado
   */
  hasError(field: string, error: string): boolean {
    const control = this.changePasswordForm.get(field);
    return !!(control?.hasError(error) && control?.touched);
  }

  /**
   * Verifica si el formulario tiene error de coincidencia de contraseñas
   */
  hasPasswordMismatch(): boolean {
    return !!(
      this.changePasswordForm.hasError('passwordMismatch') &&
      this.changePasswordForm.get('confirmPassword')?.touched
    );
  }

  /**
   * Obtiene el mensaje de error de un campo
   */
  getErrorMessage(field: string): string {
    const control = this.changePasswordForm.get(field);

    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }

    if (field === 'newPassword') {
      if (control?.hasError('minlength')) {
        return 'La contraseña debe tener al menos 8 caracteres';
      }
      if (control?.hasError('pattern')) {
        return 'Debe contener mayúsculas, minúsculas, números y caracteres especiales';
      }
    }

    return '';
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
   * Cancela y vuelve al perfil
   */
  cancel(): void {
    this.router.navigate(['/profile']);
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