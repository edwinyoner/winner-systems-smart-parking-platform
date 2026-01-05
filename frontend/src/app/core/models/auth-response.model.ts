import { User } from './user.model';

/**
 * Respuesta del login exitoso
 */
export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number; // milisegundos
  user: UserInfo;
}

/**
 * Información del usuario autenticado
 */
export interface UserInfo {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  profilePicture?: string;
  emailVerified: boolean;
  status: boolean;
  roles: string[]; // ["ADMIN", "OPERADOR"]
  permissions: string[]; // ["users.create", "users.read", ...]
}

/**
 * Respuesta del refresh token
 */
export interface RefreshTokenResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
}

/**
 * Respuesta de verificación de email
 */
export interface VerifyEmailResponse {
  success: boolean;
  message: string;
}

/**
 * Respuesta de recuperación de contraseña
 */
export interface ForgotPasswordResponse {
  success: boolean;
  message: string;
}

/**
 * Respuesta de reset de contraseña
 */
export interface ResetPasswordResponse {
  success: boolean;
  message: string;
}