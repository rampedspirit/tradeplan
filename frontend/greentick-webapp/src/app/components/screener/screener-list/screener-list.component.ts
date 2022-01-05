import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectionList, MatSelectionListChange } from '@angular/material/list';
import { NgxSpinnerService } from 'ngx-spinner';
import { TabAreaService, Tab } from 'src/app/services/tab-area.service';
import { ScreenerResponse, ScreenerService } from 'src/gen/screener';
import { ScreenerCreateComponent } from '../screener-create/screener-create.component';
import { ScreenerNotificationService } from '../screener-notification.service';

@Component({
  selector: 'app-screener-list',
  templateUrl: './screener-list.component.html',
  styleUrls: ['./screener-list.component.scss']
})
export class ScreenerListComponent implements OnInit {

  @ViewChild(MatSelectionList)
  screenerList!: MatSelectionList;

  public screeners: ScreenerResponse[];
  public fetchError = false;

  constructor(private screenerService: ScreenerService, private screenerNotificationService: ScreenerNotificationService,
    private tabAreaService: TabAreaService, private dialog: MatDialog, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.refresh();

    this.screenerNotificationService.createSubject.subscribe(screener => {
      this.refresh();
      this.openTab(screener);
    });

    this.screenerNotificationService.updateSubject.subscribe(screener => {
      this.refresh();
    });

    this.screenerNotificationService.deleteSubject.subscribe(id => {
      this.refresh();
      this.tabAreaService.closeTab(id);
    });
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();

    this.screenerService.getAllScreeners().subscribe(screeners => {
      this.screeners = screeners;
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
      this.fetchError = true;
    });
  }

  openCreateScreenerDialog = () => {
    const dialogRef = this.dialog.open(ScreenerCreateComponent, {
      minWidth: "30%"
    });
  }

  onSelectionChange(event: MatSelectionListChange) {
    this.screenerList.deselectAll();
    let selectedScreener: ScreenerResponse = event.options[0].value;
    this.openTab(selectedScreener);
  }

  private openTab(screener: ScreenerResponse) {
    let tab: Tab = {
      id: screener.screenerId,
      type: 'screener',
      title: screener.name,
      dirtyFlag: false
    }
    this.tabAreaService.openTab(tab);
  }
}
