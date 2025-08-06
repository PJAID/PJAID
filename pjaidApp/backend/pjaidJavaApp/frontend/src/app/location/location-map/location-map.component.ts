import {Component, Input} from '@angular/core';
import {NgForOf, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-location-map',
  imports: [NgForOf, NgOptimizedImage],
  templateUrl: './location-map.component.html',
  styleUrl: './location-map.component.css'
})
export class LocationMapComponent {
  @Input() highlightedHallId: string | null = null;

  halls = [
    {id: 'B1', x: 440, y: 110, width: 120, height: 170},
    {id: 'B2', x: 320, y: 100, width: 115, height: 200},
    {id: 'B3', x: 220, y: 120, width: 90, height: 170},
    {id: 'B4', x: 130, y: 110, width: 65, height: 170},
    {id: 'B5', x: 30, y: 110, width: 100, height: 170},
  ];

  width = 600;
  height = 400;
}
