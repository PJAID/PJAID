import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {DeviceService} from '../services/device.service';
import {Device} from '../../shared/models/device.model';

@Component({
  selector: 'app-device-view',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './device-view.component.html',
  styleUrl: './device-view.component.css',
  providers: [DeviceService],
})
export class DeviceViewComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);

  device?: Device;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.deviceService.getDeviceById(id).subscribe(device => {
      this.device = device;
    });
  }

  goBack() {
    this.router.navigate(['/devices']);
  }
}
