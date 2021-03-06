import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FilterLanguageEditorOptions } from 'src/app/lang/filter/filter-language.editor.options';
import { FilterLanguageParser, LibraryError, SytntaxError } from 'src/app/lang/filter/filter-language.parser';
import { EditorService, MonacoWrapper } from 'src/app/services/editor.service';
import { FilterResponse, FilterService } from 'src/gen/filter';

import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { NgxSpinnerService } from 'ngx-spinner';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationComponent } from '../../common/confirmation/confirmation.component';
import { Tab } from 'src/app/services/tab-area.service';
import { FilterNotificationService } from '../filter-notification.service';
import { MessageComponent } from '../../common/message/message.component';
import { FilterValidators } from '../filter-validators';

@Component({
  selector: 'app-filter-edit',
  templateUrl: './filter-edit.component.html',
  styleUrls: ['./filter-edit.component.scss']
})
export class FilterEditComponent implements OnInit {

  private filterLanguageParser: FilterLanguageParser;

  fetchError: boolean;
  editFilterForm: FormGroup;
  editorOptions = new FilterLanguageEditorOptions();

  @Input()
  tab: Tab;

  get nameControl(): FormControl {
    return this.editFilterForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.editFilterForm.get('description') as FormControl;
  }

  get codeControl(): FormControl {
    return this.editFilterForm.get('code') as FormControl;
  }

  constructor(private editorService: EditorService, private filterService: FilterService,
    private filterNotificationService: FilterNotificationService, private dialog: MatDialog,
    private spinner: NgxSpinnerService) {
    this.filterLanguageParser = new FilterLanguageParser();
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();
    this.filterService.getFilter(this.tab.id).subscribe(filter => {
      this.editFilterForm = new FormGroup({
        name: new FormControl(filter.name, [Validators.required, FilterValidators.noSpace],
          [FilterValidators.notUnique(this.filterService, [filter.name])]),
        description: new FormControl(filter.description, [Validators.required]),
        code: new FormControl(filter.code, [Validators.required])
      });

      this.editFilterForm.valueChanges.subscribe(change => {
        let changedFilter: FilterResponse = change;
        this.tab.dirtyFlag = changedFilter.name != filter.name ||
          changedFilter.description != filter.description ||
          !this.isSame(changedFilter.code, filter.code);
      });
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
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
    Object.values(this.editFilterForm.controls).forEach(control => {
      control.markAllAsTouched();
    });

    if (this.editFilterForm.valid) {
      let name = this.editFilterForm.get('name')?.value;
      let description = this.editFilterForm.get('description')?.value;
      let code = this.editFilterForm.get('code')?.value;
      let parseTree = JSON.stringify(this.filterLanguageParser.getParseTree(code));
      this.spinner.show();
      this.filterService.updateFilter(
        [{
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
        },
        {
          operation: 'REPLACE',
          property: 'PARSE_TREE',
          value: parseTree
        }], this.tab.id).subscribe(filter => {
          this.tab.title = filter.name;
          this.tab.dirtyFlag = false;
          this.filterNotificationService.triggerUpdateNotification(filter);
          this.spinner.hide();
        }, error => {
          this.spinner.hide();
          this.dialog.open(MessageComponent, {
            data: {
              type: "error",
              message: "Failed to save filter.",
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
        title: "Delete Filter",
        icon: "warning",
        message: "Are you sure to delete ?"
      }
    });

    dialogRef.afterClosed().subscribe(okToDelete => {
      if (okToDelete) {
        this.spinner.show();
        this.filterService.deleteFilter(this.tab.id).subscribe(filter => {
          this.spinner.hide();
          this.filterNotificationService.triggerDeleteNotification(filter.id);
        }, error => {
          this.dialog.open(MessageComponent, {
            data: {
              type: "error",
              message: "Failed to delete filter.",
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

    editor.onMouseUp(event => {
      console.log(event);
      if (!event.target.detail?.isAfterLines) {
        editor.trigger(null, 'editor.action.triggerSuggest', null);
      }
    });

    editor.onKeyUp(event => {
      if (event.code == "Space") {
        editor.trigger(null, 'editor.action.triggerSuggest', null);
      }
    });
  }

  private updateSyntaxError(model: monaco.editor.ITextModel) {
    let syntaxError: SytntaxError = this.filterLanguageParser.parseForSyntaxError(model.getValue());
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
    let libraryErrors: LibraryError[] = this.filterLanguageParser.parseForLibraryErrors(model.getValue());
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
}
