import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

export class GreentickLanguageConfiguration implements monaco.languages.LanguageConfiguration {
    surroundingPairs = [{
        open: "[",
        close: "]"
    }]

    autoClosingPairs = [{
        open: "[",
        close: "]"
    }]

    _square_brackets: [string, string] = ["[", "]"];
    brackets = [this._square_brackets]
}