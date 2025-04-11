import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {TicketService} from '../services/ticket.service';
import {Router} from '@angular/router';
import {TicketResponse} from '../models/ticket-response.model';

@Component({
  selector: 'app-ticket-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ticket-new.component.html',
  styleUrl: './ticket-new.component.css',
  providers: [TicketService]
})
export class TicketNewComponent {
  private readonly fb = inject(FormBuilder);
  private readonly ticketService = inject(TicketService);
  private readonly router = inject(Router);

  ticketForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    status: ['NOWE', Validators.required]
  });

  submit() {
    if (this.ticketForm.valid) {
      const formData = this.ticketForm.getRawValue() as TicketResponse;

      this.ticketService.addTicket(formData).subscribe(() => {
        alert('Zgłoszenie dodane!');
        this.router.navigate(['/tickets']);
      });
    }
  }

}
