import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {LocationComponent} from './location/location.component';
import {TimetableComponent} from './timetable/timetable.component';
import {DevicesComponent} from './devices/devices.component';
import {ReportsComponent} from './reports/reports.component';
import {NewReportComponent} from './new-report/new-report.component';

export const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'newReport', component: NewReportComponent},
  {path: 'location', component: LocationComponent},
  {path: 'timetable', component: TimetableComponent},
  {path: 'devices', component: DevicesComponent},
  {path: 'reports', component: ReportsComponent},
];

