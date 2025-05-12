import {Component, inject} from '@angular/core';
import {AuthService} from '../../auth/services/auth.service';
import {Router} from '@angular/router';

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
  private readonly router = inject(Router);
  loggedPerson: string | null = this.authService.getLoggedUser();

  logout(): void {
    const refreshToken = this.authService.getRefreshToken();
    if (refreshToken) {
      this.authService.logout().subscribe({
        complete: () => {
          this.authService.clearTokens();
          this.router.navigate(['/login']);
        },
        error: () => {
          this.authService.clearTokens(); //
          this.router.navigate(['/login']);
        }
      });
    } else {
      this.authService.clearTokens();
      this.router.navigate(['/login']);
    }
  }
}
