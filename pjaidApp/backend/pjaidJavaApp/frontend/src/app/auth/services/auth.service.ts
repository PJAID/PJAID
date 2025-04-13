import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {User} from '../../shared/models/user.model';

export class AuthService {
  private readonly router = inject(Router);
  private readonly STORAGE_KEY = 'user';

  login(username: string, password: string): boolean {
    if (username === 'admin' && password === 'admin') {
      const user: User = {
        id: 1,
        userName: 'admin',
        email: 'admin@example.com'
      };
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(user));
      return true;
    }
    return false;
  }

  logout(): void {
    localStorage.removeItem(this.STORAGE_KEY);
    this.router.navigate(['/login']);
  }

  getLoggedUser(): User | null {
    const data = localStorage.getItem(this.STORAGE_KEY);
    return data ? JSON.parse(data) as User : null;
  }

  isLoggedIn(): boolean {
    return !!this.getLoggedUser();
  }
}
