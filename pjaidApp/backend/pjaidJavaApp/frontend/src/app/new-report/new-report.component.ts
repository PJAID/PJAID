import { Component } from '@angular/core';
import {UserService} from '../user.service';

@Component({
  selector: 'app-new-report',
  imports: [],
  templateUrl: './new-report.component.html',
  styleUrl: './new-report.component.css'
})
export class NewReportComponent {
  constructor(private userService: UserService) {
  }
  logedPerson=''
  obiect_type="drukaraka"
  obiect_name="Epson"
  obiect_code=2137
  obiect_poss="Gdańsk, ul.Gdańska,00-000"

  ngOnInit(){
    this.logedPerson=this.userService.loggedPerson;
  }
}
