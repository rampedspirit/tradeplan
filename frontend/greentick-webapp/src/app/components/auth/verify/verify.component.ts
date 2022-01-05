import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.scss']
})
export class VerifyComponent implements OnInit {

  email: string;
  message: string;
  submissionError: string;

  verifyForm: FormGroup;

  get codeControl(): FormControl {
    return this.verifyForm.get('code') as FormControl;
  }

  constructor(private activatedroute: ActivatedRoute, private router: Router, private authService: AuthService,
    private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.reset();

    this.activatedroute.paramMap.subscribe(params => {
      if (!params.get("email") || !params.get("message")) {
        this.router.navigate(['/error']);
      } else {
        this.email = params.get("email");
        this.message = params.get("message");

        this.verifyForm = new FormGroup({
          code: new FormControl(null, [Validators.required])
        });

      }
    });
  }

  generateVerificationCode() {
    this.reset();

    this.spinner.show();
    this.authService.resendEmailVerification(this.email)
      .then(result => {
        this.reset();
        this.message = "Email verification code resent to email id <b>" + this.email + "</b>";
        console.log("Email Verification Resend Successfull");
      }).catch(error => {
        this.reset();
        this.submissionError = error.message + " : Please Retry Again.";
        console.log("Email Verification Resend Failure");
      });
  }

  verify() {
    this.validateVerifyFormFields();

    if (this.verifyForm.valid) {
      let code = this.verifyForm.get('code')?.value;

      this.spinner.show();
      this.authService.verifyEmail(this.email, code)
        .then(result => {
          this.reset();
          this.router.navigate(['/auth/login', { "message": "Signup Successfull. Please Login." }]);
          console.log("Email Verification Successfull");
        })
        .catch(error => {
          this.reset();
          this.submissionError = error.message;
          console.log("Email Verification Failure :" + error.message);
        });
    }
  }

  private validateVerifyFormFields() {
    Object.values(this.verifyForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private reset() {
    this.message = null;
    this.submissionError = null;
    this.spinner.hide()
  }
}
