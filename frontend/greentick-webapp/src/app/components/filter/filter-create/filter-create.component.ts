import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NgxSpinnerService } from 'ngx-spinner';
import { Filter, FilterService } from 'src/app/_gen';

@Component({
  selector: 'app-filter-create',
  templateUrl: './filter-create.component.html',
  styleUrls: ['./filter-create.component.scss']
})
export class FilterCreateComponent implements OnInit {

  createFilterForm: FormGroup;

  get nameControl(): FormControl {
    return this.createFilterForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.createFilterForm.get('description') as FormControl;
  }

  constructor(public dialogRef: MatDialogRef<FilterCreateComponent>, private filterService: FilterService,
    private snackBar: MatSnackBar, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.createFilterForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      description: new FormControl(null, [Validators.required])
    });
  }

  submit() {
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

  cancel(): void {
    this.dialogRef.close();
  }

  private createFilter(name: string, description: string) {
    this.spinner.show();
    this.filterService.createFilter({
      name: name,
      description: description
    }).subscribe(filter => {
      this.dialogRef.close(filter);
      this.spinner.hide();
    }, error => {
      this.snackBar.open("Failed to create new filter '" + name + "', Please Retry.", "OK", {
        verticalPosition: "bottom",
        horizontalPosition: "center"
      });
      this.dialogRef.close();
      this.spinner.hide();
    });
  }
}
