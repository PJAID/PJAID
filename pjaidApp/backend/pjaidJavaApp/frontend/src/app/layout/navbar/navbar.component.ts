import { Component } from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthService} from '../../auth/services/auth.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, NgIf],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  constructor(private readonly authService: AuthService) {}

  hasAdminRole(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }
}
