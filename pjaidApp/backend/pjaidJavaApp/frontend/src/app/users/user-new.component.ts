import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserService} from './services/user.service';
import {Router} from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-user-new',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './user-new.component.html',
  styleUrl: './user-new.component.css',
})
export class UserNewComponent {
  form: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly userService: UserService,
    private readonly router: Router
  ) {
    this.form = this.fb.group({
      userName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      roles: this.fb.control([]),
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    this.userService.createUser(this.form.value).subscribe({
      next: () => this.router.navigate(['/users']),
      error: err => alert('Błąd tworzenia użytkownika: ' + err.message)
    });
  }
}
