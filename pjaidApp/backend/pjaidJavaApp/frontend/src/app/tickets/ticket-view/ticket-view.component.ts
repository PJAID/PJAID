import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TicketService} from '../services/ticket.service';
import {TicketResponse} from '../models/ticket-response.model';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';

@Component({
  selector: 'app-ticket-view',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ticket-view.component.html',
  providers: [TicketService]
})
export class TicketViewComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly ticketService = inject(TicketService);
  ticket?: TicketResponse;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.ticketService.getTicket(id).subscribe(data => {
        this.ticket = data;
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/tickets']);
  }
}
