import { Routes } from '@angular/router';
import { TicketDetailsComponent } from './components/ticket-details/ticket-details.component';

export const routes: Routes = [
  { path: 'ticket/:id', component: TicketDetailsComponent },
  { path: '', redirectTo: '/ticket/1', pathMatch: 'full' }, // tymczasowo dla testów
];
