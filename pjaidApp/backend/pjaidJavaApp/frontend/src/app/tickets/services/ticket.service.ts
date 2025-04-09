import {inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TicketResponse} from '../models/ticket-response.model';
import {Observable} from 'rxjs';

export class TicketService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/ticket';

  getTicket(id: number): Observable<TicketResponse> {
    return this.http.get<TicketResponse>(`${this.baseUrl}/${id}`);
  }

  getAllTickets(): Observable<TicketResponse[]> {
    return this.http.get<TicketResponse[]>(`${this.baseUrl}`);
  }

  addTicket(ticket: Partial<TicketResponse>): Observable<TicketResponse> {
    return this.http.post<TicketResponse>(`${this.baseUrl}`, ticket);
  }

  updateTicket(id: number, ticket: Partial<TicketResponse>): Observable<TicketResponse> {
    return this.http.put<TicketResponse>(`${this.baseUrl}/${id}`, ticket);
  }

}
