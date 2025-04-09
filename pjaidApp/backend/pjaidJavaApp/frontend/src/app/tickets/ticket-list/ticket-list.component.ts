import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {TicketService} from '../services/ticket.service';
import {TicketResponse} from '../models/ticket-response.model';

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ticket-list.component.html',
  styleUrl: './ticket-list.component.css',
  providers: [TicketService]
})
export class TicketListComponent implements OnInit{
  private ticketService = inject(TicketService);
  tickets: TicketResponse[] = [];

  ngOnInit(): void {
    this.ticketService.getAllTickets().subscribe(data => {
      this.tickets = data;
    });
  }
}
