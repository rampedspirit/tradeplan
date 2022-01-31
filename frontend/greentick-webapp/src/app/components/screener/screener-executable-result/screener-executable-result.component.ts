import { DatePipe } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ExecutableDetailedResponse, ExecutableResponse, ExecutableService, ScreenerService, ScripResult } from 'src/gen/screener';

@Component({
  selector: 'app-screener-executable-result',
  templateUrl: './screener-executable-result.component.html',
  styleUrls: ['./screener-executable-result.component.scss']
})
export class ScreenerExecutableResultComponent implements OnInit {

  fetchError: boolean;

  executable: ExecutableDetailedResponse;
  results: ScripResult[];

  displayedColumns: string[] = ['scripName', 'status'];

  constructor(public dialogRef: MatDialogRef<ScreenerExecutableResultComponent>,
    private executableService: ExecutableService, @Inject(MAT_DIALOG_DATA) private data: { executableId: string },
    private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.refreshResults();
  }

  refreshResults = () => {
    this.fetchError = false;
    this.spinner.show()
    this.executableService.getResult(this.data.executableId).subscribe(executable => {
      this.executable = executable;
      this.results = executable.result;
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
      this.spinner.hide();
    });
  }
}
