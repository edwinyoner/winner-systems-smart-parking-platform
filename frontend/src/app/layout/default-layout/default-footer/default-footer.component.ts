import { Component } from '@angular/core';
import { FooterComponent, FooterModule } from '@coreui/angular';

@Component({
  selector: 'app-default-footer',
  standalone: true,
  imports: [FooterComponent, FooterModule],
  templateUrl: './default-footer.component.html',
  styleUrls: ['./default-footer.component.scss']
})
export class DefaultFooterComponent extends FooterComponent {
  public currentYear: number;

  constructor() {
    super();
    this.currentYear = new Date().getFullYear();
  }
}