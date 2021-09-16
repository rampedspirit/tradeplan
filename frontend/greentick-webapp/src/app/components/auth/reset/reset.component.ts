import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html',
  styleUrls: ['./reset.component.scss']
})
export class ResetComponent implements OnInit {

  submissionError: string;
  isEmailSent: boolean;

  resetForm: FormGroup;
  verifyForm: FormGroup;

  get emailControl(): FormControl {
    return this.resetForm.get('email') as FormControl;
  }

  get codeControl(): FormControl {
    return this.verifyForm.get('code') as FormControl;
  }

  get passwordControl(): FormControl {
    return this.verifyForm.get('password') as FormControl;
  }

  constructor(private router: Router, private authService: AuthService, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.reset();

    this.resetForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
    });

    this.verifyForm = new FormGroup({
      code: new FormControl(null, [Validators.required]),
      password: new FormControl(null, [Validators.required, Validators.pattern(".*\\d+.*"), Validators.min(8)])
    });
  }

  generateVerificationCode() {
    this.reset();
    this.validateResetFormFields();

    if (this.resetForm.valid) {
      let email = this.resetForm.get('email')?.value;

      this.spinner.show();
      this.authService.forgotPassword(email)
        .then(result => {
          this.reset();
          this.isEmailSent = true;
          console.log("Password Reset Email Successfull");
        })
        .catch(error => {
          this.reset();
          this.submissionError = error.message;
          console.log("Password Reset Email Failure :" + error.message);
        });
    }
  }

  verify() {
    this.validateVerifyFormFields();

    if (this.verifyForm.valid) {
      let email = this.resetForm.get('email')?.value;
      let code = this.verifyForm.get('code')?.value;
      let password = this.verifyForm.get('password')?.value;

      this.spinner.show();
      this.authService.resetPassword(email, code, password)
        .then(result => {
          this.reset();
          this.router.navigate(['/auth/login', { "message": "Password Reset Successfull. Please Login." }]);
          console.log("Password Reset Successfull");
        })
        .catch(error => {
          this.reset();
          this.verifyForm.reset();
          this.submissionError = error.message;
          console.log("Password Reset Failure :" + error.message);
        });
    }
  }

  private validateResetFormFields() {
    Object.values(this.resetForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private validateVerifyFormFields() {
    Object.values(this.resetForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private reset() {
    this.submissionError = null;
    this.spinner.hide();
    this.isEmailSent = false;
  }
}
