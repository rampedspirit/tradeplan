import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FilterLanguageResultEditorOptions } from 'src/app/lang/filter/filter-language-result.editor.options';
import { ExpressionResult, FilterResultResponse, FilterService } from 'src/gen/filter';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { EditorService } from 'src/app/services/editor.service';
import { ThisReceiver } from '@angular/compiler';

@Component({
  selector: 'app-filter-result',
  templateUrl: './filter-result.component.html',
  styleUrls: ['./filter-result.component.scss']
})
export class FilterResultComponent implements OnChanges {

  @Input()
  result: FilterResultResponse;

  editorOptions = new FilterLanguageResultEditorOptions();

  private editor: monaco.editor.IStandaloneCodeEditor;

  constructor(private filterService: FilterService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.result) {
      this.updateCode();
    }
  }

  private updateCode() {
    this.filterService.getFilter(this.result.filterId).subscribe(filter => {
      if (this.editor) {
        this.editor.getModel().setValue(filter.code);
      }
    });
  }

  onEditorInit(editor: monaco.editor.IStandaloneCodeEditor) {
    this.editor = editor;
    this.updateCode();

    editor.onDidChangeModelContent(e => {
      this.updateDecorations();
    });
  }

  private updateDecorations() {
    if (this.editor && this.result) {
      let decorations = this.result.expressionResults.map(r => this.getModelDeltaDecoration(r));
      this.editor.deltaDecorations([], decorations);
    }
  }

  private getModelDeltaDecoration(expressionResult: ExpressionResult): monaco.editor.IModelDeltaDecoration {
    if (expressionResult.type == ExpressionResult.TypeEnum.ARITHMETIC) {
      return this.getDecorationForArithmeticExp(expressionResult);
    } else if (expressionResult.type == ExpressionResult.TypeEnum.COMPARE) {
      return this.getDecorationForCompareExp(expressionResult);
    }
    return null;
  }

  private getDecorationForArithmeticExp(expressionResult: ExpressionResult): monaco.editor.IModelDeltaDecoration {
    return {
      range: new monaco.Range(expressionResult.location.start.line, expressionResult.location.start.column,
        expressionResult.location.end.line, expressionResult.location.end.column),
      options: {
        hoverMessage: {
          value: expressionResult.result
        }
      }
    } as monaco.editor.IModelDeltaDecoration;
  }

  private getDecorationForCompareExp(expressionResult: ExpressionResult): monaco.editor.IModelDeltaDecoration {
    return {
      range: new monaco.Range(expressionResult.location.start.line, expressionResult.location.start.column,
        expressionResult.location.end.line, expressionResult.location.end.column),
      options: {
        inlineClassName: "expression-result-" + expressionResult.type + "-" + this.getRandomStatus(),
      }
    } as monaco.editor.IModelDeltaDecoration;
  }

  private getRandomStatus(): string {
    let statuses = ["QUEUED", "RUNNING", "PASS", "FAIL", "ERROR"];
    const random = Math.floor(Math.random() * statuses.length);
    return statuses[random];
  }
}
