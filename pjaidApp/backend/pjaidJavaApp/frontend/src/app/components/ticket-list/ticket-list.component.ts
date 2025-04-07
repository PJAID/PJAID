import { Component, OnInit } from '@angular/core';
import { TicketService, Ticket } from '../../ticket.service';
import {NgForOf, NgIf} from '@angular/common';
import {RouterLink, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  imports: [NgForOf, RouterLink, NgIf],
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.css']
})
export class TicketListComponent implements OnInit {
  tickets: Ticket[] = [];

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    this.ticketService.getAllTickets().subscribe({
      next: (data) => {this.tickets = data;
      },
      error: (err) => {console.error('Błąd podczas pobierania zgłoszeń:', err);
      }
    });
  }
}
