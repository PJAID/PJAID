import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {DeviceService} from '../services/device.service';
import {DeviceRequest} from '../../shared/models/device-request.model';
import {DeviceResponse} from '../../shared/models/device-response.model';

@Component({
  selector: 'app-device-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './device-new.component.html',
  styleUrl: './device-new.component.css',
  providers: [DeviceService]
})
export class DeviceNewComponent implements OnInit {
  private fb = inject(NonNullableFormBuilder);
  private deviceService = inject(DeviceService);
  createdDevice?: DeviceResponse;

  deviceForm = this.fb.group({
    name: ['', Validators.required],
    serialNumber: [''],
    purchaseDate: [''],
    lastService: ['']
  });

  ngOnInit(): void {
    //puste
  }

  submit(): void {
    if (this.deviceForm.valid) {
      const dto: DeviceRequest = this.deviceForm.getRawValue();
      this.deviceService.addDevice(dto)
        .subscribe({
          next: device => {
            this.createdDevice = device;
          },
          error: err => {
            console.error('Błąd przy tworzeniu urządzenia', err);
          }
        });
    }
  }

  get downloadHref(): string {
    return this.createdDevice?.qrCode || '';
  }

  get downloadName(): string {
    return this.createdDevice
      ? `device-${this.createdDevice.id}.png`
      : 'qr.png';
  }
}
