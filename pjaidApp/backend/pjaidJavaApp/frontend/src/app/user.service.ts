import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private _loggedPerson="Romek Atomek";

  get loggedPerson() :string{
    return this._loggedPerson;
  }

  set loggedPerson(name: string){
    this._loggedPerson=name;
  }

  constructor() { }
}
