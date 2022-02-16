import { DatePipe, formatDate } from '@angular/common';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ScreenerService } from 'src/gen/screener';

@Component({
  selector: 'app-screener-executable-create',
  templateUrl: './screener-executable-create.component.html',
  styleUrls: ['./screener-executable-create.component.scss']
})
export class ScreenerExecutableCreateComponent implements OnInit {

  createScreenerExecutableForm: FormGroup;
  createError: boolean;

  get marketTimeControl(): FormControl {
    return this.createScreenerExecutableForm.get('marketTime') as FormControl;
  }

  get noteControl(): FormControl {
    return this.createScreenerExecutableForm.get('note') as FormControl;
  }

  constructor(public dialogRef: MatDialogRef<ScreenerExecutableCreateComponent>, @Inject(MAT_DIALOG_DATA) private data: { screenerId: string },
    private screenerService: ScreenerService, private spinner: NgxSpinnerService, private datePipe: DatePipe) { }

  ngOnInit(): void {
    let dateStr: string = this.datePipe.transform(new Date(), 'yyyy-MM-ddTHH:mm', "GMT+0530");
    this.createScreenerExecutableForm = new FormGroup({
      marketTime: new FormControl(dateStr, [Validators.required]),
      note: new FormControl(null, [])
    });
  }

  submit = () => {
    this.validateAllFields();
    if (this.createScreenerExecutableForm.valid) {
      let marketTime = this.createScreenerExecutableForm.get('marketTime')?.value;
      let note = this.createScreenerExecutableForm.get('note')?.value;
      this.createScreenerExecutable(marketTime, note);
    }
  }

  private validateAllFields() {
    Object.values(this.createScreenerExecutableForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private createScreenerExecutable(marketTime: string, note: string) {
    this.spinner.show();
    this.createError = false;
    let date: Date = new Date(marketTime);
    this.screenerService.runScreener({
      marketTime: date,
      note: note,
      scripNames: ["INFY", "HDFC", "TCS", "TATAMOTORS"],
    }, this.data.screenerId).subscribe(screener => {
      this.dialogRef.close(true);
      this.spinner.hide();
    }, error => {
      this.createError = true;
      this.spinner.hide();
    });
  }
}
