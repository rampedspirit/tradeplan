import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UUID } from 'angular2-uuid';
import { NgxSpinnerService } from 'ngx-spinner';
import { ScreenerService } from 'src/gen/screener';
import { ScreenerNotificationService } from '../screener-notification.service';

@Component({
  selector: 'app-screener-create',
  templateUrl: './screener-create.component.html',
  styleUrls: ['./screener-create.component.scss']
})
export class ScreenerCreateComponent implements OnInit {

  createScreenerForm: FormGroup;
  createError: boolean;

  get nameControl(): FormControl {
    return this.createScreenerForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.createScreenerForm.get('description') as FormControl;
  }

  constructor(public dialogRef: MatDialogRef<ScreenerCreateComponent>, private screenerService: ScreenerService,
    private screenerNotificationService: ScreenerNotificationService, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.createScreenerForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      description: new FormControl(null, [Validators.required])
    });
  }

  submit = () => {
    this.validateAllFields();
    if (this.createScreenerForm.valid) {
      let name = this.createScreenerForm.get('name')?.value;
      let description = this.createScreenerForm.get('description')?.value;
      this.createScreener(name, description);
    }
  }

  private validateAllFields() {
    Object.values(this.createScreenerForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private createScreener(name: string, description: string) {
    this.spinner.show();
    this.createError = false;
    this.screenerService.createScreener({
      name: name,
      description: description
    }).subscribe(screener => {
      this.dialogRef.close();
      this.screenerNotificationService.triggerCreateNotification(screener);
      this.spinner.hide();
    }, error => {
      this.createError = true;
      this.spinner.hide();
    });
  }

}
