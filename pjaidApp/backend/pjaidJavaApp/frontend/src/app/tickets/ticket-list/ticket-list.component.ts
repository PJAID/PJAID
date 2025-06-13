import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {TicketService} from '../services/ticket.service';
import {TicketResponse} from '../../shared/models/ticket-response.model';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './ticket-list.component.html',
  styleUrl: './ticket-list.component.css',
  providers: [TicketService]
})
export class TicketListComponent implements OnInit {
  private readonly ticketService = inject(TicketService);
  tickets: TicketResponse[] = [];
  isLoading = false;
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  statusCounts = {
    NOWE: 0,
    W_TRAKCIE: 0,
    ZAMKNIETE: 0
  };


  filterStatus: string = '';
  filterUserName: string = '';
  filterDeviceName: string = '';
  filterTitle: string = '';

  ngOnInit(): void {
    this.loadTickets();
  }

  loadTickets(): void {
    this.isLoading = true;

    const currentFilters = {
      status: this.filterStatus,
      user: this.filterUserName,
      device: this.filterDeviceName,
      titleContains: this.filterTitle
    };

    this.ticketService.getAllTickets(currentFilters).subscribe({
      next: data => {
        this.tickets = data;
        this.countStatuses();
        this.isLoading = false;
      },
      error: err => {
        console.error("Błąd podczas ładowania ticketów:", err);
        this.isLoading = false;
      }
    });
  }

  private countStatuses(): void {
    this.statusCounts = {
      NOWE: 0,
      W_TRAKCIE: 0,
      ZAMKNIETE: 0
    };

    for (const ticket of this.tickets) {
      if (ticket.status && this.statusCounts.hasOwnProperty(ticket.status)) {
        this.statusCounts[ticket.status]++;
      }
    }
  }

  applyFilters(): void {
    this.loadTickets();
    this.sortTickets();
  }

  clearFilters(): void {
    this.filterStatus = '';
    this.filterUserName = '';
    this.filterDeviceName = '';
    this.filterTitle = '';
    this.loadTickets();
  }

  sortBy(column: string): void {
    if (this.sortColumn === column) {
      // Odwróć kierunek sortowania
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


  startTicket(ticketId: number): void {
    this.ticketService.startTicket(ticketId).subscribe({
      next: updatedTicket => {
        this.loadTickets();
      },
      error: err => {
        console.error("Błąd podczas rozpoczynania zgłoszenia:", err);
      }
    });
  }
}
