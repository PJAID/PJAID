import {Component, inject} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {DeviceService} from '../services/device.service';
import {Device} from '../../shared/models/device.model';

@Component({
  selector: 'app-device-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './device-list.component.html',
  styleUrl: './device-list.component.css',
  providers: [DeviceService],
})
export class DeviceListComponent {
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);

  devices: Device[] = [];

  ngOnInit() {
    this.deviceService.getDevices().subscribe(devices => {
      this.devices = devices;
    });
  }

  goToDetails(deviceId: number) {
    this.router.navigate(['/devices', deviceId]);
  }
}
