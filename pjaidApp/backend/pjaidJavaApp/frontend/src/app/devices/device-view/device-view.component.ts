import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {DeviceService} from '../services/device.service';
import {DeviceResponse} from '../../shared/models/device-response.model';

@Component({
  selector: 'app-device-view',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './device-view.component.html',
  styleUrl: './device-view.component.css',
  providers: [DeviceService],
})
export class DeviceViewComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);

  device?: DeviceResponse;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.deviceService.getDeviceById(id).subscribe(device => {
      this.device = device;
    });
  }

  goBack() {
    this.router.navigate(['/devices']);
  }
}
