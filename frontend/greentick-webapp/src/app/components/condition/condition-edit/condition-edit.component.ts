import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ConditionLanguageEditorOptions } from 'src/app/lang/condition/condition-language.editor.options';
import { Tab } from 'src/app/services/tab-area.service';
import { Condition, ConditionService } from 'src/gen/condition';
import { ConfirmationComponent } from '../../common/confirmation/confirmation.component';
import { MessageComponent } from '../../common/message/message.component';
import { ConditionNotificationService } from '../condition-notification.service';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { ConditionLanguageParser, LibraryError, SytntaxError } from 'src/app/lang/condition/condition-language.parser';
import { EditorService } from 'src/app/services/editor.service';
import { FilterService } from 'src/gen/filter';
import { ConditionLanguageIntellisense } from 'src/app/lang/condition/condition-language.intellisense';

@Component({
  selector: 'app-condition-edit',
  templateUrl: './condition-edit.component.html',
  styleUrls: ['./condition-edit.component.scss']
})
export class ConditionEditComponent implements OnInit {

  private conditionLanguageParser: ConditionLanguageParser;

  fetchError: boolean;
  fetchError2: boolean;

  editConditionForm: FormGroup;
  editorOptions = new ConditionLanguageEditorOptions();

  @Input()
  tab: Tab;

  get nameControl(): FormControl {
    return this.editConditionForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.editConditionForm.get('description') as FormControl;
  }

  get codeControl(): FormControl {
    return this.editConditionForm.get('code') as FormControl;
  }


  constructor(private filterService: FilterService, private editorService: EditorService,
    private conditionService: ConditionService, private conditionNotificationService: ConditionNotificationService,
    private dialog: MatDialog, private spinner: NgxSpinnerService) {
    this.conditionLanguageParser = new ConditionLanguageParser();
  }

  ngOnInit(): void {
    this.refresh();
    this.refresh2();
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();
    this.conditionService.getCondition(this.tab.id).subscribe(condition => {
      this.editConditionForm = new FormGroup({
        name: new FormControl(condition.name, [Validators.required]),
        description: new FormControl(condition.description, [Validators.required]),
        code: new FormControl(condition.code, [Validators.required])
      });

      this.editConditionForm.valueChanges.subscribe(change => {
        let changedCondition: Condition = change;
        this.tab.dirtyFlag = changedCondition.name != condition.name ||
          changedCondition.description != condition.description ||
          !this.isSame(changedCondition.code, condition.code);
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
    this.filterService.getAllFilters().subscribe(filters => {
      ConditionLanguageIntellisense.FILTERS = filters;
      this.spinner.hide();
    }, error => {
      this.fetchError2 = true;
      this.spinner.hide();
    });
  }

  private isSame(str1: string, str2: string): boolean {
    let s1 = str1 == null ? "" : str1;
    let s2 = str2 == null ? "" : str2;
    return s1 == s2;
  }

  save = () => {
    //Validate fields
    Object.values(this.editConditionForm.controls).forEach(control => {
      control.markAllAsTouched();
    });

    if (this.editConditionForm.valid) {
      let name = this.editConditionForm.get('name')?.value;
      let description = this.editConditionForm.get('description')?.value;
      let code = this.editConditionForm.get('code')?.value;

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
        },
        {
          operation: 'REPLACE',
          property: 'CODE',
          value: code
        }]
      }, this.tab.id).subscribe(condition => {
        this.tab.title = condition.name;
        this.tab.dirtyFlag = false;
        this.conditionNotificationService.triggerUpdateNotification(condition);
        this.spinner.hide();
      }, error => {
        this.spinner.hide();
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

  /**
   * EDITOR Configurations
   */
  onEditorInit(editor: monaco.editor.IStandaloneCodeEditor) {
    editor.onDidChangeModelContent((event) => {
      let model = editor.getModel();
      this.updateSyntaxError(model);
      this.updateLibraryError(model);
    });
  }

  private updateSyntaxError(model: monaco.editor.ITextModel) {
    let syntaxError: SytntaxError = this.conditionLanguageParser.parseForSyntaxError(model.getValue());
    if (syntaxError) {
      this.editorService.setModelMarkers(model, "Sytax Error", [
        {
          startLineNumber: syntaxError.startLineNumber,
          startColumn: syntaxError.startColumn,
          endLineNumber: syntaxError.endLineNumber,
          endColumn: syntaxError.endColumn,
          message: syntaxError.message,
          severity: monaco.MarkerSeverity.Error
        }
      ]);
    } else {
      this.editorService.setModelMarkers(model, "Sytax Error", []);
    }
  }

  private updateLibraryError(model: monaco.editor.ITextModel) {
    let libraryErrors: LibraryError[] = this.conditionLanguageParser.parseForLibraryErrors(model.getValue(), this.isValidFilterName);
    if (libraryErrors) {
      let markers: monaco.editor.IMarkerData[] = libraryErrors.filter(libraryError => libraryError != null)
        .map(libraryError => {
          return {
            startLineNumber: libraryError.startLineNumber,
            startColumn: libraryError.startColumn,
            endLineNumber: libraryError.endLineNumber,
            endColumn: libraryError.endColumn,
            message: libraryError.message,
            severity: monaco.MarkerSeverity.Error
          }
        });
      this.editorService.setModelMarkers(model, "Library Error", markers);
    } else {
      this.editorService.setModelMarkers(model, "Library Error", []);
    }
  }
  private isValidFilterName(name: string): boolean {
    if (ConditionLanguageIntellisense.FILTERS) {
      return ConditionLanguageIntellisense.FILTERS.find(filter => filter.name == name) != null;
    }
    return false;
  }
}
