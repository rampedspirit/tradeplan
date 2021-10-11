import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ConditionService } from 'src/gen/condition';
import { ConditionNotificationService } from '../condition-notification.service';

@Component({
  selector: 'app-condition-create',
  templateUrl: './condition-create.component.html',
  styleUrls: ['./condition-create.component.scss']
})
export class ConditionCreateComponent implements OnInit {

  createConditionForm: FormGroup;
  createError: boolean;

  get nameControl(): FormControl {
    return this.createConditionForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.createConditionForm.get('description') as FormControl;
  }

  constructor(public dialogRef: MatDialogRef<ConditionCreateComponent>, private conditionService: ConditionService,
    private conditionNotificationService: ConditionNotificationService, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.createConditionForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      description: new FormControl(null, [Validators.required])
    });
  }

  submit = () => {
    this.validateAllFields();
    if (this.createConditionForm.valid) {
      let name = this.createConditionForm.get('name')?.value;
      let description = this.createConditionForm.get('description')?.value;
      this.createCondition(name, description);
    }
  }

  private validateAllFields() {
    Object.values(this.createConditionForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private createCondition(name: string, description: string) {
    this.spinner.show();
    this.createError = false;
    this.conditionService.createCondition({
      name: name,
      description: description
    }).subscribe(condition => {
      this.dialogRef.close();
      this.conditionNotificationService.triggerCreateNotification(condition);
      this.spinner.hide();
    }, error => {
      this.createError = true;
      this.spinner.hide();
    });
  }
}
