import {Injectable} from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import {BehaviorSubject, catchError, filter, finalize, Observable, switchMap, take, tap, throwError} from 'rxjs';
import {AuthService} from './auth.service';
import {AuthResponse} from '../auth.types';
import {environment} from '../../../environments/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private readonly refreshTokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

  constructor(private readonly authService: AuthService, private readonly http: HttpClient) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = this.authService.getAccessToken();
    let authReq = req;

    if (accessToken) {
      authReq = this.addTokenHeader(req, accessToken);
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !authReq.url.includes('/api/auth/login')) {
          return this.handle401Error(authReq, next);
        }
        return throwError(() => error);
      })
    );
  }

  private addTokenHeader(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {Authorization: `Bearer ${token}`},
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const refreshToken = this.authService.getRefreshToken();
      if (!refreshToken) {
        this.authService.logout();
        return throwError(() => new Error('Brak refresh tokena'));
      }

      return this.http
        .post<AuthResponse>(`${environment.apiBaseUrl}/api/auth/refresh`, {refreshToken})
        .pipe(
          tap((response: AuthResponse) => {
            this.authService.saveTokens(response.accessToken, response.refreshToken);
            this.refreshTokenSubject.next(response.accessToken);
          }),
          switchMap((response: AuthResponse) => next.handle(this.addTokenHeader(request, response.accessToken))),
          finalize(() => (this.isRefreshing = false))
        );
    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token !== null),
        take(1),
        switchMap(token => next.handle(this.addTokenHeader(request, token!)))
      );
    }
  }
}

