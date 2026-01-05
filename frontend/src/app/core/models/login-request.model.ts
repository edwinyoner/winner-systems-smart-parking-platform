/**
 * Request para login
 */
export interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
  captchaToken?: string;
  selectedRole?: string;  // ✅ NUEVO: Rol seleccionado al login
}

/**
 * Request para cambiar contraseña
 */
export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

/**
 * Request para recuperar contraseña
 */
export interface ForgotPasswordRequest {
  email: string;
  captchaToken?: string;
}

/**
 * Request para restablecer contraseña
 */
export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
  confirmPassword: string;
}

/**
 * Request para reenviar verificación de email
 */
export interface ResendVerificationRequest {
  email: string;
}