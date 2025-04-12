import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'pjaidApp',
    pathMatch: 'full'
  },
  {
    path: 'tickets',
    loadComponent: () => import('./tickets/ticket-list/ticket-list.component').then(m => m.TicketListComponent)
  },
  {
    path: 'ticket/:id',
    loadComponent: () => import('./tickets/ticket-view/ticket-view.component').then(m => m.TicketViewComponent)
  },
  {
    path: 'ticket/:id/edit',
    loadComponent: () => import('./tickets/ticket-edit/ticket-edit.component').then(m => m.TicketEditComponent)
  },
  {
    path: 'devices',
    loadComponent: () => import('./devices/device-list/device-list.component').then(m => m.DeviceListComponent)
  },
  {
    path: 'devices/:id',
    loadComponent: () => import('./devices/device-view/device-view.component').then(m => m.DeviceViewComponent)
  },
  {
    path: 'timetable',
    loadComponent: () => import('./timetable/timetable-view/timetable-view.component').then(m => m.TimetableViewComponent)
  },
  {
    path: 'location',
    loadComponent: () => import('./location/location-view/location-view.component').then(m => m.LocationViewComponent)
  },
  {
    path: 'reportTicket',
    loadComponent: () => import('./tickets/ticket-new/ticket-new.component').then(m => m.TicketNewComponent)
  },
  {
    path: '**',
    redirectTo: 'pjaidApp'
  }
];
