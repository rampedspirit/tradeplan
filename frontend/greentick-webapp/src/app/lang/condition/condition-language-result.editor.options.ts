import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { GreentickLanguageConstants } from '../greentick-language.constants';
import { ConditionLanguageConstants } from './condition-language.constants';

export class ConditionLanguageResultEditorOptions implements monaco.editor.IStandaloneEditorConstructionOptions {
    language = ConditionLanguageConstants.ResultLanguageId;
    contextmenu = false;
    theme = GreentickLanguageConstants.ThemeId
    fontSize = 18
    scrollBeyondLastLine = false;
    automaticLayout = true;
    fixedOverflowWidgets = true;
    wordBasedSuggestions = false;
    occurrencesHighlight = false;
    selectionHighlight = false;
    readOnly = true;
}