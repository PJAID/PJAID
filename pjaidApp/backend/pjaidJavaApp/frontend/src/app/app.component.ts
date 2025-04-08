import { Component } from '@angular/core';
import {NavbarComponent} from './layout/navbar/navbar.component';
import {TopNavbarComponent} from './layout/top-navbar/top-navbar.component';
import {RouterModule, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, NavbarComponent, TopNavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {}
