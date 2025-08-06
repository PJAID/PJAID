import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import * as L from 'leaflet';

@Component({
  selector: 'app-location-details-map',
  standalone: true,
  imports: [],
  templateUrl: './location-details-map.component.html',
  styleUrl: './location-details-map.component.css'
})
export class LocationDetailsMapComponent implements OnChanges {
  @Input() latitude!: number;
  @Input() longitude!: number;
  private map: L.Map | null = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (this.latitude && this.longitude) {
      this.loadMap();
    }
  }

  private loadMap(): void {
    if (this.map) return;

    this.map = L.map('map').setView([this.latitude, this.longitude], 16);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    L.marker([this.latitude, this.longitude]).addTo(this.map)
      .bindPopup('Lokalizacja awarii')
      .openPopup();

    setTimeout(() => {
      this.map?.invalidateSize();
    }, 500);
  }
}
