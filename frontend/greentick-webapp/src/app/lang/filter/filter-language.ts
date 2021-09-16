import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { FilterLanguageLibrary } from './filter-language-library';

export class FilterLanguage implements monaco.languages.IMonarchLanguage {

    private library: FilterLanguageLibrary = new FilterLanguageLibrary();
    private functions = this.library.functions.map(func => func.name);
    private logicalOperators = this.library.logicalOperators.map(op => op.symbol);
    private comparisonOperators = this.library.comparisonOperators.map(op => op.symbol);
    private arithmeticOperators = this.library.arithmeticOperators.map(op => op.symbol);
    private argument = /[a-zA-Z0-9-]+/;

    private rules: monaco.languages.IMonarchLanguageRule[] = [
        [/[a-zA-Z0-9_$][\w$]*/, {
            cases: {
                '@logicalOperators': 'fl-logical-operator',
                '@functions': 'fl-function'
            }
        }],
        [/[=><!~?:&|+\-*\/\^%]+/, {
            cases: {
                '@comparisonOperators': 'fl-comparison-operator',
                '@arithmeticOperators': 'fl-arithmetic-operator'
            }
        }],
        [/\(([a-zA-Z0-9-]+(,[a-zA-Z0-9-]+)*)?\)/, 'fl-argument'],
        [/[\[\]]/, '@brackets'],
        [/[0-9]/, 'fl-number']
    ]

    ignoreCase = false;
    defaultToken = "fl-invalid";
    tokenizer = {
        root: this.rules
    }
}