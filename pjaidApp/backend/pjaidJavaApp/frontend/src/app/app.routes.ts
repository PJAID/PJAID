import { Routes } from '@angular/router';
import {TicketViewComponent} from './tickets/ticket-view/ticket-view.component';
import {TicketListComponent} from './tickets/ticket-list/ticket-list.component';
import {DeviceListComponent} from './devices/device-list/device-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'pjaidApp', pathMatch: 'full' },
  { path: 'tickets', component: TicketListComponent },
  { path: 'devices', component: DeviceListComponent },
  { path: 'ticket/:id', component: TicketViewComponent },
  { path: '**', redirectTo: 'pjaidApp' }
];
