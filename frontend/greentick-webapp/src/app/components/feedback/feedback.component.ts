import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.scss']
})
export class FeedbackComponent implements OnInit {
  submissionError: string;
  feedbackForm: FormGroup;


  get nameControl(): FormControl {
    return this.feedbackForm.get('name') as FormControl;
  }

  get emailControl(): FormControl {
    return this.feedbackForm.get('email') as FormControl;
  }

  get feedbackControl(): FormControl {
    return this.feedbackForm.get('feedback') as FormControl;
  }

  constructor(private spinner: NgxSpinnerService) {
   
  }

  ngOnInit(): void {
    this.reset();
    this.feedbackForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required, Validators.email]),
      feedback: new FormControl(null, [Validators.required])
    });
  }

  onSubmit() {
    this.reset();
    this.validateAllFields();
    if (this.feedbackForm.valid) {
      let name = this.feedbackForm.get('name')?.value;
      let email = this.feedbackForm.get('email')?.value;
      let password = this.feedbackForm.get('feedback')?.value;

      this.spinner.show();
    }
  }

  private validateAllFields() {
    Object.values(this.feedbackForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private reset() {
    this.submissionError = null;
    this.spinner.hide();
  }
}
