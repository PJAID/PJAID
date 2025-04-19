import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {DeviceService} from '../services/device.service';
import {DeviceResponse} from '../../shared/models/device-response.model';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-device-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './device-list.component.html',
  styleUrl: './device-list.component.css',
  providers: [DeviceService],
})
export class DeviceListComponent implements OnInit {
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);

  devices: DeviceResponse[] = [];
  isLoading = false;

  filterName: string = '';
  filterPurchaseDate: string = '';
  filterLastService: string = '';

  ngOnInit(): void {
    this.deviceService.getDevices().subscribe(devices => {
      this.devices = devices;
    });
  }

  loadDevices(): void {
    this.isLoading = true; // Pokaż ładowanie
    const currentFilters = {
      name: this.filterName,
      purchaseDate: this.filterPurchaseDate, // Klucz pasuje do @RequestParam
      lastService: this.filterLastService   // Klucz pasuje do @RequestParam
    };

    this.deviceService.getDevices(currentFilters).subscribe({
      next: devices => {
        this.devices = devices;
        this.isLoading = false; // Ukryj ładowanie
      },
      error: err => {
        console.error("Błąd podczas ładowania urządzeń:", err);
        this.isLoading = false; // Ukryj ładowanie przy błędzie
      }
    });
  }

  applyFilters(): void {
    this.loadDevices(); // Przeładuj dane z filtrami
  }

  clearFilters(): void {
    // Wyczyść pola filtrów
    this.filterName = '';
    this.filterPurchaseDate = '';
    this.filterLastService = '';
    this.loadDevices(); // Załaduj dane bez filtrów
  }
}
