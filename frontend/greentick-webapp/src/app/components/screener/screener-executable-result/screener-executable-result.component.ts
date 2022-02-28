import { ChangeDetectorRef, ElementRef, EventEmitter, ViewChild } from '@angular/core';
import { Component, Inject, Input, OnInit, Output } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { ExecutableResponse, ExecutableService, ScripResult } from 'src/gen/screener';
import * as LightWeightCharts from 'lightweight-charts';
import { ConditionDetailedResponse, ConditionResultResponse, ResultService } from 'src/gen/condition';
import { DatePipe } from '@angular/common';
import internal from 'stream';
import { FilterResponse, FilterResultResponse, FilterService } from 'src/gen/filter';
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
  fetchError2: boolean;

  results: ScripResult[];
  executable: ExecutableResponse;
  displayedColumns: string[] = ['scripName', 'status'];

  selectedScripName: string;
  filterResult: FilterResultResponse;
  conditionResult: ConditionResultResponse;

  chart: LightWeightCharts.IChartApi;

  constructor(private executableService: ExecutableService, private resultService: ResultService,
    private filterService: FilterService, private spinner: NgxSpinnerService, private datePipe: DatePipe,
    private changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.refreshResults();
  }

  refreshResults = () => {
    this.fetchError = false;
    this.spinner.show();
    this.executableService.getResult(this.executableId).subscribe(executable => {
      this.executable = executable;
      this.results = executable.result;
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
      this.spinner.hide();
    });
  }

  onScripSelected(scripName: any) {
    this.selectedScripName = scripName;
    this.refreshConditionResult();
  }

  refreshConditionResult = () => {
    this.spinner.show();
    this.fetchError2 = false;
    this.filterResult = null;

    let dateStr: string = this.datePipe.transform(this.executable.marketTime, 'yyyy-MM-ddTHH:mm:ss', "GMT+0530");
    this.resultService.getResult(this.conditionId, dateStr + "+05:30", this.selectedScripName).subscribe(result => {
      this.conditionResult = result;
      this.spinner.hide();
    }, error => {
      this.fetchError2 = true;
      this.spinner.hide();
    });
  }

  onFilterSelected(filterId: string) {
    this.spinner.show();
    this.fetchError2 = false;
    
    let dateStr: string = this.datePipe.transform(this.executable.marketTime, 'yyyy-MM-ddTHH:mm:ss', "GMT+0530");
    this.filterService.getFilterResult(filterId, dateStr + "+05:30", this.selectedScripName).subscribe(result => {
      this.filterResult = result;
      this.spinner.hide();
      this.changeDetectorRef.detectChanges();
    }, error => {
      this.fetchError2 = true;
      this.spinner.hide();
    });
  }
}
