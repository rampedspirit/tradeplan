import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectionList, MatSelectionListChange } from '@angular/material/list';
import { NgxSpinnerService } from 'ngx-spinner';
import { Tab, TabAreaService } from 'src/app/services/tab-area.service';
import { FilterResponse, FilterService } from 'src/gen/filter';
import { FilterCreateComponent } from '../filter-create/filter-create.component';
import { FilterNotificationService } from '../filter-notification.service';

@Component({
  selector: 'app-filter-list',
  templateUrl: './filter-list.component.html',
  styleUrls: ['./filter-list.component.scss']
})
export class FilterListComponent implements OnInit {

  @ViewChild(MatSelectionList)
  filterList!: MatSelectionList;

  public filters: FilterResponse[];
  public fetchError = false;

  constructor(private filterService: FilterService, private filterNotificationService: FilterNotificationService,
    private tabAreaService: TabAreaService, private dialog: MatDialog, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.refresh();

    this.filterNotificationService.createSubject.subscribe(filter => {
      this.refresh();
      this.openTab(filter);
    });

    this.filterNotificationService.updateSubject.subscribe(filter => {
      this.refresh();
    });

    this.filterNotificationService.deleteSubject.subscribe(id => {
      this.refresh();
      this.tabAreaService.closeTab(id);
    });
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();

    this.filterService.getAllFilters().subscribe(filters => {
      this.filters = filters;
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
      this.fetchError = true;
    });
  }

  openCreateFilterDialog = () => {
    const dialogRef = this.dialog.open(FilterCreateComponent, {
      minWidth: "30%"
    });
  }

  onSelectionChange(event: MatSelectionListChange) {
    this.filterList.deselectAll();
    let selectedFilter: FilterResponse = event.options[0].value;
    this.openTab(selectedFilter);
  }

  private openTab(filter: FilterResponse) {
    let tab: Tab = {
      id: filter.id,
      type: 'filter',
      title: filter.name,
      dirtyFlag: false
    }
    this.tabAreaService.openTab(tab);
  }
}
