import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ConditionLanguageResultEditorOptions } from 'src/app/lang/condition/condition-language-result.editor.options';
import { ConditionLanguageIntellisense } from 'src/app/lang/condition/condition-language.intellisense';
import { ConditionResultResponse, FilterResultResponse } from 'src/gen/condition';
import { FilterService } from 'src/gen/filter';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-condition-result',
  templateUrl: './condition-result.component.html',
  styleUrls: ['./condition-result.component.scss']
})
export class ConditionResultComponent implements OnChanges {

  @Input()
  result: ConditionResultResponse;

  @Output()
  onFilterSelect: EventEmitter<string> = new EventEmitter<string>();

  editorOptions = new ConditionLanguageResultEditorOptions();

  private editor: monaco.editor.IStandaloneCodeEditor;

  constructor(private filterService: FilterService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.result) {
      if (ConditionLanguageIntellisense.FILTERS) {
        this.updateCode();
      } else {
        this.filterService.getAllFilters().subscribe(filters => {
          ConditionLanguageIntellisense.FILTERS = filters;
          this.updateCode();
        });
      }
    }
  }

  private updateCode() {
    if (this.editor) {
      let code = this.replaceFilterIdWithName(this.result.code);
      this.editor.getModel().setValue(code);
    }
  }

  private replaceFilterIdWithName(code: string) {
    let replacedCode = code;
    let filterIds = Array.from(code.matchAll(/[a-zA-Z0-9-]*/g)).map(match => {
      return ConditionLanguageIntellisense.FILTERS.find(filter => filter.id == match[0]);
    }).filter(f => f != null || f != undefined).map(f => f.id);

    filterIds.forEach(filterId => {
      let filterName = ConditionLanguageIntellisense.FILTERS.find(f => f.id == filterId).name;
      replacedCode = replacedCode.replace(filterId, filterName);
    });
    return replacedCode;
  }

  onEditorInit(editor: monaco.editor.IStandaloneCodeEditor) {
    this.editor = editor;
    this.updateCode();
    this.updateDecorations();

    let decorations: string[] = [];
    editor.onMouseDown(e => {
      if (e.target.position) {
        let selectedToken = editor.getModel().getWordAtPosition(e.target.position);
        if (selectedToken != null) {
          let filter = ConditionLanguageIntellisense.FILTERS.find(f => f.name == selectedToken.word);
          if (filter) {
            this.onFilterSelect.emit(filter.id);

            decorations = editor.deltaDecorations(decorations, [{
              range: {
                startLineNumber: e.target.position.lineNumber,
                endLineNumber: e.target.position.lineNumber,
                startColumn: selectedToken.startColumn,
                endColumn: selectedToken.endColumn
              },
              options: {
                inlineClassName: "selected"
              }
            }]);
          }
        }
      }
    });

    editor.onDidChangeModelContent(e => {
      this.updateDecorations();
    })
  }

  private updateDecorations() {
    if (this.editor && this.result && this.result.filtersResult) {
      let decorations = this.result.filtersResult.flatMap(result => this.getModelDeltaDecoration(result, this.result.scripName));
      this.editor.deltaDecorations([], decorations);
    }
  }

  private getModelDeltaDecoration(filterResult: FilterResultResponse, scripName: string): monaco.editor.IModelDeltaDecoration[] {
    let filterName = ConditionLanguageIntellisense.FILTERS.find(filter => filter.id == filterResult.filterId).name;
    let hoverText = this.getHoverText(filterResult.status, scripName, filterName);
    let backgroundColor = this.getBackgroundColor(filterResult.status);
    let borderColor = this.getBorderColor(filterResult.status);
    let decorations: monaco.editor.IModelDeltaDecoration[] = [];
    filterResult.location.forEach(location => {
      decorations.push(
        {
          range: new monaco.Range(location.start.line, location.start.column,
            location.end.line, location.end.column),
          options: {
            inlineClassName: "filter-result-" + filterResult.status,
            hoverMessage: {
              isTrusted: true,
              supportHtml: true,
              value: `<span style="color:#6B9ECF;background-color:` + backgroundColor + `;">` + hoverText + `</span>`
            }
          }
        } as monaco.editor.IModelDeltaDecoration
      );
    })
    return decorations;
  }

  private getBackgroundColor(status: FilterResultResponse.StatusEnum): string {
    let backgroundColor = "";
    if (status == FilterResultResponse.StatusEnum.QUEUED
      || status == FilterResultResponse.StatusEnum.RUNNING) {
      backgroundColor = "#F5F8FC";
    } else if (status == FilterResultResponse.StatusEnum.PASS) {
      backgroundColor = "#F8FCF5";
    } else if (status == FilterResultResponse.StatusEnum.FAIL
      || status == FilterResultResponse.StatusEnum.ERROR) {
      backgroundColor = "#FCF5F8";
    }
    return backgroundColor;
  }

  private getBorderColor(status: FilterResultResponse.StatusEnum): string {
    let borderColor = "";
    if (status == FilterResultResponse.StatusEnum.QUEUED
      || status == FilterResultResponse.StatusEnum.RUNNING) {
      borderColor = "#EAF1F8";
    } else if (status == FilterResultResponse.StatusEnum.PASS) {
      borderColor = "#F1F8EA";
    } else if (status == FilterResultResponse.StatusEnum.FAIL
      || status == FilterResultResponse.StatusEnum.ERROR) {
      borderColor = "#F8EAF1";
    }
    return borderColor;
  }

  private getHoverText(status: FilterResultResponse.StatusEnum, scripName: string, filterName: string) {
    let hoverText = "";
    if (status == FilterResultResponse.StatusEnum.QUEUED
      || status == FilterResultResponse.StatusEnum.RUNNING) {
      hoverText = scripName + " screening in progress. Click " + filterName + " for more details.";
    } else if (status == FilterResultResponse.StatusEnum.PASS) {
      hoverText = scripName + " screened successfully. Click " + filterName + " for more details.";
    } else if (status == FilterResultResponse.StatusEnum.FAIL
      || status == FilterResultResponse.StatusEnum.ERROR) {
      hoverText = scripName + " failed to get screened. Click " + filterName + " for more details.";
    }
    return hoverText;
  }
}
