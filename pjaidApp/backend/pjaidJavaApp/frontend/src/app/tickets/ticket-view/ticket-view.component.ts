import { Component, inject, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketService } from '../services/ticket.service';
import { TicketResponse } from '../models/ticket-response.model';
import { HttpClientModule } from '@angular/common/http';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';

@Component({
  selector: 'app-ticket-view',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterModule],
  templateUrl: './ticket-view.component.html',
  providers: [TicketService]
})
export class TicketViewComponent implements OnInit {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private ticketService = inject(TicketService);
  ticket?: TicketResponse;

  goBack(): void {
    this.router.navigate(['/tickets']);
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.ticketService.getTicket(id).subscribe(data => {
        this.ticket = data;
      });
    }
  }
}
