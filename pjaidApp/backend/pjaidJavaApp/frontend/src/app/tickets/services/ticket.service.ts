import {inject} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {TicketResponse} from '../../shared/models/ticket-response.model';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

export class TicketService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/ticket`;

  getTicket(id: number): Observable<TicketResponse> {
    return this.http.get<TicketResponse>(`${this.baseUrl}/${id}`);
  }

  getAllTickets(filters?: any): Observable<TicketResponse[]> {
    let params = new HttpParams();

    if (filters) {
      Object.keys(filters).forEach(key => {
        const value = filters[key];

        if (value !== null && value !== undefined && value !== '') {
          params = params.append(key, value);
        }
      });
    }
    return this.http.get<TicketResponse[]>(this.baseUrl, {params});
  }

  addTicket(ticket: Partial<TicketResponse>): Observable<TicketResponse> {
    return this.http.post<TicketResponse>(`${this.baseUrl}`, ticket);
  }

  updateTicket(id: number, ticket: Partial<TicketResponse>): Observable<TicketResponse> {
    return this.http.put<TicketResponse>(`${this.baseUrl}/${id}`, ticket);
  }
  getTicketsByUser(username: string): Observable<TicketResponse[]> {
    return this.http.get<TicketResponse[]>(`http://localhost:8080/ticket?user=${username}`);
  }

}
