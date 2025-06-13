import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {TicketService} from '../services/ticket.service';
import {Router} from '@angular/router';
import {DeviceResponse} from '../../shared/models/device-response.model';
import {TicketRequest} from '../../shared/models/ticket-request.model';
import {DeviceService} from '../../devices/services/device.service';
import {AuthService} from '../../auth/services/auth.service';

@Component({
  selector: 'app-ticket-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ticket-new.component.html',
  styleUrl: './ticket-new.component.css',
  providers: [TicketService, DeviceService, AuthService]
})
export class TicketNewComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly ticketService = inject(TicketService);
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);
  loggedPerson: string | null = this.authService.getLoggedUser();

  devices: DeviceResponse[] = [];
  userName: string = '';

  ticketForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    status: ['NOWE', Validators.required],
    deviceId: [0, Validators.required],
    incidentId: [null]
  });

  ngOnInit(): void {
    this.deviceService.getDevices().subscribe(devices => {
      this.devices = devices;
    });
  }

  submit() {
    if (this.ticketForm.valid) {
      const formValue = this.ticketForm.getRawValue();

      const ticketRequest: TicketRequest = {
        title: formValue.title,
        description: formValue.description,
        status: formValue.status as 'NOWE' | 'W_TRAKCIE' | 'ZAMKNIETE',
        deviceId: formValue.deviceId,
        userName: this.loggedPerson ?? 'defaultUser'
      };

      this.ticketService.addTicket(ticketRequest).subscribe(() => {
        alert('Zgłoszenie dodane!');
        this.router.navigate(['/tickets']);
      });
    }
  }

}
