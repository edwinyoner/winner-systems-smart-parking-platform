// src/app/shared/fa-icon-nav/fa-icon-nav.component.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-fa-icon-nav',
  standalone: true,
  imports: [CommonModule],
  template: `<i [class]="iconClass"></i>`,
  styles: [`
    i {
      width: 1.5rem;
      display: inline-block;
      text-align: center;
      font-size: 1.1rem;
    }
  `]
})
export class FaIconNavComponent {
  @Input() iconClass: string = '';
}