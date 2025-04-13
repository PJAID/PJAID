import {Component} from '@angular/core';
import {NavbarComponent} from './layout/navbar/navbar.component';
import {TopNavbarComponent} from './layout/top-navbar/top-navbar.component';
import {Router, RouterModule, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, NavbarComponent, TopNavbarComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(public router: Router) {
  }

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
}
