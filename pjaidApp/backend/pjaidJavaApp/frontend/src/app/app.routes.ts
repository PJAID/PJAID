import {Routes} from '@angular/router';
import {authGuard} from './auth/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: 'tickets',
        loadComponent: () => import('./tickets/ticket-list/ticket-list.component').then(m => m.TicketListComponent)
      },
      {
        path: 'tickets/:id',
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
        path: 'addDevice',
        loadComponent: () => import('./devices/device-new/device-new.component').then(m => m.DeviceNewComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
