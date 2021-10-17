import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { FilterLanguageLibrary } from './filter-language-library';
import { FilterLanguageParser, SytntaxError } from './filter-language.parser';
import { LFunction, LOperator } from './filter-language.types';
import { TokenType } from './token-type';

export class FilterLanguageIntellisense implements monaco.languages.CompletionItemProvider, monaco.languages.HoverProvider {

    public triggerCharacters: string[] = ['.'];
    private library: FilterLanguageLibrary = new FilterLanguageLibrary();

    provideCompletionItems(model: monaco.editor.ITextModel, position: monaco.Position, context: monaco.languages.CompletionContext, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.CompletionList> {
        var textUntilPosition = model.getValueInRange({
            startLineNumber: 1, startColumn: 1, endLineNumber: position.lineNumber, endColumn: position.column
        });

        let result: SytntaxError = new FilterLanguageParser().parseForSyntaxError(textUntilPosition);
        if (result) {
            return {
                suggestions: this.getCompletionItems(result.expectedTokenTypes, textUntilPosition, context.triggerCharacter)
            }
        }
        return {
            suggestions: []
        }
    }

    resolveCompletionItem?(item: monaco.languages.CompletionItem, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.CompletionItem> {
        return item;
    }

    provideHover(model: monaco.editor.ITextModel, position: monaco.Position, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.Hover> {
        let word: string = model.getWordAtPosition(position)?.word;
        let func: LFunction = this.library.functions.find(f => f.name == word);
        if (func) {
            return {
                contents: [{ value: this.getFunctionDocumentation(func) }]
            }
        }
        return {
            contents: []
        }
    }

    private getCompletionItems(expectedTokens: Set<TokenType>, text: string, triggerCharacter: string): monaco.languages.CompletionItem[] {
        let completionItems: monaco.languages.CompletionItem[] = [];

        if (expectedTokens.has(TokenType.ARGUMENT)) {
            let functions: string[] = text.match(/\w*\(\w*(,\w*)*\)*/g);
            if (functions) {
                let lastFunction: string = functions[functions.length - 1];
                let functionName: string = lastFunction.match(/\w*/)[0];
                let func: LFunction = this.library.functions.find(f => f.name == functionName);
                let args: string[] = lastFunction.match(/(\w*)(,\w*)*/g)[2].split(",");
                if (args.length > 0 && func.arguments.length >= args.length) {
                    func.arguments[args.length - 1].valueProposals.forEach(value => {
                        completionItems.push(this.createValueCompletionItem(value));
                    })
                } else if (func.arguments.length > 0) {
                    func.arguments[0].valueProposals.forEach(value => {
                        completionItems.push(this.createValueCompletionItem(value));
                    })
                }
            }
            return completionItems;
        }

        if (expectedTokens.has(TokenType.FUNCTION)) {
            if (/.*\.$/.test(text) || /.*\.[\w\(]+$/.test(text)) {
                let functions: string[] = text.match(/(\w*)\((\w*)(,\w*)*\)/g);
                if (functions) {
                    let lastFunction: string = functions[functions.length - 1];
                    let functionName: string = lastFunction.match(/\w*/)[0];
                    let func: LFunction = this.library.functions.find(f => f.name == functionName);
                    func.nextFunctions.forEach(nextFunctionName => {
                        let nextFunction: LFunction = this.library.functions.find(f => f.name == nextFunctionName)
                        completionItems.push(this.createFunctionCompletionItem(nextFunction,
                            monaco.languages.CompletionItemKind.Function, "f"));
                    });
                }
            } else {
                this.library.functions.filter(f => f.isStandalone)
                    .forEach(func => {
                        completionItems.push(this.createFunctionCompletionItem(func,
                            monaco.languages.CompletionItemKind.Function, "f"));
                    });
            }
            return completionItems;
        }

        if (expectedTokens.has(TokenType.LOGICAL_OPERATOR)) {
            this.library.logicalOperators
                .forEach(opeartor => completionItems.push(this.createOperatorCompletionItem(opeartor,
                    monaco.languages.CompletionItemKind.Keyword, "a")));
        }

        if (expectedTokens.has(TokenType.COMPARISON_OPERATOR)) {
            this.library.comparisonOperators
                .forEach(opeartor => completionItems.push(this.createOperatorCompletionItem(opeartor,
                    monaco.languages.CompletionItemKind.Variable, "b")));
        }

        if (expectedTokens.has(TokenType.ARITHMETIC_OPERATOR)) {
            this.library.arithmeticOperators
                .forEach(opeartor => completionItems.push(this.createOperatorCompletionItem(opeartor,
                    monaco.languages.CompletionItemKind.Operator, "c")));
        }
        return completionItems;
    }

    createFunctionCompletionItem(func: LFunction, kind: monaco.languages.CompletionItemKind,
        sortText: string): any {
        return {
            label: func.name,
            kind: kind,
            documentation: {
                value: this.getFunctionDocumentation(func)
            },
            insertText: func.name + "(" + this.getDefaultArguments(func) + ")",
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
            sortText: sortText
        };
    }

    private getDefaultArguments(func: LFunction): string {
        return func.arguments.filter(arg => !arg.isOptional)
            .map(arg => arg.defaultValue).join(",");
    }

    private getFunctionDocumentation(func: LFunction): string {
        let documentation = "# " + func.name
            + "\n"
            + func.description
            + "\n\n"
            + "| Argument | Required | Description |"
            + "\n"
            + "|:--|:--|:--|"
            + "\n"
            + func.arguments.map(arg => "| " + arg.name + " | " + !arg.isOptional + " | " + arg.description + " |").join("\n");

        if (func.moreInfo) {
            documentation += "\n" + func.moreInfo;
        }
        return documentation;
    }

    createValueCompletionItem(value: string): any {
        return {
            label: value,
            kind: monaco.languages.CompletionItemKind.Value,
            insertText: value,
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
        };
    }

    createOperatorCompletionItem(operator: LOperator, kind: monaco.languages.CompletionItemKind,
        sortText: string): any {
        return {
            label: operator.name,
            kind: kind,
            documentation: operator.name,
            insertText: operator.symbol,
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
            sortText: sortText
        };
    }
}