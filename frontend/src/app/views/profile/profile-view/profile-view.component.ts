import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile-view',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 2rem; text-align: center;">
      <h2>Mi Perfil</h2>
      <p>Este módulo se implementará próximamente.</p>
    </div>
  `
})
export class ProfileViewComponent {}