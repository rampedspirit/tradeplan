import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

export class ConditionLanguage implements monaco.languages.IMonarchLanguage {

    private logicalOperators = ["AND", "OR"];

    private rules: monaco.languages.IMonarchLanguageRule[] = [
        [/[a-zA-Z0-9_$][\w$]*/, {
            cases: {
                '@logicalOperators': 'cl-logical-operator',
                '@default': 'cl-filter'
            }
        }],
        [/[\[\]]/, {
            cases: {
                '@default' : 'cl-bracket'
            }
        }]
    ]

    ignoreCase = false;
    defaultToken = "cl-invalid";
    tokenizer = {
        root: this.rules
    }
}