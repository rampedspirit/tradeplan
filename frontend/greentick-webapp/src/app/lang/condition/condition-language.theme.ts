import { BuiltinType } from '@angular/compiler';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

export class ConditionLanguageTheme implements monaco.editor.IStandaloneThemeData {
    base = 'vs' as const;
    inherit = false;
    rules = [
        { token: 'cl-filter', foreground: '2196f3' },
        { token: 'cl-logical-operator', foreground: '0ce781', fontStyle: 'bold' },
        { token: 'cl-bracket', foreground: '2196f3' },
        { token: 'cl-invalid', foreground: 'ff0000' }
    ]
    colors = {}
}
