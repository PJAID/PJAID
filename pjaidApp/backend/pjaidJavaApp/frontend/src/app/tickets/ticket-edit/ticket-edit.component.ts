import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {TicketService} from '../services/ticket.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TicketResponse} from '../models/ticket-response.model';

@Component({
  selector: 'app-ticket-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ticket-edit.component.html',
  styleUrl: './ticket-edit.component.css',
  providers: [TicketService]
})
export class TicketEditComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);
  private ticketService = inject(TicketService);
  private router = inject(Router);

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
        this.router.navigate(['/ticket', this.ticketId]);
      });
    }
  }

  goBack() {
    this.router.navigate(['/ticket', this.ticketId]);
  }
}
