import { Component, EventEmitter, Output } from '@angular/core';

interface User {
  username: string;
  role: string;
  isAuthenticated: boolean;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true
})
export class LoginComponent {
  loginForm = {
    username: '',
    password: ''
  };

  @Output() loginSuccess = new EventEmitter<User>();

  login() {
    this.simulateAuth(this.loginForm.username, this.loginForm.password).then(res => {
      if (res.success) {
        this.loginSuccess.emit(res.user);
      } else {
        alert('Błędne dane logowania');
      }
    });
  }

  private async simulateAuth(username: string, password: string): Promise<any> {
    await new Promise(resolve => setTimeout(resolve, 500));

    if (username === 'admin' && password === 'password') {
      return {
        success: true,
        user: {
          username: 'Administrator',
          role: 'Admin',
          isAuthenticated: true
        }
      };
    } else if (username === 'user' && password === 'user') {
      return {
        success: true,
        user: {
          username: 'User',
          role: 'Operator',
          isAuthenticated: true
        }
      };
    } else {
      return { success: false };
    }
  }
}

