import { Component, Input } from '@angular/core';

interface Technician {
  id: string;
  name: string;
  shift: string;
  isAvailable: boolean;
}

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css'],
  standalone: true
})
export class SummaryComponent {
  @Input() technicians: Technician[] = [];

  getAvailableCount(): number {
    return this.technicians.filter(t => t.isAvailable).length;
  }

  getUnavailableCount(): number {
    return this.technicians.filter(t => !t.isAvailable).length;
  }

  getTotalCount(): number {
    return this.technicians.length;
  }
}
