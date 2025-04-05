import { Component } from '@angular/core';
import {RouterModule} from '@angular/router';
import {NavbarComponent} from './navbar/navbar.component';
import {TopNavbarComponent} from './top-navbar/top-navbar.component';

@Component({
  selector: 'app-root',
  imports: [RouterModule,NavbarComponent,TopNavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
}
