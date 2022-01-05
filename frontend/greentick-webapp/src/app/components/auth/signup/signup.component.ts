import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { ISignUpResult } from 'amazon-cognito-identity-js';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  submissionError: string;
  signupForm: FormGroup;

  get nameControl(): FormControl {
    return this.signupForm.get('name') as FormControl;
  }

  get emailControl(): FormControl {
    return this.signupForm.get('email') as FormControl;
  }

  get passwordControl(): FormControl {
    return this.signupForm.get('password') as FormControl;
  }

  constructor(private router: Router, private authService: AuthService, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.reset();
    this.signupForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, [Validators.required, Validators.pattern(".*\\d+.*"), Validators.min(8)])
    });
  }

  onSubmit() {
    this.reset();
    this.validateAllFields();
    if (this.signupForm.valid) {
      let name = this.signupForm.get('name')?.value;
      let email = this.signupForm.get('email')?.value;
      let password = this.signupForm.get('password')?.value;

      this.spinner.show();
      this.authService.signup(name, email, password)
        .then(result => {
          this.reset();
          this.router.navigate(['/auth/verify', { "email": email, "message": "Email Verification Code Sent to <b>" + email + "</b>" }]);
          console.log("Signup Successfull");
        })
        .catch(error => {
          this.reset();
          this.submissionError = error.message;
          console.log("Signup Failure :" + error.message);
        });
    }
  }

  private validateAllFields() {
    Object.values(this.signupForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private reset() {
    this.submissionError = null;
    this.spinner.hide();
  }
}
