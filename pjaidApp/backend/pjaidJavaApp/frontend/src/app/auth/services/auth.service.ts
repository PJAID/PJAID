import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {AuthRequest, AuthResponse} from '../auth.types';

@Injectable({providedIn: 'root'})
export class AuthService {
  private readonly apiUrl = `${environment.apiBaseUrl}/api/auth`;

  constructor(private readonly http: HttpClient) {
  }

  login(data: AuthRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, data);
  }


  logout() {
    const refreshToken = this.getRefreshToken();
    return this.http.post(`${this.apiUrl}/logout`, {refreshToken});
  }

  saveTokens(accessToken: string, refreshToken: string) {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  clearTokens() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  isLoggedIn(): boolean {
    return !!this.getAccessToken();
  }

  getLoggedUser(): string | null {
    const token = this.getAccessToken();
    if (!token) return null;

    const payload = token.split('.')[1];
    try {
      const decoded = JSON.parse(atob(payload));
      return decoded.sub || decoded.username || null;
    } catch {
      return null;
    }
  }

  getDecodedAccessToken(): any {
    const token = this.getAccessToken();
    if (!token) return null;

    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch {
      return null;
    }
  }

  getUserRoles(): string[] {
    const decoded = this.getDecodedAccessToken();
    return decoded?.roles || [];
  }

  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }
}
