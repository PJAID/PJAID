import {Routes} from '@angular/router';
import {TicketViewComponent} from './tickets/ticket-view/ticket-view.component';
import {TicketListComponent} from './tickets/ticket-list/ticket-list.component';
import {DeviceListComponent} from './devices/device-list/device-list.component';
import {TimetableViewComponent} from './timetable/timetable-view/timetable-view.component';
import {LocationViewComponent} from './location/location-view/location-view.component';
import {TicketNewComponent} from './tickets/ticket-new/ticket-new.component';
import {TicketEditComponent} from './tickets/ticket-edit/ticket-edit.component';

export const routes: Routes = [
  {path: '', redirectTo: 'pjaidApp', pathMatch: 'full'},
  {path: 'tickets', component: TicketListComponent},
  {path: 'ticket/:id', component: TicketViewComponent},
  {path: 'ticket/:id/edit', component: TicketEditComponent},
  {path: 'devices', component: DeviceListComponent},
  {path: 'timetable', component: TimetableViewComponent},
  {path: 'location', component: LocationViewComponent},
  {path: 'reportTicket', component: TicketNewComponent},
  {path: '**', redirectTo: 'pjaidApp'}
];
