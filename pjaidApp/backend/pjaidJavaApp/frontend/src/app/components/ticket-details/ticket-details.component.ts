import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TicketService, Ticket } from '../../ticket.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-ticket-details',
  standalone: true,
  templateUrl: './ticket-details.component.html',
  styleUrls: ['./ticket-details.component.css'],
  imports: [NgIf],
})
export class TicketDetailsComponent implements OnInit {
  ticket: Ticket | null = null;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.ticketService.getTicket(id).subscribe({
      next: (data) => (this.ticket = data),
      error: (err) => console.error('Ticket not found:', err)
    });
  }
}
