import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

/**
 * Layout para páginas de autenticación
 * 
 * Sin sidebar, diseño centrado y limpio para:
 * - Login
 * - Register
 * - Forgot Password
 * - Reset Password
 * - Verify Email
 */
@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet
  ],
  templateUrl: './auth-layout.component.html',
  styleUrls: ['./auth-layout.component.scss']
})
export class AuthLayoutComponent {
  
  currentYear: number = new Date().getFullYear();
  
//   companyName: string = 'Winner Systems Corporation S.A.C.';
    companyName: string = 'Municipalidad Provincial De Huaraz';
  appName: string = 'Smart Parking Platform';

  constructor() {}
}