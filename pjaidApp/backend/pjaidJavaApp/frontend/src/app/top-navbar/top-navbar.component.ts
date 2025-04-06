import { Component } from '@angular/core';
import {UserService} from '../user.service';

@Component({
  selector: 'app-top-navbar',
  imports: [],
  templateUrl: './top-navbar.component.html',
  styleUrl: './top-navbar.component.css'
})
export class TopNavbarComponent {
  loggedPerson=""
  constructor(private userService: UserService) {}
  ngOnInit(){
    this.loggedPerson=this.userService.loggedPerson;
  }
}
