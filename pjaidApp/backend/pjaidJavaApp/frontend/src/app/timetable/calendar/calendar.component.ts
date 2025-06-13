import { Component, Input, Output, EventEmitter } from '@angular/core';

interface ShiftSchedule {
  date: string;
  shift: string;
  timeFrom: string;
  timeTo: string;
  hours: number;
  dayType: number;
}

interface Technician {
  id: string;
  name: string;
  shift: string;
  isAvailable: boolean;
}

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css'],
  standalone: true
})
export class CalendarComponent {
  @Input() currentWeekStart!: Date;
  @Input() scheduleData: ShiftSchedule[] = [];
  @Input() technicians: Technician[] = [];

  @Output() previousWeek = new EventEmitter<void>();
  @Output() nextWeek = new EventEmitter<void>();
  @Output() toggleTech = new EventEmitter<Technician>();

  getCurrentWeekDays(): Date[] {
    const days: Date[] = [];
    for (let i = 0; i < 7; i++) {
      const day = new Date(this.currentWeekStart);
      day.setDate(this.currentWeekStart.getDate() + i);
      days.push(day);
    }
    return days;
  }

  formatDayHeader(date: Date): string {
    const dayNames = ['Nd', 'Pn', 'Wt', 'Śr', 'Cz', 'Pt', 'Sb'];
    return `${dayNames[date.getDay()]} ${date.getDate().toString().padStart(2, '0')}.${(date.getMonth() + 1).toString().padStart(2, '0')}`;
  }

  isShiftWorking(shift: string, date: Date): boolean {
    const dateStr = date.toISOString().split('T')[0];
    const schedule = this.scheduleData.find(s => s.date === dateStr && s.shift === shift);
    return schedule ? schedule.hours > 0 : false;
  }

  getShiftInfo(shift: string, date: Date): string {
    const dateStr = date.toISOString().split('T')[0];
    const schedule = this.scheduleData.find(s => s.date === dateStr && s.shift === shift);
    return schedule && schedule.hours > 0
      ? `${schedule.timeFrom}-${schedule.timeTo}`
      : 'Wolne';
  }

  getTechniciansForShift(shift: string): Technician[] {
    return this.technicians.filter(t => t.shift === shift);
  }

  onToggleTech(t: Technician) {
    this.toggleTech.emit(t);
  }
}
