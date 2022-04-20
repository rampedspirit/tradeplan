import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FilterLanguageResultEditorOptions } from 'src/app/lang/filter/filter-language-result.editor.options';
import { ExpressionResultResponse, FilterResultResponse, FilterService, Location } from 'src/gen/filter';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

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

  private decorations: string[] = [];
  private zoneIds: string[] = [];
  private contentWidgets: monaco.editor.IContentWidget[] = [];

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
      this.addDecorations();
      this.addResultLines();
      this.addResults();
    });
  }

  private addDecorations() {
    if (this.editor && this.result) {
      let newDecorations: monaco.editor.IModelDeltaDecoration[] = [];

      this.result.expressionResults.forEach(expResult => {
        let classNamePrefix = "";
        if (expResult.type == ExpressionResultResponse.TypeEnum.ARITHEMETICEXPRESSION) {
          classNamePrefix = "expression-result-arithmetic";
        } else if (expResult.type == ExpressionResultResponse.TypeEnum.COMPAREEXPRESSION) {
          classNamePrefix = "expression-result-compare-" + expResult.result;
        }

        expResult.location.forEach(location => {
          newDecorations.push({
            range: new monaco.Range(location.start.line, location.start.column, location.end.line, location.end.column),
            options: {
              inlineClassName: classNamePrefix,
            }
          } as monaco.editor.IModelDeltaDecoration);
        });
      });

      this.decorations = this.editor.deltaDecorations(this.decorations, newDecorations);
    }
  }

  private addResultLines() {
    let locations: Location[] = this.result.expressionResults
      .filter(expResult => expResult.type == ExpressionResultResponse.TypeEnum.ARITHEMETICEXPRESSION)
      .flatMap(expResult => expResult.location);

    this.editor.changeViewZones(accessor => {
      this.zoneIds.forEach(zoneId => accessor.removeZone(zoneId));
      this.zoneIds = [];
      new Set(locations.map(location => location.start.line)).forEach(line => {
        let domNode = document.createElement("div");
        let zoneId = accessor.addZone({
          afterLineNumber: line - 1,
          heightInLines: 1,
          domNode: domNode
        });
        this.zoneIds.push(zoneId);
      });
    });
  }
  private addResults() {
    this.contentWidgets.forEach(contentWidget => this.editor.removeContentWidget(contentWidget));
    this.contentWidgets = [];

    let arithmeticExpressionResults = this.result.expressionResults
      .filter(expResult => expResult.type == ExpressionResultResponse.TypeEnum.ARITHEMETICEXPRESSION);

    arithmeticExpressionResults.forEach(expressionResult => {
      let expressionResultIndex = arithmeticExpressionResults.indexOf(expressionResult);
      expressionResult.location.forEach(location => {
        let locationIndex = expressionResult.location.indexOf(location);
        this.contentWidgets.push(this.createResultContentWidget(expressionResultIndex + "." + locationIndex, location, expressionResult.result));
      });
    });

    this.contentWidgets.forEach(contentWidget => this.editor.addContentWidget(contentWidget));
  }

  private createResultContentWidget(id: string, location: Location, result: string | number): monaco.editor.IContentWidget {
    return {
      getId: function () {
        return 'result.content.widget.' + id;
      },
      getDomNode: function () {
        let domNode = document.createElement('div');
        domNode.innerHTML = result as string;
        domNode.className = "expression-result-value";
        return domNode;
      },
      getPosition: function () {
        return {
          position: {
            lineNumber: location.start.line,
            column: location.start.column
          },
          preference: [
            monaco.editor.ContentWidgetPositionPreference.ABOVE
          ]
        };
      }
    } as monaco.editor.IContentWidget
  }
}
