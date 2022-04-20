import { BuiltinType } from '@angular/compiler';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

export class GreentickLanguageTheme implements monaco.editor.IStandaloneThemeData {
    base = 'vs' as const;
    inherit = false;
    rules = [
        { token: 'fl-function', foreground: '2C75BB' },
        { token: 'fl-argument', foreground: '989CA2' },
        { token: 'fl-logical-operator', foreground: 'CF9C6B' },
        { token: 'fl-arithmetic-operator', foreground: 'BB722C', fontStyle: 'bold' },
        { token: 'fl-comparison-operator', foreground: 'BB722C', fontStyle: 'bold' },
        { token: 'fl-bracket', foreground: '989CA2', fontStyle: 'bold' },
        { token: 'fl-number', foreground: '1F5283' },
        { token: 'fl-invalid', foreground: 'ff0000' },
        { token: 'cl-filter', foreground: '2C75BB' },
        { token: 'cl-logical-operator', foreground: 'CF9C6B' },
        { token: 'cl-bracket', foreground: '989ca2', fontStyle: 'bold' },
        { token: 'cl-invalid', foreground: 'ff0000' }
    ]
    colors = {}
}
