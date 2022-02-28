import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  message: string;
  submissionError: string;
  loginForm: FormGroup;

  get emailControl(): FormControl {
    return this.loginForm.get('email') as FormControl;
  }

  get passwordControl(): FormControl {
    return this.loginForm.get('password') as FormControl;
  }

  constructor(private authService: AuthService, private spinner: NgxSpinnerService, private router: Router,
    private activatedroute: ActivatedRoute) { }

  ngOnInit(): void {
    this.reset();

    this.loginForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, [Validators.required])
    });

    this.activatedroute.paramMap.subscribe(params => {
      if (params.get("message")) {
        this.message = params.get("message");
      }
    });
  }

  onSubmit() {
    this.reset();
    this.validateAllFields();
    if (this.loginForm.valid) {
      let email = this.loginForm.get('email')?.value;
      let password = this.loginForm.get('password')?.value;

      this.spinner.show();
      this.authService.login(email, password)
        .then(user => {
          this.reset();
          this.router.navigate(['/home']);
          console.log("Login Successfull");
        })
        .catch(error => {
          this.reset();
          if (error.message == "User is not confirmed.") {
            this.router.navigate(['/auth/verify', { "email": email, "message": "Email Verification Code Sent to <b>" + email + "</b>" }]);
            console.log("User not confirmed");
          } else {
            this.submissionError = error.message;
            console.log("Login Failure :" + error.message);
          }
        });
    }
  }

  private validateAllFields() {
    Object.values(this.loginForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private reset() {
    this.message = null;
    this.submissionError = null;
    this.spinner.hide();
  }
}
