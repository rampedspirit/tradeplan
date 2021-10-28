import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { FilterService } from 'src/gen/filter';
import { FilterNotificationService } from '../filter-notification.service';
import { FilterValidators } from '../filter-validators';

@Component({
  selector: 'app-filter-create',
  templateUrl: './filter-create.component.html',
  styleUrls: ['./filter-create.component.scss']
})
export class FilterCreateComponent implements OnInit {

  isValidating: boolean;

  createFilterForm: FormGroup;
  createError: boolean;

  get nameControl(): FormControl {
    return this.createFilterForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.createFilterForm.get('description') as FormControl;
  }

  constructor(public dialogRef: MatDialogRef<FilterCreateComponent>, private filterService: FilterService,
    private filterNotificationService: FilterNotificationService, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.createFilterForm = new FormGroup({
      name: new FormControl(null, {
        validators: [Validators.required, FilterValidators.noSpace],
        asyncValidators: [FilterValidators.notUniqueWithCallback(this.filterService, () => this.isValidating = true, () => this.isValidating = false)],
        updateOn: 'blur'
      }),
      description: new FormControl(null, [Validators.required])
    });
  }

  submit = () => {
    this.validateAllFields();
    if (this.createFilterForm.valid) {
      let name = this.createFilterForm.get('name')?.value;
      let description = this.createFilterForm.get('description')?.value;
      this.createFilter(name, description);
    }
  }

  private validateAllFields() {
    Object.values(this.createFilterForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private createFilter(name: string, description: string) {
    this.spinner.show();
    this.createError = false;
    this.filterService.createFilter({
      name: name,
      description: description
    }).subscribe(filter => {
      this.dialogRef.close();
      this.filterNotificationService.triggerCreateNotification(filter);
      this.spinner.hide();
    }, error => {
      this.createError = true;
      this.spinner.hide();
    });
  }
}
