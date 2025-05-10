import {Component, inject} from '@angular/core';
import {NavbarComponent} from './layout/navbar/navbar.component';
import {TopNavbarComponent} from './layout/top-navbar/top-navbar.component';
import {Router, RouterModule, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {AuthService} from './auth/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, NavbarComponent, TopNavbarComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly authService = inject(AuthService);

  constructor(public router: Router) {
  }

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
}
