import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-technician-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './technician-list.component.html'
})
export class TechnicianListComponent implements OnInit {
  @Input() ticketId!: number;
  @Output() technicianAssigned = new EventEmitter<number>();

  technicians: { id: number; userName: string }[] = [];
  selectedId: number | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/users/technicians').subscribe({
      next: data => this.technicians = data,
      error: err => console.error('Błąd ładowania techników', err)
    });
  }

  onAssign(): void {
    if (this.selectedId != null) {
      this.technicianAssigned.emit(this.selectedId);
    } else {
      alert('Wybierz technika');
    }
  }
}
