import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { QueryBuilderConfig } from 'angular2-query-builder';
import { NgxSpinnerService } from 'ngx-spinner';
import { Tab } from 'src/app/services/tab-area.service';
import { Condition, ConditionService } from 'src/gen/condition';
import { Filter, FilterService } from 'src/gen/filter';
import { ConfirmationComponent } from '../../common/confirmation/confirmation.component';
import { MessageComponent } from '../../common/message/message.component';
import { ConditionQuery } from '../condition-builder/condition-query';
import { ConditionNotificationService } from '../condition-notification.service';

@Component({
  selector: 'app-condition-edit',
  templateUrl: './condition-edit.component.html',
  styleUrls: ['./condition-edit.component.scss']
})
export class ConditionEditComponent implements OnInit {

  fetchError: boolean;
  editConditionForm: FormGroup;
  conditionQuery: ConditionQuery;

  @Input()
  tab: Tab;

  get nameControl(): FormControl {
    return this.editConditionForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.editConditionForm.get('description') as FormControl;
  }

  constructor(private conditionService: ConditionService,
    private conditionNotificationService: ConditionNotificationService,
    private dialog: MatDialog, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.refresh();
    this.conditionQuery = new ConditionQuery(true);
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();
    this.conditionService.getCondition(this.tab.id).subscribe(condition => {
      this.editConditionForm = new FormGroup({
        name: new FormControl(condition.name, [Validators.required]),
        description: new FormControl(condition.description, [Validators.required])
      });

      this.editConditionForm.valueChanges.subscribe(change => {
        this.tab.dirtyFlag = true;
      });
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
      this.spinner.hide();
    });
  }

  save = () => {
    //Validate fields
    Object.values(this.editConditionForm.controls).forEach(control => {
      control.markAllAsTouched();
    });

    if (this.editConditionForm.valid) {
      let name = this.editConditionForm.get('name')?.value;
      let description = this.editConditionForm.get('description')?.value;

      this.spinner.show();
      this.conditionService.updateCondition({
        patchData: [{
          operation: 'REPLACE',
          property: 'NAME',
          value: name
        },
        {
          operation: 'REPLACE',
          property: 'DESCRIPTION',
          value: description
        }]
      }, this.tab.id).subscribe(condition => {
        this.tab.title = condition.name;
        this.tab.dirtyFlag = false;
        this.conditionNotificationService.triggerUpdateNotification(condition);
        this.spinner.hide();
      }, error => {
        this.dialog.open(MessageComponent, {
          data: {
            type: "error",
            message: "Failed to save condition.",
            callbackText: "Retry",
            callback: this.save
          }
        });
      })
    }
  }

  delete = () => {
    const dialogRef = this.dialog.open(ConfirmationComponent, {
      data: {
        title: "Delete Condition",
        icon: "warning",
        message: "Are you sure to delete ?"
      }
    });

    dialogRef.afterClosed().subscribe(okToDelete => {
      if (okToDelete) {
        this.spinner.show();
        this.conditionService.deleteCondition(this.tab.id).subscribe(condition => {
          this.spinner.hide();
          this.conditionNotificationService.triggerDeleteNotification(condition.id);
        }, error => {
          this.dialog.open(MessageComponent, {
            data: {
              type: "error",
              message: "Failed to delete condition.",
              callbackText: "Retry",
              callback: this.delete
            }
          });
          this.spinner.hide();
        });
      }
    });
  }
}
