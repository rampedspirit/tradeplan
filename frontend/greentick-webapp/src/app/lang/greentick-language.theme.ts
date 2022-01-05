import { BuiltinType } from '@angular/compiler';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

export class GreentickLanguageTheme implements monaco.editor.IStandaloneThemeData {
    base = 'vs' as const;
    inherit = false;
    rules = [
        { token: 'fl-function', foreground: '2196f3' },
        { token: 'fl-argument', foreground: 'BB9402' },
        { token: 'fl-logical-operator', foreground: '0ce781', fontStyle: 'bold' },
        { token: 'fl-arithmetic-operator', foreground: '6d0370', fontStyle: 'bold' },
        { token: 'fl-comparison-operator', foreground: '535353', fontStyle: 'bold' },
        { token: 'fl-bracket', foreground: '2196f3' },
        { token: 'fl-number', foreground: '000000', fontStyle: 'bold' },
        { token: 'fl-invalid', foreground: 'ff0000' },
        { token: 'cl-filter', foreground: '2196f3' },
        { token: 'cl-logical-operator', foreground: '0ce781', fontStyle: 'bold' },
        { token: 'cl-bracket', foreground: '2196f3' },
        { token: 'cl-invalid', foreground: 'ff0000' }
    ]
    colors = {}
}
