import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {TicketService} from '../services/ticket.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TicketResponse} from '../../shared/models/ticket-response.model';

@Component({
  selector: 'app-ticket-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ticket-edit.component.html',
  styleUrls: ['./ticket-edit.component.css'],
  providers: [TicketService]
})
export class TicketEditComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);
  private readonly ticketService = inject(TicketService);
  private readonly router = inject(Router);

  ticketId!: number;

  ticketForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    status: ['NOWE', Validators.required]
  });

  ngOnInit(): void {
    this.ticketId = Number(this.route.snapshot.paramMap.get('id'));
    this.ticketService.getTicket(this.ticketId).subscribe(ticket => {
      this.ticketForm.patchValue(ticket);
    });
  }

  save() {
    if (this.ticketForm.valid) {
      const formData = this.ticketForm.getRawValue() as TicketResponse;
      this.ticketService.updateTicket(this.ticketId, formData).subscribe(() => {
        alert('Zgłoszenie zaktualizowane!');
        this.router.navigate(['/tickets', this.ticketId]);
      });
    }
  }

  goBack() {
    this.router.navigate(['/tickets', this.ticketId]);
  }
}
