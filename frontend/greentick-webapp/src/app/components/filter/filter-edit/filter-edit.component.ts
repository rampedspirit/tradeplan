import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FilterLanguageEditorOptions } from 'src/app/lang/filter/filter-language.editor.options';
import { FilterLanguageParser, LibraryError, SytntaxError } from 'src/app/lang/filter/filter-language.parser';
import { EditorService } from 'src/app/services/editor.service';
import { Filter, FilterService } from 'src/app/_gen';

import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { NgxSpinnerService } from 'ngx-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-filter-edit',
  templateUrl: './filter-edit.component.html',
  styleUrls: ['./filter-edit.component.scss']
})
export class FilterEditComponent implements OnInit {
  private filterLanguageParser: FilterLanguageParser;

  editFilterForm: FormGroup;
  editorOptions = new FilterLanguageEditorOptions();

  @Input()
  filter: Filter;


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
    private snackBar: MatSnackBar, private spinner: NgxSpinnerService) {
    this.filterLanguageParser = new FilterLanguageParser();
  }

  ngOnInit(): void {
    this.editFilterForm = new FormGroup({
      name: new FormControl(this.filter.name, [Validators.required]),
      description: new FormControl(this.filter.description, [Validators.required]),
      code: new FormControl(this.filter.code, [Validators.required])
    });
  }

  onEditorInit(editor: monaco.editor.IStandaloneCodeEditor) {
    editor.onDidChangeModelContent((event) => {
      let model = editor.getModel();
      this.updateSyntaxError(model);
      this.updateLibraryError(model);
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

  save(): void {
    this.validateAllFields();
    if (this.editFilterForm.valid) {
      let name = this.editFilterForm.get('name')?.value;
      let description = this.editFilterForm.get('description')?.value;
      let code = this.editFilterForm.get('code')?.value;
      this.update(name, description, code);
    }
  }

  private validateAllFields() {
    Object.values(this.editFilterForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private update(name: string, description: string, code: string) {
    this.spinner.show();

    this.filterService.updateFilter({
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
    }, this.filter.id).subscribe(filter => {
      this.spinner.hide();
    }, error => {
      this.snackBar.open("Failed to save filter '" + name + "', Please Retry.", "OK", {
        verticalPosition: "bottom",
        horizontalPosition: "center"
      });

      this.spinner.hide();
    })
  }
}
