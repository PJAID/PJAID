import { Routes } from '@angular/router';
import { TicketListComponent } from './components/ticket-list/ticket-list.component';
import { TicketDetailsComponent } from './components/ticket-details/ticket-details.component';

export const routes: Routes = [
  {
    path: 'ticket/:id',
    loadComponent: () => import('./components/ticket-details/ticket-details.component').then(m => m.TicketDetailsComponent)
  },
  {
    path: 'ticket/active',
    loadComponent: () => import('./components/ticket-list/ticket-list.component').then(m => m.TicketListComponent)
  }
];
