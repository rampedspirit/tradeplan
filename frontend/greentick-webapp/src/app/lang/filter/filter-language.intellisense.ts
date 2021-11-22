import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { FilterLanguageLibrary } from './filter-language-library';
import { FilterLanguageParser, SytntaxError } from './filter-language.parser';
import { LArgument, LFunction, LOperator } from './filter-language.types';
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
                suggestions: this.getCompletionItems(result.expectedTokenTypes, textUntilPosition)
            }
        } else if (this.hasNextFunction(textUntilPosition)) {
            let result: SytntaxError = new FilterLanguageParser().parseForSyntaxError(textUntilPosition + "#");
            if (result) {
                return {
                    suggestions: this.getCompletionItems(result.expectedTokenTypes, textUntilPosition)
                }
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
            let functionDoc = this.getFunctionDocumentation(func);
            return {
                contents: [this.toMarkDownString(functionDoc)]
            }
        }
        return {
            contents: []
        }
    }

    private getCompletionItems(expectedTokens: Set<TokenType>, text: string): monaco.languages.CompletionItem[] {
        let completionItems: monaco.languages.CompletionItem[] = [];

        if (expectedTokens.has(TokenType.DOT) && this.hasNextFunction(text)) {
            completionItems.push(this.createDotCompletionItem());
        }

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
        let completionItem = {
            label: func.name,
            kind: kind,
            documentation: {
                value: this.getFunctionDocumentation(func),
                isTrusted: true
            },
            insertText: func.name + "(" + this.getDefaultArguments(func) + ")",
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
            sortText: sortText,
            command: { id: "editor.action.triggerSuggest", title: "" }
        };

        if (!func.nextFunctions || func.nextFunctions.length == 0) {
            completionItem.insertText += " ";
        }

        return completionItem;
    }

    private getDefaultArguments(func: LFunction): string {
        return func.arguments.filter(arg => !arg.isOptional)
            .map(arg => arg.defaultValue).join(",");
    }

    private getFunctionDocumentation(func: LFunction): string {
        return func.description + "\n" + func.arguments.map(arg => this.toArgumentDoc(arg)).join("\n") +
            "\n\n[see more](command:open-documentation?{\"type\":\"function\",\"name\":\"" + func.name + "\"})";
    }

    private toArgumentDoc(arg: LArgument): string {
        let optional = arg.isOptional ? " (optional) " : "";
        return "- " + "*" + arg.name + optional + "* - " + arg.description;
    }
    private toMarkDownString(value: string): monaco.IMarkdownString {
        return {
            value: value,
            isTrusted: true
        }
    }

    private hasNextFunction(text: string) {
        let functions: string[] = text.match(/\w*\(\w*(,\w*)*\)*/g);
        if (functions) {
            let lastFunction: string = functions[functions.length - 1];
            let functionName: string = lastFunction.match(/\w*/)[0];
            let func: LFunction = this.library.functions.find(f => f.name == functionName);
            return func.nextFunctions && func.nextFunctions.length > 0;
        }
        return false;
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
            insertText: operator.symbol + " ",
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
            sortText: sortText,
            command: { id: "editor.action.triggerSuggest", title: "" }
        };
    }

    createDotCompletionItem(): any {
        return {
            label: ".",
            kind: monaco.languages.CompletionItemKind.Value,
            insertText: ".",
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
            command: { id: "editor.action.triggerSuggest", title: "" }
        };
    }
}