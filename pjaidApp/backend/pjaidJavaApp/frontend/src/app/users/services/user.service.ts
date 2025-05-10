import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../shared/models/user.model';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({providedIn: 'root'})
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/users`;

  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.baseUrl, user);
  }
}
