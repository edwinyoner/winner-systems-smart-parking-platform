// src/app/views/parking-service/shifts/shift-detail/shift-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

// Services
import { ShiftService } from '../../../../core/services/parking/shift.service';

// Models
import { Shift } from '../../../../core/models/parking/shift.model';

// Components
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-shift-detail',
  standalone: true,
  imports: [
    CommonModule,
    AlertMessageComponent
  ],
  templateUrl: './shift-detail.component.html',
  styleUrls: ['./shift-detail.component.css']
})
export class ShiftDetailComponent implements OnInit {

  shiftId!: number;
  shift: Shift | null = null;
  isLoading = true;
  errorMessage: string | null = null;
  showDeleteConfirm = false;

  constructor(
    private shiftService: ShiftService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.shiftId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadShift();
  }

  private loadShift(): void {
    this.isLoading = true;
    
    this.shiftService.getById(this.shiftId).subscribe({
      next: (shift: Shift) => {
        this.shift = shift;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los detalles del turno';
        console.error(error);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/shifts']);
  }

  formatTime(time: string): string {
    if (!time) return 'N/A';
    return time.substring(0, 5);
  }

  calculateDuration(startTime: string, endTime: string): string {
    if (!startTime || !endTime) return 'N/A';
    
    const [startHour, startMin] = startTime.split(':').map(Number);
    const [endHour, endMin] = endTime.split(':').map(Number);
    
    let totalMinutes = (endHour * 60 + endMin) - (startHour * 60 + startMin);
    if (totalMinutes < 0) totalMinutes += 24 * 60;
    
    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;
    
    return minutes > 0 ? `${hours}h ${minutes}min` : `${hours}h`;
  }

  dismissError(): void {
    this.errorMessage = null;
  }
}