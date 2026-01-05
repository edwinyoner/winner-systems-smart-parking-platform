/**
 * DTO para registro de nuevo usuario (ciudadano)
 */
export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  phoneNumber?: string;
  captchaToken?: string; // reCAPTCHA v3
  acceptTerms: boolean;
}

/**
 * Respuesta del registro
 */
export interface RegisterResponse {
  success: boolean;
  message: string;
  userId?: number;
}