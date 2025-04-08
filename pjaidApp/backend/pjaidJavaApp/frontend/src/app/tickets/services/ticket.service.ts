import { inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TicketResponse} from '../models/ticket-response.model';
import { Observable } from 'rxjs';

export class TicketService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/ticket';

  getTicket(id: number): Observable<TicketResponse> {
    return this.http.get<TicketResponse>(`${this.baseUrl}/${id}`);
  }

  getAllTickets(): Observable<TicketResponse[]> {
    return this.http.get<TicketResponse[]>(`${this.baseUrl}/active`);
  }
}
