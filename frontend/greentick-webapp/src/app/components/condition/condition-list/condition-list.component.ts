import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectionList, MatSelectionListChange } from '@angular/material/list';
import { NgxSpinnerService } from 'ngx-spinner';
import { TabAreaService, Tab } from 'src/app/services/tab-area.service';
import { Condition, ConditionService } from 'src/gen/condition';
import { ConditionCreateComponent } from '../../condition/condition-create/condition-create.component';
import { ConditionNotificationService } from '../condition-notification.service';

@Component({
  selector: 'app-condition-list',
  templateUrl: './condition-list.component.html',
  styleUrls: ['./condition-list.component.scss']
})
export class ConditionListComponent implements OnInit {

  @ViewChild(MatSelectionList)
  conditionList!: MatSelectionList;

  public conditions: Condition[];
  public fetchError = false;

  constructor(private conditionService: ConditionService, private conditionNotificationService: ConditionNotificationService,
    private tabAreaService: TabAreaService, private dialog: MatDialog, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.refresh();

    this.conditionNotificationService.createSubject.subscribe(condition => {
      this.refresh();
      this.openTab(condition);
    });

    this.conditionNotificationService.updateSubject.subscribe(condition => {
      this.refresh();
    });

    this.conditionNotificationService.deleteSubject.subscribe(id => {
      this.refresh();
      this.tabAreaService.closeTab(id);
    });
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();

    this.conditionService.getAllConditions().subscribe(conditions => {
      this.conditions = conditions;
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
      this.fetchError = true;
    });
  }

  openCreateConditionDialog = () => {
    const dialogRef = this.dialog.open(ConditionCreateComponent, {
      minWidth: "30%"
    });
  }

  onSelectionChange(event: MatSelectionListChange) {
    this.conditionList.deselectAll();
    let selectedcondition: Condition = event.options[0].value;
    this.openTab(selectedcondition);
  }

  private openTab(condition: Condition) {
    let tab: Tab = {
      id: condition.id,
      type: 'condition',
      title: condition.name,
      dirtyFlag: false
    }
    this.tabAreaService.openTab(tab);
  }

}
