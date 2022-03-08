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

    editor.onMouseDown(e => {
      let selectedToken = editor.getModel().getWordAtPosition(e.target.position);
      if (selectedToken != null) {
        let filter = ConditionLanguageIntellisense.FILTERS.find(f => f.name == selectedToken.word);
        if (filter) {
          this.onFilterSelect.emit(filter.id);
        }
      }
    });

    editor.onDidChangeModelContent(e => {
      this.updateDecorations();
    })
  }

  private updateDecorations() {
    if (this.editor && this.result && this.result.filtersResult) {
      let decorations = this.result.filtersResult.flatMap(result => this.getModelDeltaDecoration(result));
      this.editor.deltaDecorations([], decorations);
    }
  }
  private getModelDeltaDecoration(filterResult: FilterResultResponse): monaco.editor.IModelDeltaDecoration[] {
    let decorations: monaco.editor.IModelDeltaDecoration[] = [];
    filterResult.location.forEach(location => {
      decorations.push(
        {
          range: new monaco.Range(location.start.line, location.start.column,
            location.end.line, location.end.column),
          options: {
            inlineClassName: "filter-result-" + filterResult.status,
            hoverMessage: {
              value: filterResult.status
            }
          }
        } as monaco.editor.IModelDeltaDecoration
      );
    })
    return decorations;
  }
}
