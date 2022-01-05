import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Tab, TabAreaService } from 'src/app/services/tab-area.service';
import { Condition, ConditionService } from 'src/gen/condition';
import { ScreenerService } from 'src/gen/screener';
import { ConfirmationComponent } from '../../common/confirmation/confirmation.component';
import { MessageComponent } from '../../common/message/message.component';
import { ConditionNotificationService } from '../../condition/condition-notification.service';
import { ScreenerNotificationService } from '../screener-notification.service';

@Component({
  selector: 'app-screener-edit',
  templateUrl: './screener-edit.component.html',
  styleUrls: ['./screener-edit.component.scss']
})
export class ScreenerEditComponent implements OnInit {

  fetchError: boolean;
  fetchError2: boolean;

  editScreenerForm: FormGroup;
  conditions: Condition[];

  @Input()
  tab: Tab;

  get nameControl(): FormControl {
    return this.editScreenerForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.editScreenerForm.get('description') as FormControl;
  }

  get conditionControl(): FormControl {
    return this.editScreenerForm.get('condition') as FormControl;
  }

  constructor(private screenerService: ScreenerService, private screenerNotificationService: ScreenerNotificationService,
    private conditionService: ConditionService, private conditionNotificationService: ConditionNotificationService,
    private dialog: MatDialog, private spinner: NgxSpinnerService, private router: Router, private tabAreaService: TabAreaService) {
  }

  ngOnInit(): void {
    this.refresh();
    this.refresh2();

    this.conditionNotificationService.createSubject.subscribe(condition => {
      this.refresh2();
    });

    this.conditionNotificationService.updateSubject.subscribe(condition => {
      this.refresh2();
    });

    this.conditionNotificationService.deleteSubject.subscribe(id => {
      this.refresh2();
    });
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();
    this.screenerService.getScreener(this.tab.id).subscribe(screener => {
      this.editScreenerForm = new FormGroup({
        name: new FormControl(screener.name, [Validators.required]),
        description: new FormControl(screener.description, [Validators.required]),
        condition: new FormControl(screener.conditionId, [Validators.required])
      });

      this.editScreenerForm.valueChanges.subscribe(change => {
        this.tab.dirtyFlag = true;
      });
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
      this.spinner.hide();
    });
  }

  refresh2 = () => {
    this.fetchError2 = false;
    this.spinner.show();
    this.conditionService.getAllConditions().subscribe(conditions => {
      this.spinner.hide();
      this.conditions = conditions;
    }, error => {
      this.fetchError2 = true;
      this.spinner.hide();
    });
  }

  save = () => {
    //Validate fields
    Object.values(this.editScreenerForm.controls).forEach(control => {
      control.markAllAsTouched();
    });

    if (this.editScreenerForm.valid) {
      let name = this.editScreenerForm.get('name')?.value;
      let description = this.editScreenerForm.get('description')?.value;

      this.spinner.show();
      this.screenerService.updateScreener({
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
      }, this.tab.id).subscribe(screener => {
        this.tab.title = screener.name;
        this.tab.dirtyFlag = false;
        this.screenerNotificationService.triggerUpdateNotification(screener);
        this.spinner.hide();
      }, error => {
        this.spinner.hide();
        this.dialog.open(MessageComponent, {
          data: {
            type: "error",
            message: "Failed to save screener.",
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
        title: "Delete Screener",
        icon: "warning",
        message: "Are you sure to delete ?"
      }
    });

    dialogRef.afterClosed().subscribe(okToDelete => {
      if (okToDelete) {
        this.spinner.show();
        this.screenerService.deleteScreener(this.tab.id).subscribe(screener => {
          this.spinner.hide();
          this.screenerNotificationService.triggerDeleteNotification(screener.screenerId);
        }, error => {
          this.dialog.open(MessageComponent, {
            data: {
              type: "error",
              message: "Failed to delete screener.",
              callbackText: "Retry",
              callback: this.delete
            }
          });
          this.spinner.hide();
        });
      }
    });
  }

  navigateToConditionsList = () => {
    this.router.navigate(["/condition/list"]);
    this.tabAreaService.closeTab(this.tab.id);
  }
}
