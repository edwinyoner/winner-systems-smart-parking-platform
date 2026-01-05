import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 2rem; text-align: center;">
      <h2>Módulo de Usuarios</h2>
      <p>Este módulo se implementará próximamente.</p>
    </div>
  `
})
export class UserListComponent {}