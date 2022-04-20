import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FilterResponse, FilterService } from 'src/gen/filter';
import { ConditionLanguageParser, SytntaxError } from './condition-language.parser';
import { TokenType } from './token-type';

export class ConditionLanguageIntellisense implements monaco.languages.CompletionItemProvider, monaco.languages.HoverProvider {

    public static FILTERS: FilterResponse[];
    public triggerCharacters: string[] = ['.'];

    provideCompletionItems(model: monaco.editor.ITextModel, position: monaco.Position, context: monaco.languages.CompletionContext, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.CompletionList> {
        var textUntilPosition = model.getValueInRange({
            startLineNumber: 1, startColumn: 1, endLineNumber: position.lineNumber, endColumn: position.column
        });

        let result: SytntaxError = new ConditionLanguageParser().parseForSyntaxError(textUntilPosition);
        let completionItems: monaco.languages.CompletionItem[] = [];
        if (result) {
            if (result.expectedTokenTypes.has(TokenType.LOGICAL_OPERATOR)) {
                completionItems.push(this.createCompletionItem("AND", null, monaco.languages.CompletionItemKind.Keyword));
                completionItems.push(this.createCompletionItem("OR", null, monaco.languages.CompletionItemKind.Keyword));
            } else if (ConditionLanguageIntellisense.FILTERS) {
                ConditionLanguageIntellisense.FILTERS.forEach(filter =>
                    completionItems.push(this.createCompletionItem(filter.name, this.getFilterDocumentation(filter), monaco.languages.CompletionItemKind.Function)));
            }
        }

        if (completionItems.length == 0) {
            ConditionLanguageIntellisense.FILTERS.forEach(filter =>
                completionItems.push(this.createCompletionItem(filter.name, this.getFilterDocumentation(filter), monaco.languages.CompletionItemKind.Function)));
        }

        return {
            suggestions: completionItems
        }
    }

    resolveCompletionItem?(item: monaco.languages.CompletionItem, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.CompletionItem> {
        return item;
    }

    provideHover(model: monaco.editor.ITextModel, position: monaco.Position, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.Hover> {
        let word: string = model.getWordAtPosition(position)?.word;
        let filter: FilterResponse = ConditionLanguageIntellisense.FILTERS.find(f => f.name == word);
        if (filter) {
            return {
                contents: [{ value: this.getFilterDocumentation(filter) }]
            }
        }
        return {
            contents: []
        }
    }

    createCompletionItem(item: String, doc: string, kind: monaco.languages.CompletionItemKind): any {
        return {
            label: item,
            kind: kind,
            documentation: {
                value: doc
            },
            insertText: item,
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
        };
    }

    private getFilterDocumentation(filter: FilterResponse): string {
        let documentation = "# " + filter.name
            + "\n"
            + filter.description;
        return documentation;
    }

}