import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TicketService } from '../services/ticket.service';
import { TicketResponse } from '../../shared/models/ticket-response.model';
import { FormsModule } from '@angular/forms';

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
        this.isLoading = false;
      },
      error: err => {
        console.error("Błąd podczas ładowania ticketów:", err);
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    this.loadTickets();
  }

  clearFilters(): void {
    this.filterStatus = '';
    this.filterUserName = '';
    this.filterDeviceName = '';
    this.filterTitle = '';
    this.loadTickets();
  }

  loadUserTickets(username: string): void {
    this.isLoading = true;
    this.ticketService.getTicketsByUser(username).subscribe({
      next: data => {
        this.tickets = data;
        this.isLoading = false;
      },
      error: err => {
        console.error("Błąd podczas ładowania zgłoszeń użytkownika:", err);
        this.isLoading = false;
      }
    });
  }
}
