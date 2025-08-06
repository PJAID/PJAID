import {Component, inject, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import * as L from 'leaflet';
import {CommonModule, DatePipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TicketService} from '../../tickets/services/ticket.service';
import {TicketResponse} from '../../shared/models/ticket-response.model';
import {RouterModule} from '@angular/router';
import {LocationMapComponent} from '../location-map/location-map.component';

@Component({
  selector: 'app-location-view',
  standalone: true,
  imports: [
    DatePipe, FormsModule, NgForOf, NgIf, CommonModule, RouterModule, FormsModule, LocationMapComponent
  ],
  templateUrl: './location-view.component.html',
  styleUrl: './location-view.component.css',
  providers: [TicketService]
})
export class LocationViewComponent implements OnInit, OnChanges {
  @Input() latitude!: number;
  @Input() longitude!: number;
  private map: L.Map | null = null;
  selectedHallId: string | null = null;
  selectedTicketId: number | null = null;


  private readonly ticketService = inject(TicketService);
  tickets: TicketResponse[] = [];
  isLoading = false;
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';


  ngOnInit(): void {
    this.loadTickets();
  }

  loadTickets(): void {
    this.isLoading = true;

    this.ticketService.getActiveTickets().subscribe({
      next: data => {
        this.tickets = data;
        this.isLoading = false;
      },
      error: err => {
        console.error("Błąd podczas ładowania ticketów:", err);
        this.isLoading = false;
      }
    });
  }

  sortBy(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }

    this.sortTickets();
  }

  sortTickets(): void {
    this.tickets.sort((a, b) => {
      const aValue = this.getSortValue(a, this.sortColumn);
      const bValue = this.getSortValue(b, this.sortColumn);

      if (aValue < bValue) return this.sortDirection === 'asc' ? -1 : 1;
      if (aValue > bValue) return this.sortDirection === 'asc' ? 1 : -1;
      return 0;
    });
  }

  getSortValue(ticket: TicketResponse, column: string): any {
    switch (column) {
      case 'title':
        return ticket.title.toLowerCase();
      case 'status':
        return ticket.status;
      case 'userName':
        return ticket.user?.userName?.toLowerCase() ?? '';
      case 'deviceName':
        return ticket.device?.name?.toLowerCase() ?? '';
      case 'technicianName':
        return ticket.technicianName?.toLowerCase() ?? '';
      case 'createdAt':
        return new Date(ticket.createdAt).getTime();
      default:
        return '';
    }
  }

  selectHall(hallId: string, id: number) {
    if (this.selectedTicketId === id) {
      this.selectedTicketId = null;
      this.selectedHallId = null;
      return;
    }
    if (this.selectedHallId === hallId) {
      this.selectedTicketId = id;
      return;
    }
    this.selectedTicketId = id;
    this.selectedHallId = hallId;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.latitude && this.longitude) {
      this.loadMap();
    }
  }

  private loadMap(): void {
    if (this.map) return; // zapobiega wielokrotnemu ładowaniu

    this.map = L.map('map').setView([this.latitude, this.longitude], 16);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    L.marker([this.latitude, this.longitude]).addTo(this.map)
      .bindPopup('Lokalizacja awarii')
      .openPopup();

    setTimeout(() => {
      this.map?.invalidateSize();
    }, 500);
  }
}
