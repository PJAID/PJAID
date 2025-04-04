import { Routes } from '@angular/router';
// import { TicketListComponent } from './components/ticket-list/ticket-list.component';
import { TicketDetailsComponent } from './components/ticket-details/ticket-details.component';

export const routes: Routes = [
  { path: 'ticket/:id', component: TicketDetailsComponent },
  { path: '', redirectTo: 'ticket/1', pathMatch: 'full' },
  // { path: 'ticket/:id', component: TicketListComponent }
];
