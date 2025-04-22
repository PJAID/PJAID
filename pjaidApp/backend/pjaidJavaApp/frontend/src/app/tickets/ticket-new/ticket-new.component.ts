import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {TicketService} from '../services/ticket.service';
import {Router} from '@angular/router';
import {Device} from '../../shared/models/device.model';
import {TicketRequest} from '../../shared/models/ticket-request.model';
import {DeviceService} from '../../devices/services/device.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {map, Observable, startWith} from 'rxjs';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-ticket-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatAutocompleteModule, FormsModule],
  templateUrl: './ticket-new.component.html',
  styleUrl: './ticket-new.component.css',
  providers: [TicketService, DeviceService]
})
export class TicketNewComponent implements OnInit {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly ticketService = inject(TicketService);
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);

  devices: Device[] = [];
  userId: number = 1;
  deviceControl = this.fb.control<Device | null>(null,Validators.required);
  filteredDevices!: Observable<Device[]>;
  ticketForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    status: ['NOWE', Validators.required],
    deviceId: [0, Validators.required], // teraz trzymamy tylko ID urządzenia
  });

  ngOnInit(): void {
    this.deviceService.getDevices().subscribe(devices => {
      this.devices = devices;
      this.filteredDevices = this.deviceControl.valueChanges.pipe(
        startWith(''),
        map(value => {
          const filterValue = typeof value === 'string' ? value : value?.id?.toString() ?? '';
          return this.devices.filter(device =>device.id.toString().includes(filterValue));
        })
      )
    });

  }
  displayFn(device: Device): string {
    return device ? `${device.id} ${device.name} (${device.serialNumber})` : '';
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
