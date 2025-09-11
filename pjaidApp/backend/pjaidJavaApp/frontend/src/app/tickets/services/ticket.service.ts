import {inject} from '@angular/core';
import {HttpClient, HttpParams } from '@angular/common/http';
import {TicketResponse} from '../../shared/models/ticket-response.model';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export class TicketService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/ticket`;

  getTicket(id: number): Observable<TicketResponse> {
    return this.http.get<TicketResponse>(`${this.baseUrl}/${id}`);
  }

  addTicket(ticket: Partial<TicketResponse>): Observable<TicketResponse> {
    return this.http.post<TicketResponse>(`${this.baseUrl}`, ticket);
  }

  updateTicket(id: number, ticket: Partial<TicketResponse>): Observable<TicketResponse> {
    return this.http.put<TicketResponse>(`${this.baseUrl}/${id}`, ticket);
  }
  getTicketsByUser(username: string): Observable<TicketResponse[]> {
    return this.http.get<TicketResponse[]>(`${this.baseUrl}?user=${username}`);
  }

  startTicket(ticketId: number): Observable<TicketResponse> {
    return this.http.post<TicketResponse>(`${this.baseUrl}/${ticketId}/start`, {});
  }
  getPagedTickets(page: number, size: number = 10, filters?: any): Observable<PageResponse<TicketResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filters) {
      Object.keys(filters).forEach(key => {
        const value = filters[key];
        if (value !== null && value !== undefined && value !== '') {
          params = params.append(key, value);
        }
      });
    }

    return this.http.get<PageResponse<TicketResponse>>(`${this.baseUrl}/paged`, {params});
  }
  updateTicketStatus(id: number, status: string): Observable<TicketResponse> {
    return this.http.patch<TicketResponse>(`${this.baseUrl}/${id}/status`, { status });
  }

  downloadTicketReport(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/report`, { responseType: 'blob' });
  }


}
