import {inject} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {TicketResponse} from '../../shared/models/ticket-response.model';
import {Observable} from 'rxjs';

export class TicketService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/ticket';

  getTicket(id: number): Observable<TicketResponse> {
    return this.http.get<TicketResponse>(`${this.baseUrl}/${id}`);
  }

  // getAllTickets(): Observable<TicketResponse[]> {
  //   return this.http.get<TicketResponse[]>(`${this.baseUrl}`);
  // }

  getAllTickets(filters?: any): Observable<TicketResponse[]> {
    let params = new HttpParams();

    if (filters) {
      // Iterujemy po kluczach obiektu filtrów z komponentu
      Object.keys(filters).forEach(key => {
        const value = filters[key];

        // Dodajemy parametr tylko jeśli ma wartość (nie jest null, undefined, pusty string)
        // Możesz dostosować tę logikę, jeśli np. pusty string ma specjalne znaczenie
        if (value !== null && value !== undefined && value !== '') {
          // Klucze użyte w params.append() MUSZĄ odpowiadać nazwom @RequestParam w kontrolerze
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

}
