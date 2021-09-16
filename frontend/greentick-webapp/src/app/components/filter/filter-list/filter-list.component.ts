import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectionListChange } from '@angular/material/list';
import { NgxSpinnerService } from 'ngx-spinner';
import { Tab, TabAreaService } from 'src/app/services/tab-area.service';
import { Filter, FilterService } from 'src/app/_gen';
import { FilterCreateComponent } from '../filter-create/filter-create.component';

@Component({
  selector: 'app-filter-list',
  templateUrl: './filter-list.component.html',
  styleUrls: ['./filter-list.component.scss']
})
export class FilterListComponent implements OnInit {

  public filters: Filter[];
  public fetchError = false;

  constructor(private filterService: FilterService, private tabAreaService: TabAreaService,
    private dialog: MatDialog, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {
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

  openCreateFilterDialog() {
    const dialogRef = this.dialog.open(FilterCreateComponent, {
      width: "30%",
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(filter => {
      if (filter) {
        this.refresh();
        console.log(filter);
      }
    });
  }

  onSelectionChange(event: MatSelectionListChange) {
    let selectedFilter: Filter = event.options[0].value;
    let tab: Tab = {
      id: selectedFilter.id,
      type: 'filter',
      title: selectedFilter.name,
      data: selectedFilter
    }
    this.tabAreaService.addTab(tab);
  }
}
