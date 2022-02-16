import { ElementRef, EventEmitter, ViewChild } from '@angular/core';
import { Component, Inject, Input, OnInit, Output } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { ExecutableResponse, ExecutableService, ScripResult } from 'src/gen/screener';
import * as LightWeightCharts from 'lightweight-charts';
import { ResultService } from 'src/gen/condition';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-screener-executable-result',
  templateUrl: './screener-executable-result.component.html',
  styleUrls: ['./screener-executable-result.component.scss']
})
export class ScreenerExecutableResultComponent implements OnInit {

  @Input()
  executableId: string;

  @Input()
  conditionId: string;

  @Output()
  back: EventEmitter<void> = new EventEmitter<void>();

  fetchError: boolean;
  results: ScripResult[];
  executable: ExecutableResponse;
  displayedColumns: string[] = ['scripName', 'status'];

  selectedScrip: string;
  chart: LightWeightCharts.IChartApi;

  constructor(private executableService: ExecutableService, private resultService: ResultService,
    private spinner: NgxSpinnerService, private datePipe: DatePipe) { }

  ngOnInit(): void {
    this.refreshResults();
  }

  refreshResults = () => {
    this.fetchError = false;
    this.spinner.show()
    this.executableService.getResult(this.executableId).subscribe(executable => {
      this.executable = executable;
      this.results = executable.result;
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
      this.spinner.hide();
    });
  }

  onScripSelected(scripName: string) {
    this.selectedScrip = scripName;
    let dateStr: string = this.datePipe.transform(this.executable.marketTime, 'yyyy-MM-ddTHH:mm:ss', "GMT+0530");
    this.resultService.getResult(this.conditionId, dateStr+"+05:30", scripName).subscribe(result => {
      console.log(result);
    }, error => {
      console.log(error);
    });
  }
}
