import {Component, inject} from '@angular/core';
import { User } from '../../shared/models/user.model';
import {AuthService} from '../../auth/services/auth.service';

@Component({
  selector: 'app-top-navbar',
  standalone: true,
  imports: [],
  templateUrl: './top-navbar.component.html',
  styleUrl: './top-navbar.component.css',
  providers: [AuthService]
})
export class TopNavbarComponent {
  private readonly authService = inject(AuthService);
  loggedPerson: User | null = this.authService.getLoggedUser();

  logout(): void {
    this.authService.logout();
  }
}
