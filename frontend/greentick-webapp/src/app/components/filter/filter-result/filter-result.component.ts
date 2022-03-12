import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FilterLanguageResultEditorOptions } from 'src/app/lang/filter/filter-language-result.editor.options';
import { ExpressionResultResponse, FilterResultResponse, FilterService } from 'src/gen/filter';
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
      let decorations = this.result.expressionResults.flatMap(r => this.getModelDeltaDecoration(r));
      this.editor.deltaDecorations([], decorations);
    }
  }

  private getModelDeltaDecoration(expressionResult: ExpressionResultResponse): monaco.editor.IModelDeltaDecoration[] {
    if (expressionResult.type == ExpressionResultResponse.TypeEnum.ARITHEMETICEXPRESSION) {
      return this.getDecorationForArithmeticExp(expressionResult);
    } else if (expressionResult.type == ExpressionResultResponse.TypeEnum.COMPAREEXPRESSION) {
      return this.getDecorationForCompareExp(expressionResult);
    }
    return this.getDecorationForOtherExp(expressionResult);
  }

  private getDecorationForArithmeticExp(expressionResult: ExpressionResultResponse): monaco.editor.IModelDeltaDecoration[] {
    let decorations: monaco.editor.IModelDeltaDecoration[] = [];
    expressionResult.location.forEach(location => {
      decorations.push({
        range: new monaco.Range(location.start.line, location.start.column, location.end.line, location.end.column),
        options: {
          inlineClassName: "expression-result-arithmetic",
          hoverMessage: {
            value: expressionResult.result
          }
        }
      } as monaco.editor.IModelDeltaDecoration);
    });
    return decorations;
  }

  private getDecorationForCompareExp(expressionResult: ExpressionResultResponse): monaco.editor.IModelDeltaDecoration[] {
    let decorations: monaco.editor.IModelDeltaDecoration[] = [];
    expressionResult.location.forEach(location => {
      decorations.push({
        range: new monaco.Range(location.start.line, location.start.column, location.end.line, location.end.column),
        options: {
          inlineClassName: "expression-result-compare-" + expressionResult.result,
        }
      } as monaco.editor.IModelDeltaDecoration);
    });
    return decorations;
  }

  private getDecorationForOtherExp(expressionResult: ExpressionResultResponse): monaco.editor.IModelDeltaDecoration[] {
    let decorations: monaco.editor.IModelDeltaDecoration[] = [];
    expressionResult.location.forEach(location => {
      decorations.push({
        range: new monaco.Range(location.start.line, location.start.column, location.end.line, location.end.column),
        options: {
          hoverMessage: {
            value: expressionResult.result
          }
        }
      } as monaco.editor.IModelDeltaDecoration);
    });
    return decorations;
  }
}
