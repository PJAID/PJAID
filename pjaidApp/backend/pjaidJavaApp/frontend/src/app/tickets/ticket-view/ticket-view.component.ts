import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { TicketService } from '../services/ticket.service';
import { TicketResponse } from '../../shared/models/ticket-response.model';
import { TechnicianListComponent } from '../technician-list/technician-list.component';
import {LocationDetailsMapComponent} from '../../location/location-details-map/location-details-map.component';


@Component({
  selector: 'app-ticket-view',
  standalone: true,
  imports: [CommonModule, RouterModule, TechnicianListComponent, LocationDetailsMapComponent],
  templateUrl: './ticket-view.component.html',
  styleUrls: ['./ticket-view.component.css'],
  providers: [TicketService]
})
export class TicketViewComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly ticketService = inject(TicketService);
  private readonly http = inject(HttpClient);

  ticket?: TicketResponse;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.ticketService.getTicket(id).subscribe(data => {
        this.ticket = data;
        console.log('Załadowano ticket:', this.ticket);
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/tickets']);
  }

  assignTechnician(technicianId: number): void {
    if (!this.ticket?.id) return;

    this.http.put<TicketResponse>(
      `http://localhost:8080/ticket/${this.ticket.id}/assign?technicianId=${technicianId}`,
      {}
    ).subscribe({
      next: updated => {
        this.ticket = updated;
        alert('Technik przypisany');
      },
      error: err => {
        console.error('Błąd przypisania technika:', err);
        alert('Nie udało się przypisać technika');
      }
    });
  }
}
