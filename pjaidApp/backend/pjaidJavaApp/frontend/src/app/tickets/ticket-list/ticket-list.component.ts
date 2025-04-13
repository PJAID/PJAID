import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {TicketService} from '../services/ticket.service';
import {TicketResponse} from '../../shared/models/ticket-response.model';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './ticket-list.component.html',
  styleUrl: './ticket-list.component.css',
  providers: [TicketService]
})
export class TicketListComponent implements OnInit{
  private readonly ticketService = inject(TicketService);
  tickets: TicketResponse[] = [];
  isLoading = false;

  filterStatus: string = '';
  filterUserName: string = '';
  filterDeviceName: string = '';
  filterTitle: string = '';


  ngOnInit(): void {
    this.ticketService.getAllTickets().subscribe(data => {
      this.tickets = data;
    });
  }

  loadTickets(): void {
    this.isLoading = true;

    // --- Tworzenie obiektu filtrów dla serwisu ---
    // Klucze obiektu MUSZĄ pasować do nazw parametrów backendu: status, userName, device, titleContains
    const currentFilters = {
      status: this.filterStatus,
      userName: this.filterUserName,
      device: this.filterDeviceName, // Klucz 'device' pasuje do @RequestParam("device") w backendzie
      titleContains: this.filterTitle // Klucz 'titleContains' pasuje do @RequestParam("titleContains")
    };

    // Wywołaj serwis z aktualnymi filtrami
    // Serwis sam zadba o usunięcie pustych wartości przed wysłaniem (zgodnie z kodem z Kroku 1)
    this.ticketService.getAllTickets(currentFilters).subscribe({
      next: data => {
        this.tickets = data;
        this.isLoading = false;
      },
      error: err => {
        console.error("Błąd podczas ładowania ticketów:", err);
        this.isLoading = false;
        // Rozważ wyświetlenie błędu użytkownikowi
      }
    });
  }

  // Wywoływana po kliknięciu przycisku "Filtruj"
  applyFilters(): void {
    this.loadTickets();
  }

  // Wywoływana po kliknięciu przycisku "Wyczyść filtry"
  clearFilters(): void {
    // Resetuj właściwości komponentu przechowujące wartości filtrów
    this.filterStatus = '';
    this.filterUserName = '';
    this.filterDeviceName = '';
    this.filterTitle = '';
    // Ponownie załaduj dane (teraz bez aktywnych filtrów)
    this.loadTickets();
  }
}
