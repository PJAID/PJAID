import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {TicketService} from '../services/ticket.service';
import {Router} from '@angular/router';
import {Device} from '../../shared/models/device.model';
import {TicketRequest} from '../../shared/models/ticket-request.model';
import {DeviceService} from '../../devices/services/device.service';

@Component({
  selector: 'app-ticket-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ticket-new.component.html',
  styleUrl: './ticket-new.component.css',
  providers: [TicketService, DeviceService]
})
export class TicketNewComponent {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly ticketService = inject(TicketService);
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);

  devices: Device[] = [];
  userId: number = 1;

  ticketForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    status: ['NOWE', Validators.required],
    deviceId: [0, Validators.required], // teraz trzymamy tylko ID urządzenia
  });

  ngOnInit() {
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
        userId: this.userId
      };

      this.ticketService.addTicket(ticketRequest).subscribe(() => {
        alert('Zgłoszenie dodane!');
        this.router.navigate(['/tickets']);
      });
    }
  }

}
